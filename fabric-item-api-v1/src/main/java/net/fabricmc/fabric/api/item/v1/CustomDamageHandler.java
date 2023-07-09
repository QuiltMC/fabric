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

package net.fabricmc.fabric.api.item.v1;

import java.util.function.Consumer;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

/**
 * Allows an item to run custom logic when {@link ItemStack#damage(int, LivingEntity, Consumer)} is called.
 * This is useful for items that, for example, may drain durability from some other source before damaging
 * the stack itself.
 *
 * <p>Custom damage handlers can be set with {@link FabricItemSettings#customDamage}.
 *
 * @deprecated Use Quilt Item Setting API's {@link org.quiltmc.qsl.item.setting.api.CustomDamageHandler} instead.
 */
@Deprecated
@FunctionalInterface
public interface CustomDamageHandler extends org.quiltmc.qsl.item.setting.api.CustomDamageHandler {
}
