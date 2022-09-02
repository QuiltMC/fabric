package net.fabricmc.fabric.impl.content.registry.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quiltmc.loader.api.QuiltLoader;
import org.quiltmc.qsl.base.api.util.TriState;
import org.quiltmc.qsl.registry.api.event.RegistryMonitor;
import org.quiltmc.qsl.registry.attachment.api.RegistryEntryAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.event.GameEvent;

public class QuiltDeferringQueues<T> {
	private static final TriState CRASH_ON_DEFERRING_ENTRY = TriState.fromProperty("quilted_fabric_api.quilted_fabric_content_registries_v0.crash_on_deferring_entry");
	private static final Logger LOGGER = LoggerFactory.getLogger("Quilted Fabric Content Registries");

	private final List<DeferringQueue<T, ?>> queues = new ArrayList<>();
	private boolean hasEvent = false;

	public static final QuiltDeferringQueues<Block> BLOCK = new QuiltDeferringQueues<>();
	public static final QuiltDeferringQueues<Item> ITEM = new QuiltDeferringQueues<>();
	public static final QuiltDeferringQueues<GameEvent> GAME_EVENT = new QuiltDeferringQueues<>();

	public QuiltDeferringQueues() {
		this.hasEvent = false;
	}

	public <V> DeferringQueue<T, V> register(RegistryEntryAttachment<T, V> registryAttachment) {
		var queue = new DeferringQueue<>(registryAttachment, new HashMap<>());

		queues.add(queue);

		return queue;
	}

	public <V> void addEntry(DeferringQueue<T, V> queue, T entry, V value) {
		if (queue.registryAttachment().registry().getKey(entry).isEmpty()) {
			LOGGER.warn("There was an attempt to add an unregistered item to a content registry! The registration will be deferred to a later point.");
			queue.deferredEntries().put(entry, value);

			if (!hasEvent) {
				if (CRASH_ON_DEFERRING_ENTRY.toBooleanOrElse(QuiltLoader.isDevelopmentEnvironment())) {
					if (CRASH_ON_DEFERRING_ENTRY == TriState.DEFAULT) {
						LOGGER.warn("A mod has attempted to put an unregistered entry to a Fabric API content registry (bridged to the "
								+ queue.registryAttachment().id() + " registry attachment by QFAPI)! The game will proceed to crash."
								+ "This debugging behavior may be disabled with the \"quilted_fabric_api.quilted_fabric_content_registries_v0.crash_on_deferring_entry\" property "
								+ "set to false.");
					} else {
						LOGGER.warn("A mod has attempted to put an unregistered entry to a Fabric API content registry (bridged to the "
								+ queue.registryAttachment().id() + " registry attachment by QFAPI)! The game will proceed to crash "
								+ "due to a debug property being enabled.");
					}

					throw new UnsupportedOperationException();
				} else {
					LOGGER.warn("A mod has attempted to put an unregistered entry to a Fabric API content registry (bridged to the "
							+ queue.registryAttachment().id() + " registry attachment by QFAPI)! The " + queue.registryAttachment().registry().toString()
							+ " registry's deferring queue has been activated in order to re-route its addition to post-registration.");
					this.createEvent(queue.registryAttachment().registry());
					this.hasEvent = true;
				}
			}
		} else {
			queue.registryAttachment().put(entry, value);
		}
	}

	@SuppressWarnings("unchecked")
	public <V> void createEvent(Registry<T> registry) {
		RegistryMonitor.create(registry).forUpcoming(entryAdded -> {
			var queuesToRemove = new ArrayList<DeferringQueue<?, ?>>();

			for (DeferringQueue<?, ?> queue : queues) {
				var entriesToRemove = new ArrayList<>();

				for (Entry<?, ?> entry: queue.deferredEntries().entrySet()) {
					var castEntry = (Entry<T, V>) entry;

					if (registry.getId((castEntry.getKey())).equals(entryAdded.id())) {
						((RegistryEntryAttachment<T, V>) queue.registryAttachment()).put(castEntry.getKey(), castEntry.getValue());
						entriesToRemove.add(entry.getKey());
						LOGGER.warn("Registered the deferred entry " + entryAdded.id() + " to the Fabric API content registry bridged to QSL's "
								+ queue.registryAttachment().id() + " registry attachment.");
					}
				}

				entriesToRemove.forEach(entry -> queue.deferredEntries().remove(entry));

				if (queue.deferredEntries().size() == 0) {
					queuesToRemove.add(queue);
				}
			}

			queuesToRemove.forEach(entry -> queues.remove(entry));
		});
	}

	public record DeferringQueue<T, V>(
			RegistryEntryAttachment<T, V> registryAttachment,
			Map<T, V> deferredEntries
	) { }
}
