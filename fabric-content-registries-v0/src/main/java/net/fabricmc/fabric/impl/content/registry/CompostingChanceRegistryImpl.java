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

import org.quiltmc.qsl.item.content.registry.api.ItemContentRegistries;

import net.minecraft.tag.TagKey;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.impl.content.registry.util.QuiltDeferringQueues;

public class CompostingChanceRegistryImpl implements CompostingChanceRegistry {
	@Override
	public Float get(ItemConvertible item) {
		return ItemContentRegistries.COMPOST_CHANCE.get(item.asItem()).orElse(0.0F);
	}

	@Override
	public void add(ItemConvertible item, Float value) {
		QuiltDeferringQueues.addEntry(ItemContentRegistries.COMPOST_CHANCE, item.asItem(), value);
	}

	/**
	 * @throws UnsupportedOperationException if ran on Fabric API, which currently doesn't implement this method
	 */
	@Override
	public void add(TagKey<Item> tag, Float value) throws UnsupportedOperationException {
		ItemContentRegistries.COMPOST_CHANCE.put(tag, value);
	}

	@Override
	public void remove(ItemConvertible item) {
		ItemContentRegistries.COMPOST_CHANCE.remove(item.asItem());
	}

	/**
	 * @throws UnsupportedOperationException if ran on Fabric API, which currently doesn't implement this method
	 */
	@Override
	public void remove(TagKey<Item> tag) {
		ItemContentRegistries.COMPOST_CHANCE.remove(tag);
	}

	@Override
	public void clear(ItemConvertible item) {
		throw new UnsupportedOperationException("CompostingChanceRegistry operates directly on the vanilla map - clearing not supported!");
	}

	@Override
	public void clear(TagKey<Item> tag) {
		throw new UnsupportedOperationException("CompostingChanceRegistry operates directly on the vanilla map - clearing not supported!");
	}
}
