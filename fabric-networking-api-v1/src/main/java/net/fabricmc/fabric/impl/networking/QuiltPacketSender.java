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

package net.fabricmc.fabric.impl.networking;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.jetbrains.annotations.Nullable;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.PacketCallbacks;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.networking.v1.PacketSender;

public final class QuiltPacketSender implements PacketSender {
	private final org.quiltmc.qsl.networking.api.PacketSender sender;

	public QuiltPacketSender(org.quiltmc.qsl.networking.api.PacketSender sender) {
		this.sender = sender;
	}

	@Override
	public Packet<?> createPacket(Identifier channelName, PacketByteBuf buf) {
		return this.sender.createPacket(channelName, buf);
	}

	@Override
	public void sendPacket(Packet<?> packet) {
		this.sender.sendPacket(packet);
	}

	@Override
	public void sendPacket(Packet<?> packet, @Nullable GenericFutureListener<? extends Future<? super Void>> callback) {
		this.sender.sendPacket(packet, GenericFutureListenerHolder.create(callback));
	}

	@Override
	public void sendPacket(Packet<?> packet, @Nullable PacketCallbacks callback) {
		this.sender.sendPacket(packet, callback);
	}

	@Override
	public void sendPacket(Identifier channel, PacketByteBuf buf, @Nullable GenericFutureListener<? extends Future<? super Void>> callback) {
		this.sender.sendPacket(channel, buf, GenericFutureListenerHolder.create(callback));
	}
}
