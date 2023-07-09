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

package net.fabricmc.fabric.mixin.content.registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.impl.content.registry.FireBlockHooks;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin implements FireBlockHooks {
	@Shadow
	abstract int getSpreadChance(BlockState block_1);

	@Shadow
	abstract int getBurnChance(BlockState block_1);

	@Override
	public FlammableBlockRegistry.Entry fabric_getVanillaEntry(BlockState block) {
		return new FlammableBlockRegistry.Entry(getBurnChance(block), getSpreadChance(block));
	}
}
