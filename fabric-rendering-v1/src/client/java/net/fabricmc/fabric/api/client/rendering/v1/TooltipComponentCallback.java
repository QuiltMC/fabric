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

package net.fabricmc.fabric.api.client.rendering.v1;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.item.Item;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.base.event.QuiltCompatEvent;

/**
 * Allows registering a mapping from {@link TooltipData} to {@link TooltipComponent}.
 * This allows custom tooltips for items: first, override {@link Item#getTooltipData} and return a custom {@code TooltipData}.
 * Second, register a listener to this event and convert the data to your component implementation if it's an instance of your data class.
 *
 * <p>Note that failure to map some data to a component will throw an exception,
 * so make sure that any data you return in {@link Item#getTooltipData} will be handled by one of the callbacks.
 *
 * @deprecated Use Quilt Tooltip API's {@link org.quiltmc.qsl.tooltip.api.client.TooltipComponentCallback} instead.
 */
@Deprecated
public interface TooltipComponentCallback {
	Event<TooltipComponentCallback> EVENT = QuiltCompatEvent.fromQuilt(org.quiltmc.qsl.tooltip.api.client.TooltipComponentCallback.EVENT,
			tooltipComponentCallback -> tooltipComponentCallback::getComponent,
			invokerGetter -> data -> invokerGetter.get().getComponent(data)
	);

	/**
	 * Return the tooltip component for the passed data, or null if none is available.
	 */
	@Nullable
	TooltipComponent getComponent(TooltipData data);
}
