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

package net.fabricmc.fabric.impl.biome.modification;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.PlacedFeature;

/**
 * Utility class for getting the registry keys of built-in worldgen objects and throwing proper exceptions if they
 * are not registered.
 */
@ApiStatus.Internal
public final class BuiltInRegistryKeys {
	private BuiltInRegistryKeys() {
	}

	public static RegistryKey<ConfiguredStructureFeature<?, ?>> get(ConfiguredStructureFeature<?, ?> configuredStructure) {
		return org.quiltmc.qsl.worldgen.biome.impl.modification.BuiltInRegistryKeys.get(configuredStructure);
	}

	public static RegistryKey<ConfiguredFeature<?, ?>> get(ConfiguredFeature<?, ?> configuredFeature) {
		return org.quiltmc.qsl.worldgen.biome.impl.modification.BuiltInRegistryKeys.get(configuredFeature);
	}

	public static RegistryKey<PlacedFeature> get(PlacedFeature placedFeature) {
		return org.quiltmc.qsl.worldgen.biome.impl.modification.BuiltInRegistryKeys.get(placedFeature);
	}

	public static RegistryKey<ConfiguredCarver<?>> get(ConfiguredCarver<?> configuredCarver) {
		return org.quiltmc.qsl.worldgen.biome.impl.modification.BuiltInRegistryKeys.get(configuredCarver);
	}
}
