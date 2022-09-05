/*
 * Copyright 2022 QuiltMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.impl.content.registry.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.util.TriState;
import org.quiltmc.qsl.block.content.registry.api.ReversibleBlockEntry;
import org.quiltmc.qsl.registry.api.event.RegistryMonitor;
import org.quiltmc.qsl.registry.attachment.api.RegistryEntryAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

public class QuiltDeferringQueues<T> {
	private static final TriState CRASH_ON_DEFERRING_ENTRY = TriState.fromProperty("quilted_fabric_api.quilted_fabric_content_registries_v0.crash_on_deferring_entry");
	private static final Logger LOGGER = LoggerFactory.getLogger("Quilted Fabric Content Registries");

	public static final DeferringQueue<Block> BLOCK = new DeferringQueue<>(Registry.BLOCK);
	public static final DeferringQueue<Item> ITEM = new DeferringQueue<>(Registry.ITEM);
	public static final DeferringQueue<GameEvent> GAME_EVENT = new DeferringQueue<>(Registry.GAME_EVENT);

	public static final Map<RegistryEntryAttachment<Object, Object>, List<RegistryEntryAttachment.Entry<Object, Object>>> OMNIQUEUE = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <K, V> void addEntry(RegistryEntryAttachment<K, V> queue, K entry, V value) {
		boolean hasDeferredKey = deferEntry(entry);
		boolean hasDeferredValue = deferEntry(value);

		if (hasDeferredKey || hasDeferredValue) {
			if (!OMNIQUEUE.containsKey(queue)) {
				OMNIQUEUE.put((RegistryEntryAttachment<Object, Object>) queue, new ArrayList<>());
			}

			OMNIQUEUE.get(queue).add(new RegistryEntryAttachment.Entry<>(entry, value));
		} else {
			queue.put(entry, value);
		}
	}

	public static <V> boolean deferEntry(V value) {
		if (value instanceof Block block) {
			return QuiltDeferringQueues.BLOCK.deferEntry(block);
		} else if (value instanceof BlockState state) {
			return QuiltDeferringQueues.BLOCK.deferEntry(state.getBlock());
		} else if (value instanceof ReversibleBlockEntry entry) {
			return QuiltDeferringQueues.BLOCK.deferEntry(entry.block());
		} else if (value instanceof Item item) {
			return QuiltDeferringQueues.ITEM.deferEntry(item);
		} else if (value instanceof GameEvent event) {
			return QuiltDeferringQueues.GAME_EVENT.deferEntry(event);
		}

		return false;
	}

	public static <V> boolean isEntryDeferred(V value) {
		if (value instanceof Block block) {
			return QuiltDeferringQueues.BLOCK.isDeferred(block);
		} else if (value instanceof BlockState state) {
			return QuiltDeferringQueues.BLOCK.isDeferred(state.getBlock());
		} else if (value instanceof ReversibleBlockEntry entry) {
			return QuiltDeferringQueues.BLOCK.isDeferred(entry.block());
		} else if (value instanceof Item item) {
			return QuiltDeferringQueues.ITEM.isDeferred(item);
		} else if (value instanceof GameEvent event) {
			return QuiltDeferringQueues.GAME_EVENT.isDeferred(event);
		}

		return false;
	}

	public static void updateOmniqueue() {
		var entriesToRemove = new ArrayList<>();

		for (var entry : OMNIQUEUE.entrySet()) {
			var entriesToRemove2 = new ArrayList<>();

			for (var listEntry : entry.getValue()) {
				if (!isEntryDeferred(listEntry.entry()) && !isEntryDeferred(listEntry.value())) {
					entry.getKey().put(listEntry.entry(), listEntry.value());
					entriesToRemove2.add(listEntry.entry());
				}
			}

			entry.getValue().removeAll(entriesToRemove2);
			if (entry.getValue().size() == 0) entriesToRemove.add(entry.getKey());
		}

		entriesToRemove.forEach(entry -> OMNIQUEUE.remove(entry));
	}

	public static class DeferringQueue<K> {
		private List<K> deferredEntries;
		private boolean active;
		private final Registry<K> registry;

		public DeferringQueue(Registry<K> registry) {
			this.deferredEntries = new ArrayList<>();
			this.active = false;
			this.registry = registry;
		}

		public boolean deferEntry(K entry) {
			if (registry.getKey(entry).isEmpty()) {
				this.deferredEntries.add(entry);
				this.activate();

				return true;
			}

			return false;
		}

		public void activate() {
			if (!this.active) {
				if (CRASH_ON_DEFERRING_ENTRY.toBooleanOrElse(QuiltLoader.isDevelopmentEnvironment())) {
					if (CRASH_ON_DEFERRING_ENTRY == TriState.DEFAULT) {
						LOGGER.error("An unregistered entry on the " + this.registry + " registry was attempted to be registered in a content registry through the Quilted Fabric API bridge! "
								+ "Due to this happening on a dev environment, the game will proceed to crash now. "
								+ "This crash may be disabled by setting the \"quilted_fabric_api.quilted_fabric_content_registries_v0.crash_on_deferring_entry\" property. to false!");
					} else {
						LOGGER.error("An unregistered entry on the " + this.registry + " registry was attempted to be registered in a content registry through the Quilted Fabric API bridge! "
								+ "Due to a debug option being enabled, the game will proceed to crash now.");
					}

					throw new DisabledDeferringQueueException();
				} else {
					LOGGER.warn("An unregistered entry on the " + this.registry + " registry was attempted to be registered in a content registry through the Quilted Fabric API bridge! "
							+ "Its instability will be circumvented through the activation of the registry's deferring queue, which may affect performance!");
				}

				this.active = true;
				this.createEvent();
			}
		}

		private void createEvent() {
			RegistryMonitor.create(this.registry).forUpcoming(entryAdded -> {
				var entriesToRemove = new ArrayList<>();

				for (K entry : this.deferredEntries) {
					if (entryAdded.id().equals(this.registry.getId(entry))) {
						entriesToRemove.add(entry);
					}
				}

				this.deferredEntries.removeAll(entriesToRemove);

				if (entriesToRemove.size() != 0) {
					updateOmniqueue();
				}
			});
		}

		public boolean isDeferred(K entry) {
			return this.deferredEntries.contains(entry);
		}
	}

	public static class DisabledDeferringQueueException extends RuntimeException {
		public DisabledDeferringQueueException() {
			super();
		}
	}
}
