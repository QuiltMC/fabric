package net.fabricmc.fabric.impl.content.registry.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
			// a
			if (OMNIQUEUE.containsKey(queue)) {
				OMNIQUEUE.get(queue).add(new RegistryEntryAttachment.Entry<>(entry, value));
			} else {
				OMNIQUEUE.put((RegistryEntryAttachment<Object, Object>) queue, new ArrayList<>());
				OMNIQUEUE.get(queue).add(new RegistryEntryAttachment.Entry<>(entry, value));
			}
		} else {
			queue.put(entry, value);
		}
	}

	public static <V> boolean deferEntry(V value) {
		if (value instanceof Block block) {
			QuiltDeferringQueues.BLOCK.deferEntry(block);
		} else if (value instanceof BlockState state) {
			QuiltDeferringQueues.BLOCK.deferEntry(state.getBlock());
		} else if (value instanceof ReversibleBlockEntry entry) {
			return QuiltDeferringQueues.BLOCK.deferEntry(entry.block());
		} else if (value instanceof Item item) {
			QuiltDeferringQueues.ITEM.deferEntry(item);
		} else if (value instanceof GameEvent event) {
			QuiltDeferringQueues.GAME_EVENT.deferEntry(event);
		} else {
			return false;
		}

		return true;
	}

	public static <V> boolean isEntryDeferred(V value) {
		if (value instanceof Block block) {
			return QuiltDeferringQueues.BLOCK.deferredEntries.contains(block);
		} else if (value instanceof BlockState state) {
			return QuiltDeferringQueues.BLOCK.deferredEntries.contains(state.getBlock());
		} else if (value instanceof ReversibleBlockEntry entry) {
			return QuiltDeferringQueues.BLOCK.deferredEntries.contains(entry.block());
		} else if (value instanceof Item item) {
			return QuiltDeferringQueues.ITEM.deferredEntries.contains(item);
		} else if (value instanceof GameEvent event) {
			return QuiltDeferringQueues.GAME_EVENT.deferredEntries.contains(event);
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
		public List<K> deferredEntries;
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
				this.active = true;
				this.createEvent();
			}
		}

		private void createEvent() {
			LOGGER.info("Event created for " + registry.toString());
			RegistryMonitor.create(this.registry).forUpcoming(entryAdded -> {
				var entriesToRemove = new ArrayList<>();
				for (K entry : this.deferredEntries) {
					LOGGER.warn("a: " + entryAdded.id());
					LOGGER.warn("b: " + this.registry.getId(entry));
					if (entryAdded.id().equals(this.registry.getId(entry))) {
						entriesToRemove.add(entry);
					}
				}
				this.deferredEntries.removeAll(entriesToRemove);
				// TODO - Check if the deferred entries list has changed; Yes, it's very easy to do that, but I actually need to debug stuff before doing that
				updateOmniqueue();
			});
		}

		public boolean isActive() {
			return active;
		}
	}
}
