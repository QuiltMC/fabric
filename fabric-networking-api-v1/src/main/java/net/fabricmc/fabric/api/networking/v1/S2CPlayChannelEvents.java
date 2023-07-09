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

package net.fabricmc.fabric.api.networking.v1;

import java.util.List;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.base.event.QuiltCompatEvent;
import net.fabricmc.fabric.impl.networking.QuiltPacketSender;

/**
 * Offers access to events related to the indication of a connected client's ability to receive packets in certain channels.
 *
 * @deprecated Use Quilt Networking's {@link org.quiltmc.qsl.networking.api.S2CPlayChannelEvents} instead.
 */
@Deprecated
public final class S2CPlayChannelEvents {
	/**
	 * An event for the server play network handler receiving an update indicating the connected client's ability to receive packets in certain channels.
	 * This event may be invoked at any time after login and up to disconnection.
	 */
	public static final Event<Register> REGISTER = QuiltCompatEvent.fromQuilt(org.quiltmc.qsl.networking.api.S2CPlayChannelEvents.REGISTER,
			register -> (handler, sender, server, channels) -> register.onChannelRegister(handler, new QuiltPacketSender(sender), server, channels),
			invokerGetter -> (handler, sender, server, channels) -> invokerGetter.get().onChannelRegister(handler, sender, server, channels)
	);

	/**
	 * An event for the server play network handler receiving an update indicating the connected client's lack of ability to receive packets in certain channels.
	 * This event may be invoked at any time after login and up to disconnection.
	 */
	public static final Event<Unregister> UNREGISTER = QuiltCompatEvent.fromQuilt(org.quiltmc.qsl.networking.api.S2CPlayChannelEvents.UNREGISTER,
			unregister -> (handler, sender, server, channels) -> unregister.onChannelUnregister(handler, new QuiltPacketSender(sender), server, channels),
			invokerGetter -> (handler, sender, server, channels) -> invokerGetter.get().onChannelUnregister(handler, sender, server, channels)
	);

	private S2CPlayChannelEvents() {
	}

	/**
	 * @see S2CPlayChannelEvents#REGISTER
	 */
	@FunctionalInterface
	public interface Register {
		void onChannelRegister(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
	}

	/**
	 * @see S2CPlayChannelEvents#UNREGISTER
	 */
	@FunctionalInterface
	public interface Unregister {
		void onChannelUnregister(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server, List<Identifier> channels);
	}
}
