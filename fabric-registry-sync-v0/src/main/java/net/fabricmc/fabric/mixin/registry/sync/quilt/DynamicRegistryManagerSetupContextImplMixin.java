/*
 * Copyright 2016, 2017, 2018, 2019 FabricMC
 * Copyright 2022 The Quilt Project
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

package net.fabricmc.fabric.mixin.registry.sync.quilt;

import java.util.Map;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.quiltmc.qsl.registry.api.event.DynamicRegistryManagerSetupContext;
import org.quiltmc.qsl.registry.api.event.RegistryEntryContext;
import org.quiltmc.qsl.registry.api.event.RegistryEvents;
import org.quiltmc.qsl.registry.impl.DynamicRegistryManagerSetupContextImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.MutableRegistry;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;

import net.fabricmc.fabric.api.event.registry.DynamicRegistryView;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.fabric.api.event.registry.RegistryEntryRemovedCallback;

@Mixin(DynamicRegistryManagerSetupContextImpl.class)
public abstract class DynamicRegistryManagerSetupContextImplMixin implements DynamicRegistryManagerSetupContext, DynamicRegistryManager, DynamicRegistryView {
	@Shadow(remap = false)
	@Final
	private Map<RegistryKey<?>, MutableRegistry<?>> registries;

	@Shadow
	@Override
	public abstract @NotNull DynamicRegistryManager registryManager();

	@Shadow
	@Override
	public abstract Stream<Entry<?>> streamAllRegistries();

	@Override
	public DynamicRegistryManager asDynamicRegistryManager() {
		return this.registryManager();
	}

	@Override
	public Stream<Registry<?>> stream() {
		return this.registries.values().stream().map(registry -> (Registry<?>) registry);
	}

	// getOptional is implemented at workaround.DynamicRegistryManagerSetupContextImplMixin

	@Override
	public <T> void registerEntryAdded(RegistryKey<? extends Registry<? extends T>> registryRef, RegistryEntryAddedCallback<T> callback) {
		var newCallback = new RegistryEvents.EntryAdded<T>() {
			@Override
			public void onAdded(RegistryEntryContext<T> context) {
				callback.onEntryAdded(context.rawId(), context.id(), context.value());
			}
		};

		this.monitor((RegistryKey<? extends Registry<T>>) (Object) registryRef, monitor -> monitor.forAll(newCallback));
	}

	@Override
	public <T> void registerEntryRemoved(RegistryKey<? extends Registry<? extends T>> registryRef, RegistryEntryRemovedCallback<T> callback) {
		Registry<T> registry = (Registry<T>) this.registries.get(registryRef);

		if (registry != null) {
			RegistryEntryRemovedCallback.event(registry).register(callback);
		}
	}
}
