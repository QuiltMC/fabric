/*
 * Copyright 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.event.registry;

import net.minecraft.util.registry.DynamicRegistryManager;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.event.QuiltCompatEvent;

/**
 * This event gets triggered when a new {@link DynamicRegistryManager} gets created, but before it gets filled.
 * Therefore, this is the ideal place to register callbacks to dynamic registries.
 * For example, the following code is used to register a callback that gets triggered for any registered Biome, both JSON and code defined.
 *
 * <pre>
 * {@code
 * DynamicRegistrySetupCallback.EVENT.register(registryManager -> {
 *     registryManager.getOptional(Registry.BIOME_KEY).ifPresent(biomes -> {
 *         RegistryEntryAddedCallback.event(biomes).register((rawId, id, object) -> {
 *             // Do something
 *         });
 *     });
 * });
 * }
 * </pre>
 *
 * <p><strong>Important Note</strong>: The passed dynamic registry manager might not
 * contain the registry, as this event is invoked for each layer of
 * the combined registry manager, and each layer holds different registries.
 * Use {@link DynamicRegistryManager#getOptional} to prevent crashes.
 *
 * @see net.minecraft.util.registry.ServerDynamicRegistryType
 */
@FunctionalInterface
public interface DynamicRegistrySetupCallback {
	void onRegistrySetup(DynamicRegistryManager registryManager);

	Event<DynamicRegistrySetupCallback> EVENT = QuiltCompatEvent.fromQuilt(
			org.quiltmc.qsl.registry.api.event.RegistryEvents.DYNAMIC_REGISTRY_SETUP,
			onDynamicRegistrySetupCallback -> (resourceManager, registryManager) -> onDynamicRegistrySetupCallback.onRegistrySetup(registryManager),
			// In theory? This lossy conversion should be fine since only Fabric API invokes this event.
			// A fix should be investigated though if shenanigans happen.
			invokerGetter -> registryManager -> invokerGetter.get().onDynamicRegistrySetup(null, registryManager)
	);
}
