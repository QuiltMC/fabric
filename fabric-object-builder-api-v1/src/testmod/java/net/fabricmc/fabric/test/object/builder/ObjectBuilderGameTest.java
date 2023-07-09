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

package net.fabricmc.fabric.test.object.builder;

import java.util.List;

import org.quiltmc.qsl.testing.api.game.QuiltGameTest;
import org.quiltmc.qsl.testing.api.game.QuiltTestContext;

import net.minecraft.block.Block;
import net.minecraft.test.GameTest;
import net.minecraft.util.math.BlockPos;

public class ObjectBuilderGameTest {
	@GameTest(templateName = QuiltGameTest.EMPTY_STRUCTURE)
	public void testBlockUse(QuiltTestContext context) {
		List<Block> blocks = List.of(BlockEntityTypeBuilderTest.INITIAL_BETRAYAL_BLOCK, BlockEntityTypeBuilderTest.ADDED_BETRAYAL_BLOCK, BlockEntityTypeBuilderTest.FIRST_MULTI_BETRAYAL_BLOCK, BlockEntityTypeBuilderTest.SECOND_MULTI_BETRAYAL_BLOCK);
		BlockPos.Mutable pos = BlockPos.ORIGIN.mutableCopy();

		for (Block block : blocks) {
			context.setBlockState(pos, block);
			context.useBlock(pos);
			pos.up();
		}

		context.complete();
	}
}
