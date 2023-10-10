/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 * Copyright 2023 The Quilt Project
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

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.google.common.collect.Iterables;
import com.mojang.serialization.Codec;
import org.jetbrains.annotations.Unmodifiable;
import org.quiltmc.qsl.registry.api.dynamic.DynamicMetaRegistry;
import org.quiltmc.qsl.registry.api.dynamic.DynamicRegistryFlag;

import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryLoader;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public final class DynamicRegistriesImpl {
	public static final Set<RegistryKey<? extends Registry<?>>> DYNAMIC_REGISTRY_KEYS = new HashSet<>();

	static {
		for (RegistryLoader.Entry<?> vanillaEntry : RegistryLoader.DYNAMIC_REGISTRIES) {
			DYNAMIC_REGISTRY_KEYS.add(vanillaEntry.key());
		}
	}

	private DynamicRegistriesImpl() {
	}

	public static @Unmodifiable List<RegistryLoader.Entry<?>> getDynamicRegistries() {
		return List.copyOf(RegistryLoader.DYNAMIC_REGISTRIES);
	}

	public static <T> void register(RegistryKey<? extends Registry<T>> key, Codec<T> codec) {
		Objects.requireNonNull(key, "Registry key cannot be null");
		Objects.requireNonNull(codec, "Codec cannot be null");

		if (!DYNAMIC_REGISTRY_KEYS.add(key)) {
			throw new IllegalArgumentException("Dynamic registry " + key + " has already been registered!");
		}

		DynamicMetaRegistry.register(key, codec);
	}

	public static <T> void registerSynced(RegistryKey<? extends Registry<T>> registryKey, Codec<T> dataCodec, Codec<T> networkCodec, DynamicRegistries.SyncOption... options) {
		Objects.requireNonNull(registryKey, "Registry key cannot be null");
		Objects.requireNonNull(networkCodec, "Network codec cannot be null");
		Objects.requireNonNull(options, "Options cannot be null");
		EnumSet<DynamicRegistryFlag> flagSet = EnumSet.noneOf(DynamicRegistryFlag.class);
		Arrays.stream(options).iterator().forEachRemaining(option -> flagSet.add(flagConverter(option)));
		DynamicMetaRegistry.registerSynced(registryKey, dataCodec, networkCodec, Iterables.toArray(flagSet, DynamicRegistryFlag.class));
	}

	/**
	 * Not inlined for code readability. Will need to contain a switch statement once the number of SyncOptions is greater than 2.
	 */
	private static DynamicRegistryFlag flagConverter(DynamicRegistries.SyncOption option) {
		if (option.equals(DynamicRegistries.SyncOption.SKIP_WHEN_EMPTY)) {
			return DynamicRegistryFlag.OPTIONAL;
		} else {
			throw new IllegalArgumentException("Invalid SyncOption passed to flag converter! This shouldn't be possible.");
		}
	}
}
