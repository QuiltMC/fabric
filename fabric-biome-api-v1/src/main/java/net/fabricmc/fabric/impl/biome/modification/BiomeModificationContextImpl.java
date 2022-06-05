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
import java.util.function.BiPredicate;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeAdditionsSound;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.BiomeParticleConfig;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.feature.PlacedFeature;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.mixin.biome.BiomeModificationContextImplAccessor;

@ApiStatus.Internal
public class BiomeModificationContextImpl implements BiomeModificationContext {
	private final org.quiltmc.qsl.worldgen.biome.impl.modification.BiomeModificationContextImpl quiltBiomeModificationContextImpl;
	private final WeatherContext weather;
	private final EffectsContext effects;
	private final GenerationSettingsContextImpl generationSettings;
	private final SpawnSettingsContextImpl spawnSettings;

	public BiomeModificationContextImpl(DynamicRegistryManager registries, RegistryKey<Biome> biomeKey, Biome biome) {
		this.quiltBiomeModificationContextImpl = new org.quiltmc.qsl.worldgen.biome.impl.modification.BiomeModificationContextImpl(registries, biomeKey, biome);
		this.weather = new WeatherContextImpl();
		this.effects = new EffectsContextImpl();
		this.generationSettings = new GenerationSettingsContextImpl();
		this.spawnSettings = new SpawnSettingsContextImpl();
	}

	@Override
	public void setCategory(Biome.Category category) {
		this.quiltBiomeModificationContextImpl.setCategory(category);
	}

	@Override
	public WeatherContext getWeather() {
		return this.weather;
	}

	@Override
	public EffectsContext getEffects() {
		return this.effects;
	}

	@Override
	public GenerationSettingsContext getGenerationSettings() {
		return this.generationSettings;
	}

	@Override
	public SpawnSettingsContext getSpawnSettings() {
		return this.spawnSettings;
	}

	public void freeze() {
		((BiomeModificationContextImplAccessor) this.quiltBiomeModificationContextImpl).invokeFreeze();
	}

	private class WeatherContextImpl implements BiomeModificationContext.WeatherContext {
		@Override
		public void setPrecipitation(Biome.Precipitation precipitation) {
			quiltBiomeModificationContextImpl.getWeather().setPrecipitation(precipitation);
		}

		@Override
		public void setTemperature(float temperature) {
			quiltBiomeModificationContextImpl.getWeather().setTemperature(temperature);
		}

		@Override
		public void setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
			quiltBiomeModificationContextImpl.getWeather().setTemperatureModifier(temperatureModifier);
		}

		@Override
		public void setDownfall(float downfall) {
			quiltBiomeModificationContextImpl.getWeather().setDownfall(downfall);
		}
	}

	private class EffectsContextImpl implements EffectsContext {
		@Override
		public void setFogColor(int color) {
			quiltBiomeModificationContextImpl.getEffects().setFogColor(color);
		}

		@Override
		public void setWaterColor(int color) {
			quiltBiomeModificationContextImpl.getEffects().setWaterColor(color);
		}

		@Override
		public void setWaterFogColor(int color) {
			quiltBiomeModificationContextImpl.getEffects().setWaterFogColor(color);
		}

		@Override
		public void setSkyColor(int color) {
			quiltBiomeModificationContextImpl.getEffects().setSkyColor(color);
		}

		@Override
		public void setFoliageColor(Optional<Integer> color) {
			quiltBiomeModificationContextImpl.getEffects().setFoliageColor(color);
		}

		@Override
		public void setGrassColor(Optional<Integer> color) {
			quiltBiomeModificationContextImpl.getEffects().setGrassColor(color);
		}

		@Override
		public void setGrassColorModifier(@NotNull BiomeEffects.GrassColorModifier colorModifier) {
			quiltBiomeModificationContextImpl.getEffects().setGrassColorModifier(colorModifier);
		}

		@Override
		public void setParticleConfig(Optional<BiomeParticleConfig> particleConfig) {
			quiltBiomeModificationContextImpl.getEffects().setParticleConfig(particleConfig);
		}

		@Override
		public void setAmbientSound(Optional<SoundEvent> sound) {
			quiltBiomeModificationContextImpl.getEffects().setAmbientSound(sound);
		}

		@Override
		public void setMoodSound(Optional<BiomeMoodSound> sound) {
			quiltBiomeModificationContextImpl.getEffects().setMoodSound(sound);
		}

		@Override
		public void setAdditionsSound(Optional<BiomeAdditionsSound> sound) {
			quiltBiomeModificationContextImpl.getEffects().setAdditionsSound(sound);
		}

		@Override
		public void setMusic(Optional<MusicSound> sound) {
			quiltBiomeModificationContextImpl.getEffects().setMusic(sound);
		}
	}

	private class GenerationSettingsContextImpl implements GenerationSettingsContext {
		@Override
		public boolean removeFeature(GenerationStep.Feature step, RegistryKey<PlacedFeature> placedFeatureKey) {
			return quiltBiomeModificationContextImpl.getGenerationSettings().removeFeature(step, placedFeatureKey);
		}

		@Override
		public void addFeature(GenerationStep.Feature step, RegistryKey<PlacedFeature> entry) {
			quiltBiomeModificationContextImpl.getGenerationSettings().addFeature(step, entry);
		}

		@Override
		public void addCarver(GenerationStep.Carver step, RegistryKey<ConfiguredCarver<?>> entry) {
			quiltBiomeModificationContextImpl.getGenerationSettings().addCarver(step, entry);
		}

		@Override
		public boolean removeCarver(GenerationStep.Carver step, RegistryKey<ConfiguredCarver<?>> configuredCarverKey) {
			return quiltBiomeModificationContextImpl.getGenerationSettings().removeCarver(step, configuredCarverKey);
		}
	}

	private class SpawnSettingsContextImpl implements SpawnSettingsContext {
		@Override
		public void setCreatureSpawnProbability(float probability) {
			quiltBiomeModificationContextImpl.getSpawnSettings().setCreatureSpawnProbability(probability);
		}

		@Override
		public void addSpawn(SpawnGroup spawnGroup, SpawnSettings.SpawnEntry spawnEntry) {
			quiltBiomeModificationContextImpl.getSpawnSettings().addSpawn(spawnGroup, spawnEntry);
		}

		@Override
		public boolean removeSpawns(BiPredicate<SpawnGroup, SpawnSettings.SpawnEntry> predicate) {
			return quiltBiomeModificationContextImpl.getSpawnSettings().removeSpawns(predicate);
		}

		@Override
		public void setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit) {
			quiltBiomeModificationContextImpl.getSpawnSettings().setSpawnCost(entityType, mass, gravityLimit);
		}

		@Override
		public void clearSpawnCost(EntityType<?> entityType) {
			quiltBiomeModificationContextImpl.getSpawnSettings().clearSpawnCost(entityType);
		}
	}
}
