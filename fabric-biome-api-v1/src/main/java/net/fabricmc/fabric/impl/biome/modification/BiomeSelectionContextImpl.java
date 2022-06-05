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

import java.util.Optional;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.level.LevelProperties;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;

@ApiStatus.Internal
public class BiomeSelectionContextImpl implements BiomeSelectionContext {
	private final org.quiltmc.qsl.worldgen.biome.impl.modification.BiomeSelectionContextImpl quiltBiomeSelectionContextImpl;

	public BiomeSelectionContextImpl(DynamicRegistryManager dynamicRegistries, LevelProperties levelProperties, RegistryKey<Biome> key, Biome biome) {
		quiltBiomeSelectionContextImpl = new org.quiltmc.qsl.worldgen.biome.impl.modification.BiomeSelectionContextImpl(dynamicRegistries, levelProperties, key, biome);
	}

	@Override
	public RegistryKey<Biome> getBiomeKey() {
		return quiltBiomeSelectionContextImpl.getBiomeKey();
	}

	@Override
	public Biome getBiome() {
		return quiltBiomeSelectionContextImpl.getBiome();
	}

	@Override
	public RegistryEntry<Biome> getBiomeRegistryEntry() {
		return quiltBiomeSelectionContextImpl.getBiomeRegistryEntry();
	}

	@Override
	public Optional<RegistryKey<ConfiguredFeature<?, ?>>> getFeatureKey(ConfiguredFeature<?, ?> configuredFeature) {
		return quiltBiomeSelectionContextImpl.getFeatureKey(configuredFeature);
	}

	@Override
	public Optional<RegistryKey<PlacedFeature>> getPlacedFeatureKey(PlacedFeature placedFeature) {
		return quiltBiomeSelectionContextImpl.getPlacedFeatureKey(placedFeature);
	}

	@Override
	public boolean validForStructure(RegistryKey<ConfiguredStructureFeature<?, ?>> key) {
		return quiltBiomeSelectionContextImpl.validForStructure(key);
	}

	@Override
	public Optional<RegistryKey<ConfiguredStructureFeature<?, ?>>> getStructureKey(ConfiguredStructureFeature<?, ?> configuredStructure) {
		return quiltBiomeSelectionContextImpl.getStructureKey(configuredStructure);
	}

	@Override
	public boolean canGenerateIn(RegistryKey<DimensionOptions> dimensionKey) {
		return quiltBiomeSelectionContextImpl.canGenerateIn(dimensionKey);
	}

	@Override
	public boolean isIn(TagKey<Biome> tag) {
		return quiltBiomeSelectionContextImpl.isIn(tag);
	}
}
