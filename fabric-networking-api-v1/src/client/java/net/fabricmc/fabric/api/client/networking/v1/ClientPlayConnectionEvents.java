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

package net.fabricmc.fabric.api.client.networking.v1;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.impl.base.event.QuiltCompatEvent;
import net.fabricmc.fabric.impl.networking.QuiltPacketSender;

/**
 * Offers access to events related to the connection to a server on a logical client.
 */
@Deprecated
public final class ClientPlayConnectionEvents {
	/**
	 * Event indicating a connection entered the PLAY state, ready for registering channel handlers.
	 *
	 * @see ClientPlayNetworking#registerReceiver(Identifier, ClientPlayNetworking.PlayChannelHandler)
	 * @deprecated Use Quilt Networking's {@link org.quiltmc.qsl.networking.api.client.ClientPlayConnectionEvents} instead.
	 */
	public static final Event<Init> INIT = QuiltCompatEvent.fromQuilt(org.quiltmc.qsl.networking.api.client.ClientPlayConnectionEvents.INIT,
			init -> init::onPlayInit,
			invokerGetter -> (handler, client) -> invokerGetter.get().onPlayInit(handler, client)
	);

	/**
	 * An event for notification when the client play network handler is ready to send packets to the server.
	 *
	 * <p>At this stage, the network handler is ready to send packets to the server.
	 * Since the client's local state has been set up.
	 */
	public static final Event<Join> JOIN = QuiltCompatEvent.fromQuilt(org.quiltmc.qsl.networking.api.client.ClientPlayConnectionEvents.JOIN,
			join -> (handler, sender, client) -> join.onPlayReady(handler, new QuiltPacketSender(sender), client),
			invokerGetter -> (handler, sender, client) -> invokerGetter.get().onPlayReady(handler, sender, client)
	);

	/**
	 * An event for the disconnection of the client play network handler.
	 *
	 * <p>No packets should be sent when this event is invoked.
	 */
	public static final Event<Disconnect> DISCONNECT = QuiltCompatEvent.fromQuilt(org.quiltmc.qsl.networking.api.client.ClientPlayConnectionEvents.DISCONNECT,
			disconnect -> disconnect::onPlayDisconnect,
			invokerGetter -> (handler, client) -> invokerGetter.get().onPlayDisconnect(handler, client)
	);

	private ClientPlayConnectionEvents() {
	}

	@FunctionalInterface
	public interface Init {
		void onPlayInit(ClientPlayNetworkHandler handler, MinecraftClient client);
	}

	@FunctionalInterface
	public interface Join {
		void onPlayReady(ClientPlayNetworkHandler handler, PacketSender sender, MinecraftClient client);
	}

	@FunctionalInterface
	public interface Disconnect {
		void onPlayDisconnect(ClientPlayNetworkHandler handler, MinecraftClient client);
	}
}
