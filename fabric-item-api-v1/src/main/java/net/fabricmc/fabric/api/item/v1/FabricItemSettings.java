/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.api.item.v1;

import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.util.Rarity;

/**
 * Fabric's version of Item.Settings. Adds additional methods and hooks
 * not found in the original class.
 *
 * <p>To use it, simply replace {@code new Item.Settings()} with
 * {@code new FabricItemSettings()}.
 *
 * @deprecated Use Quilt Item Setting API's {@link org.quiltmc.qsl.item.setting.api.QuiltItemSettings} instead.
 */
@Deprecated
public class FabricItemSettings extends QuiltItemSettings {
	/**
	 * Sets the equipment slot provider of the item.
	 *
	 * @param equipmentSlotProvider the equipment slot provider
	 * @return this builder
	 */
	public FabricItemSettings equipmentSlot(EquipmentSlotProvider equipmentSlotProvider) {
		this.equipmentSlot((org.quiltmc.qsl.item.setting.api.EquipmentSlotProvider) equipmentSlotProvider);
		return this;
	}

	/**
	 * Sets the custom damage handler of the item.
	 * Note that this is only called on an ItemStack if {@link ItemStack#isDamageable()} returns true.
	 *
	 * @see CustomDamageHandler
	 */
	public FabricItemSettings customDamage(CustomDamageHandler handler) {
		this.customDamage((org.quiltmc.qsl.item.setting.api.CustomDamageHandler) handler);
		return this;
	}

	// Overrides of vanilla methods

	@Override
	public FabricItemSettings food(FoodComponent foodComponent) {
		super.food(foodComponent);
		return this;
	}

	@Override
	public FabricItemSettings maxCount(int maxCount) {
		super.maxCount(maxCount);
		return this;
	}

	@Override
	public FabricItemSettings maxDamageIfAbsent(int maxDamage) {
		super.maxDamageIfAbsent(maxDamage);
		return this;
	}

	@Override
	public FabricItemSettings maxDamage(int maxDamage) {
		super.maxDamage(maxDamage);
		return this;
	}

	@Override
	public FabricItemSettings recipeRemainder(Item recipeRemainder) {
		super.recipeRemainder(recipeRemainder);
		return this;
	}

	@Override
	public FabricItemSettings rarity(Rarity rarity) {
		super.rarity(rarity);
		return this;
	}

	@Override
	public FabricItemSettings fireproof() {
		super.fireproof();
		return this;
	}

	@Override
	public FabricItemSettings requires(FeatureFlag... features) {
		super.requires(features);
		return this;
	}
}
