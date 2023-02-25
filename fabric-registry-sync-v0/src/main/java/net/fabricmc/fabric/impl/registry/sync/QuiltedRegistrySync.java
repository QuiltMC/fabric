/*
 * Copyright 2022-2023 QuiltMC
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

package net.fabricmc.fabric.impl.registry.sync;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;
import org.quiltmc.loader.api.minecraft.MinecraftQuiltLoader;
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents;
import org.quiltmc.qsl.registry.impl.sync.SynchronizedRegistry;
import org.slf4j.Logger;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.mixin.registry.sync.RegistriesAccessor;

@SuppressWarnings("UnstableApiUsage")
public class QuiltedRegistrySync implements ServerLifecycleEvents.Starting, ServerLifecycleEvents.Stopped {
	private static final Logger LOGGER = LogUtils.getLogger();
	@SuppressWarnings("unchecked")
	private static final Identifier STATUS_EFFECTS_REGISTRY_ID = ((Registry<Registry<?>>) Registries.REGISTRIES).getId(Registries.STATUS_EFFECT);

	/**
	 * This patch allows to save to disk the registry map of the status effect registry specifically as it's the only
	 * one that has values serialized to disk with raw identifiers.
	 *
	 * @return the identifier map in Fabric's format
	 */
	public static @Nullable Map<Identifier, Object2IntMap<Identifier>> createStatusEffectsRegistryMap() {
		var syncedRegistry = getStatusEffectSynchronizedRegistryIfModded();

		if (syncedRegistry != null) {
			if (syncedRegistry.quilt$getContentStatus() == SynchronizedRegistry.Status.VANILLA) {
				return null;
			}

			var quiltMap = syncedRegistry.quilt$getSyncMap();
			var fabricMap = new Object2ObjectOpenHashMap<Identifier, Object2IntMap<Identifier>>();
			var effectsMap = new Object2IntOpenHashMap<Identifier>();
			fabricMap.put(STATUS_EFFECTS_REGISTRY_ID, effectsMap);

			for (var entry : quiltMap.entrySet()) {
				String namespace = entry.getKey();

				for (var syncEntry : entry.getValue()) {
					var id = new Identifier(namespace, syncEntry.path());
					effectsMap.put(id, syncEntry.rawId());
				}
			}

			return fabricMap;
		}

		return null;
	}

	public static void applyStatusEffectsRegistryMap(Map<Identifier, Object2IntMap<Identifier>> fabricMap) {
		var syncedRegistry = getStatusEffectSynchronizedRegistryIfModded();
		var effectsMap = fabricMap.get(STATUS_EFFECTS_REGISTRY_ID);

		if (syncedRegistry == null) return;

		if (effectsMap == null || effectsMap.isEmpty()) return;

		var quiltMap = new Object2ObjectOpenHashMap<String, Collection<SynchronizedRegistry.SyncEntry>>();

		for (var entry : effectsMap.object2IntEntrySet()) {
			Identifier id = entry.getKey();

			var entries = quiltMap.computeIfAbsent(id.getNamespace(), namespace -> new ArrayList<>());
			entries.add(new SynchronizedRegistry.SyncEntry(id.getPath(), entry.getIntValue(), (byte) 0));
		}

		syncedRegistry.quilt$applySyncMap(quiltMap);
	}

	@Override
	public void startingServer(MinecraftServer server) {
		if (MinecraftQuiltLoader.getEnvironmentType() == EnvType.SERVER) {
			// Freeze the registries on the server
			// FIXME - This port was powered by midnight Ennui, double-check this later!
			LOGGER.debug("Freezing registries");
			// Registries.bootstrap();
			// Quilt injects its init point at that method; We avoid its usage by doing this:
			// Registries.init();
			RegistriesAccessor.invokeFreezeRegistries();
			RegistriesAccessor.invokeValidate(Registries.REGISTRIES);
		}
	}

	@Override
	public void exitServer(MinecraftServer server) {
		var syncedRegistry = getStatusEffectSynchronizedRegistryIfModded();

		if (syncedRegistry != null) {
			syncedRegistry.quilt$restoreIdSnapshot();
		}
	}

	@SuppressWarnings("unchecked")
	public static SynchronizedRegistry<StatusEffect> getStatusEffectSynchronizedRegistry() {
		var statusEffectRegistry = Registries.STATUS_EFFECT;

		if (statusEffectRegistry instanceof SynchronizedRegistry<?>) {
			return (SynchronizedRegistry<StatusEffect>) statusEffectRegistry;
		}

		return null;
	}

	private static SynchronizedRegistry<StatusEffect> getStatusEffectSynchronizedRegistryIfModded() {
		var syncedRegistry = getStatusEffectSynchronizedRegistry();

		if (syncedRegistry != null && syncedRegistry.quilt$getContentStatus() != SynchronizedRegistry.Status.VANILLA) {
			return syncedRegistry;
		}

		return null;
	}
}
