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

package net.fabricmc.fabric.impl.biome;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.TheEndBiomeSource;

/**
 * Internal data for modding Vanilla's {@link TheEndBiomeSource}.
 */
@ApiStatus.Internal
public final class TheEndBiomeData {
	private TheEndBiomeData() {
	}

	public static void addEndBiomeReplacement(RegistryKey<Biome> replaced, RegistryKey<Biome> variant, double weight) {
		org.quiltmc.qsl.worldgen.biome.impl.TheEndBiomeData.addEndBiomeReplacement(replaced, variant, weight);
	}

	public static void addEndMidlandsReplacement(RegistryKey<Biome> highlands, RegistryKey<Biome> midlands, double weight) {
		org.quiltmc.qsl.worldgen.biome.impl.TheEndBiomeData.addEndBiomeReplacement(highlands, midlands, weight);
	}

	public static void addEndBarrensReplacement(RegistryKey<Biome> highlands, RegistryKey<Biome> barrens, double weight) {
		org.quiltmc.qsl.worldgen.biome.impl.TheEndBiomeData.addEndBiomeReplacement(highlands, barrens, weight);
	}

	public static org.quiltmc.qsl.worldgen.biome.impl.TheEndBiomeData.Overrides createOverrides(Registry<Biome> biomeRegistry, long seed) {
		return org.quiltmc.qsl.worldgen.biome.impl.TheEndBiomeData.createOverrides(biomeRegistry, seed);
	}

	/**
	 * An instance of this class is attached to each {@link TheEndBiomeSource}.
	 */
	public static class Overrides extends org.quiltmc.qsl.worldgen.biome.impl.TheEndBiomeData.Overrides {
		public Overrides(Registry<Biome> biomeRegistry, long seed) {
			super(biomeRegistry, seed);
		}
	}
}
