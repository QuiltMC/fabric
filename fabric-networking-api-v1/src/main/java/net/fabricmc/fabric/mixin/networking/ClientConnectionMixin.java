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

package net.fabricmc.fabric.mixin.networking;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketCallbacks;

import net.fabricmc.fabric.impl.networking.GenericFutureListenerHolder;

@Mixin(ClientConnection.class)
abstract class ClientConnectionMixin {
	@Inject(method = "sendInternal", at = @At(value = "INVOKE_ASSIGN", target = "Lio/netty/channel/Channel;writeAndFlush(Ljava/lang/Object;)Lio/netty/channel/ChannelFuture;"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void sendInternal(Packet<?> packet, @Nullable PacketCallbacks listener, NetworkState packetState, NetworkState currentState, CallbackInfo ci, ChannelFuture channelFuture) {
		if (listener instanceof GenericFutureListenerHolder holder) {
			channelFuture.addListener(holder.getDelegate());
			channelFuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
			ci.cancel();
		}
	}
}
