/*
 * Copyright 2016, 2017, 2018, 2019 FabricMC
 * Copyright 2022-2023 QuiltMC
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

package net.fabricmc.fabric.api.object.builder.v1.block;

import org.quiltmc.qsl.block.extensions.api.QuiltMaterialBuilder;

import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.DyeColor;

/**
 * @deprecated Use Quilt Block Extensions API's {@link org.quiltmc.qsl.block.extensions.api.QuiltMaterialBuilder} instead.
 */
@Deprecated
public class FabricMaterialBuilder extends QuiltMaterialBuilder {
	public FabricMaterialBuilder(MapColor color) {
		super(color);
	}

	public FabricMaterialBuilder(DyeColor color) {
		super(color.getMapColor());
	}

	@Override
	public FabricMaterialBuilder burnable() {
		super.burnable();
		return this;
	}

	public FabricMaterialBuilder pistonBehavior(PistonBehavior behavior) {
		super.pistonBehavior(behavior);
		return this;
	}

	public FabricMaterialBuilder lightPassesThrough() {
		super.lightPassesThrough();
		return this;
	}

	@Override
	public FabricMaterialBuilder destroyedByPiston() {
		super.destroyedByPiston();
		return this;
	}

	@Override
	public FabricMaterialBuilder blocksPistons() {
		super.blocksPistons();
		return this;
	}

	@Override
	public FabricMaterialBuilder allowsMovement() {
		super.allowsMovement();
		return this;
	}

	@Override
	public FabricMaterialBuilder liquid() {
		super.liquid();
		return this;
	}

	@Override
	public FabricMaterialBuilder notSolid() {
		super.notSolid();
		return this;
	}

	@Override
	public FabricMaterialBuilder replaceable() {
		super.replaceable();
		return this;
	}
}
