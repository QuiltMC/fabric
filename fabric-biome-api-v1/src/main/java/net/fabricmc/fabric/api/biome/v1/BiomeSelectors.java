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

package net.fabricmc.fabric.api.biome.v1;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

/**
 * Provides several convenient biome selectors that can be used with {@link BiomeModifications}.
 *
 * <p><b>Experimental feature</b>, may be removed or changed without further notice.
 */
@Deprecated
public final class BiomeSelectors {
	private BiomeSelectors() {
	}

	/**
	 * Matches all Biomes. Use a more specific selector if possible.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> all() {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.all().test(context);
	}

	/**
	 * Matches Biomes that have not been originally defined in a datapack, but that are defined in code.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> builtIn() {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.builtIn().test(context);
	}

	/**
	 * Returns a biome selector that will match all biomes from the minecraft namespace.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> vanilla() {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.foundInTheEnd().test(context);
	}

	/**
	 * Returns a biome selector that will match all biomes that would normally spawn in the Overworld,
	 * assuming Vanilla's default biome source is used.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> foundInOverworld() {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.foundInOverworld().test(context);
	}

	/**
	 * Returns a biome selector that will match all biomes that would normally spawn in the Nether,
	 * assuming Vanilla's default multi noise biome source with the nether preset is used.
	 *
	 * <p>This selector will also match modded biomes that have been added to the nether using {@link NetherBiomes}.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> foundInTheNether() {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.foundInTheNether().test(context);
	}

	/**
	 * Returns a biome selector that will match all biomes that would normally spawn in the End,
	 * assuming Vanilla's default End biome source is used.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> foundInTheEnd() {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.foundInTheEnd().test(context);
	}

	/**
	 * Returns a biome selector that will match all biomes in the given tag.
	 *
	 * @see net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags
	 * @see net.fabricmc.fabric.impl.tag.convention.TagRegistration
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> tag(TagKey<Biome> tag) {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.isIn(tag).test(context);
	}

	/**
	 * @see #excludeByKey(Collection)
	 */
	@SafeVarargs
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> excludeByKey(RegistryKey<Biome>... keys) {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.excludeByKey(keys).test(context);
	}

	/**
	 * Returns a selector that will reject any biome whose keys are in the given collection of keys.
	 *
	 * <p>This is useful for allowing a list of biomes to be defined in the config file, where
	 * a certain feature should not spawn.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> excludeByKey(Collection<RegistryKey<Biome>> keys) {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.excludeByKey(keys).test(context);
	}

	/**
	 * @see #includeByKey(Collection)
	 */
	@SafeVarargs
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> includeByKey(RegistryKey<Biome>... keys) {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.includeByKey(keys).test(context);
	}

	/**
	 * Returns a selector that will accept only biomes whos keys are in the given collection of keys.
	 *
	 * <p>This is useful for allowing a list of biomes to be defined in the config file, where
	 * a certain feature should spawn exclusively.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> includeByKey(Collection<RegistryKey<Biome>> keys) {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.includeByKey(keys).test(context);
	}

	/**
	 * Returns a biome selector that will match biomes in which one of the given entity types can spawn.
	 *
	 * <p>Matches spawns in all {@link SpawnGroup spawn groups}.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> spawnsOneOf(EntityType<?>... entityTypes) {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.spawnsOneOf(entityTypes).test(context);
	}

	/**
	 * Returns a biome selector that will match biomes in which one of the given entity types can spawn.
	 *
	 * <p>Matches spawns in all {@link SpawnGroup spawn groups}.
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> spawnsOneOf(Set<EntityType<?>> entityTypes) {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.spawnsOneOf(entityTypes).test(context);
	}

	/**
	 * Matches Biomes that have one of the given categories.
	 *
	 * @see Biome#getCategory()
	 */
	public static <T extends org.quiltmc.qsl.worldgen.biome.api.BiomeSelectionContext> Predicate<T> categories(Biome.Category... categories) {
		return context -> org.quiltmc.qsl.worldgen.biome.api.BiomeSelectors.categories(categories).test(context);
	}
}
