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

package net.fabricmc.fabric.api.registry;

import java.util.Objects;

import org.slf4j.LoggerFactory;
import org.quiltmc.qsl.block.content.registry.api.BlockContentRegistries;
import org.slf4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

import net.fabricmc.fabric.impl.content.registry.util.QuiltDeferringQueues;
import net.fabricmc.fabric.impl.content.registry.util.QuiltDeferringQueues.DeferringQueue;

/**
 * A registry for shovel flattening interactions. A vanilla example is turning dirt to dirt paths.
 */
@Deprecated
public final class FlattenableBlockRegistry {
	private static final DeferringQueue<Block, BlockState> QUEUE = QuiltDeferringQueues.BLOCK.register(BlockContentRegistries.FLATTENABLE_BLOCK);
	private static final Logger LOGGER = LoggerFactory.getLogger(FlattenableBlockRegistry.class);

	private FlattenableBlockRegistry() {
	}

	/**
	 * Registers a flattening interaction.
	 *
	 * @param input     the input block that can be flattened
	 * @param flattened the flattened result block state
	 */
	public static void register(Block input, BlockState flattened) {
		Objects.requireNonNull(input, "input block cannot be null");
		Objects.requireNonNull(flattened, "flattened block state cannot be null");

		BlockContentRegistries.FLATTENABLE_BLOCK.get(input).ifPresent(old -> {
			LOGGER.debug("Replaced old flattening mapping from {} to {} with {}", input, old, flattened);
		});

		QuiltDeferringQueues.BLOCK.addEntry(QUEUE, input, flattened);
	}
}
