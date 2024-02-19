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

package net.fabricmc.fabric.api.client.item.v1;

import java.util.List;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.base.event.QuiltCompatEvent;

/**
 * @deprecated Use Quilt Tooltip API's {@link org.quiltmc.qsl.tooltip.api.client.ItemTooltipCallback} instead.
 */
@Deprecated
@Environment(EnvType.CLIENT)
public interface ItemTooltipCallback {
	/**
	 * Fired after the game has appended all base tooltip lines to the list.
	 */
	Event<ItemTooltipCallback> EVENT = QuiltCompatEvent.fromQuilt(org.quiltmc.qsl.tooltip.api.client.ItemTooltipCallback.EVENT,
			itemTooltipCallback -> (stack, player, context, lines) -> itemTooltipCallback.getTooltip(stack, context, lines),
			invokerGetter -> (stack, context, lines) -> invokerGetter.get().onTooltipRequest(stack, MinecraftClient.getInstance().player, context, lines)
	);

	/**
	 * Called when an item stack's tooltip is rendered. Text added to {@code lines} will be
	 * rendered with the tooltip.
	 *
	 * @param lines the list containing the lines of text displayed on the stack's tooltip
	 */
	void getTooltip(ItemStack stack, TooltipContext context, List<Text> lines);
}
