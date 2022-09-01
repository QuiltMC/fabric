package net.fabricmc.fabric.impl.content.registry.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quiltmc.qsl.registry.api.event.RegistryMonitor;
import org.quiltmc.qsl.registry.attachment.api.RegistryEntryAttachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

// TODO - Clear this up
public class QuiltDeferringQueues<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger("Quilted Fabric Content Registries");

	private final List<DeferringQueue<T, ?>> queues = new ArrayList<>();
	private boolean hasEvent = false;
	
	public static final QuiltDeferringQueues<Block> BLOCK_DEFERRING_QUEUES = new QuiltDeferringQueues<>();
	public static final QuiltDeferringQueues<Item> ITEM_DEFERRING_QUEUES = new QuiltDeferringQueues<>();

	public QuiltDeferringQueues() {
		this.hasEvent = false;
	}

	public <V> DeferringQueue<T, V> registerQueue(RegistryEntryAttachment<T, V> registryAttachment) {
		var queue = new DeferringQueue<>(registryAttachment, new HashMap<>());

		queues.add(queue);

		return queue;
	}

	public <V> void addEntry(DeferringQueue<T, V> queue, T entry, V value) {
		if (queue.registryAttachment().registry().getKey(entry).isEmpty()) {
			LOGGER.warn("There was an attempt to add an unregistered item to a content registry! The registration will be deferred to a later point.");
			queue.deferredEntries().put(entry, value);

			if (!hasEvent) {
				this.createEvent(queue.registryAttachment().registry());
				this.hasEvent = true;
			}
		} else {
			queue.registryAttachment().put(entry, value);
		}
	}

	public <V> void createEvent(Registry<T> registry) {
		RegistryMonitor.create(registry).forUpcoming(entryAdded -> {
			for (DeferringQueue<?, ?> queue : queues) {
				var entriesToRemove = new ArrayList<>();

				for (Entry<?, ?> entry: queue.deferredEntries().entrySet()) {
					var castEntry = (Entry<T, V>) entry;
					if (registry.getId((castEntry.getKey())).equals(entryAdded.id())) {
						((RegistryEntryAttachment<T, V>) queue.registryAttachment()).put(castEntry.getKey(), castEntry.getValue());
						entriesToRemove.add(entry.getKey());
						LOGGER.warn("Registered the deffered entry " + entryAdded.id() + " to a content registry.");
					}
				}

				entriesToRemove.forEach(entry -> queue.deferredEntries().remove(entry));
			}
		});
	}

	public static record DeferringQueue<T, V>(
		RegistryEntryAttachment<T, V> registryAttachment,
		Map<T, V> deferredEntries
	) {}
}
