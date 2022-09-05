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

package net.fabricmc.fabric.impl.content.registry;

import java.util.IdentityHashMap;
import java.util.Map;

import org.quiltmc.qsl.item.content.registry.api.ItemContentRegistries;
import org.quiltmc.quilted_fabric_api.impl.content.registry.util.QuiltDeferringQueues;

import net.minecraft.tag.TagKey;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;

import net.fabricmc.fabric.api.registry.FuelRegistry;

// Fabric To-Do: Clamp values to 32767 (+ add hook for mods which extend the limit to disable the check?)
// About that, Quilt's equivalent API supports integer values properly, no need for that
public final class FuelRegistryImpl implements FuelRegistry {
	//private static final Logger LOGGER = LoggerFactory.getLogger(FuelRegistryImpl.class);

	public FuelRegistryImpl() { }

	public Map<Item, Integer> getFuelTimes() {
		Map<Item, Integer> ret = new IdentityHashMap<>();

		ItemContentRegistries.FUEL_TIME.entryIterator().forEachRemaining(entry -> ret.put(entry.entry(), entry.value()));

		return ret;
	}

	@Override
	public Integer get(ItemConvertible item) {
		return ItemContentRegistries.FUEL_TIME.getNullable(item.asItem());
	}

	@Override
	public void add(ItemConvertible item, Integer cookTime) {
		QuiltDeferringQueues.addEntry(ItemContentRegistries.FUEL_TIME, item.asItem(), cookTime.intValue());
	}

	@Override
	public void add(TagKey<Item> tag, Integer cookTime) {
		ItemContentRegistries.FUEL_TIME.put(tag, cookTime.intValue());
	}

	@Override
	public void remove(ItemConvertible item) {
		ItemContentRegistries.FUEL_TIME.put(item.asItem(), 0);
	}

	@Override
	public void remove(TagKey<Item> tag) {
		ItemContentRegistries.FUEL_TIME.put(tag, 0);
	}

	@Override
	public void clear(ItemConvertible item) {
		ItemContentRegistries.FUEL_TIME.remove(item.asItem());
	}

	@Override
	public void clear(TagKey<Item> tag) {
		ItemContentRegistries.FUEL_TIME.remove(tag);
	}

	public void apply(Map<Item, Integer> map) { }

	public void resetCache() { }
}
