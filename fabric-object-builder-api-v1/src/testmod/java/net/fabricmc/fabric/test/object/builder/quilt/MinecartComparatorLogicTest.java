/*
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

package net.fabricmc.fabric.test.object.builder.quilt;

import net.minecraft.entity.EntityType;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.MinecartComparatorLogicRegistry;

public class MinecartComparatorLogicTest implements ModInitializer {
	@Override
	public void onInitialize() {
		MinecartComparatorLogicRegistry.register(EntityType.FURNACE_MINECART, (minecart, state, pos) -> 6);
		// Attempt to override the value provided by QSL's testmod. This shouldn't work at all!
		MinecartComparatorLogicRegistry.register(EntityType.TNT_MINECART, (minecart, state, pos) -> 2);
	}
}
