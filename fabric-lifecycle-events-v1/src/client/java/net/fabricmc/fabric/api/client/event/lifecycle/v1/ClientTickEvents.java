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

package net.fabricmc.fabric.api.client.event.lifecycle.v1;

import org.quiltmc.qsl.lifecycle.api.client.event.ClientWorldTickEvents;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.base.event.QuiltCompatEvent;

/**
 * @deprecated Use Quilt Lifecycle Events' {@link org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents} instead.
 */
@Deprecated
@Environment(EnvType.CLIENT)
public final class ClientTickEvents {
	private ClientTickEvents() {
	}

	/**
	 * Called at the start of the client tick.
	 */
	public static final Event<StartTick> START_CLIENT_TICK = QuiltCompatEvent.fromQuilt(
			org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents.START,
			startTick -> startTick::onStartTick,
			invokerGetter -> client -> invokerGetter.get().startClientTick(client)
	);

	/**
	 * Called at the end of the client tick.
	 */
	public static final Event<EndTick> END_CLIENT_TICK = QuiltCompatEvent.fromQuilt(
			org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents.END,
			endTick -> endTick::onEndTick,
			invokerGetter -> client -> invokerGetter.get().endClientTick(client)
	);

	/**
	 * Called at the start of a ClientWorld's tick.
	 */
	public static final Event<StartWorldTick> START_WORLD_TICK = QuiltCompatEvent.fromQuilt(
			ClientWorldTickEvents.START,
			startWorldTick -> (client, world) -> startWorldTick.onStartTick(world),
			invokerGetter -> world -> invokerGetter.get().startWorldTick(MinecraftClient.getInstance(), world)
	);

	/**
	 * Called at the end of a ClientWorld's tick.
	 *
	 * <p>End of world tick may be used to start async computations for the next tick.
	 */
	public static final Event<EndWorldTick> END_WORLD_TICK = QuiltCompatEvent.fromQuilt(
			ClientWorldTickEvents.END,
			endWorldTick -> (client, world) -> endWorldTick.onEndTick(world),
			invokerGetter -> world -> invokerGetter.get().endWorldTick(MinecraftClient.getInstance(), world)
	);

	@FunctionalInterface
	public interface StartTick {
		void onStartTick(MinecraftClient client);
	}

	@FunctionalInterface
	public interface EndTick {
		void onEndTick(MinecraftClient client);
	}

	@FunctionalInterface
	public interface StartWorldTick {
		void onStartTick(ClientWorld world);
	}

	@FunctionalInterface
	public interface EndWorldTick {
		void onEndTick(ClientWorld world);
	}
}
