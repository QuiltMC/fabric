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

package net.fabricmc.fabric.impl.networking;

import org.jetbrains.annotations.Nullable;
import org.quiltmc.qsl.networking.api.PacketSender;

import net.minecraft.network.PacketCallbacks;
import net.minecraft.network.packet.Packet;

class Fabric2QuiltPacketSender<C> implements PacketSender<C> {
	final net.fabricmc.fabric.api.networking.v1.PacketSender sender;

	protected Fabric2QuiltPacketSender(net.fabricmc.fabric.api.networking.v1.PacketSender fabric) {
		this.sender = fabric;
	}

	@Override
	public Packet<?> createPacket(C payload) {
		throw new UnsupportedOperationException("This should not be called!");
	}

	@Override
	public void sendPacket(Packet<?> packet) {
		this.sender.sendPacket(packet);
	}

	@Override
	public void sendPacket(Packet<?> packet, @Nullable PacketCallbacks listener) {
		this.sender.sendPacket(packet, listener);
	}
}
