/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

package net.fabricmc.fabric.mixin.registry.sync.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;

import net.fabricmc.fabric.impl.registry.sync.trackers.vanilla.BlockInitTracker;
import net.fabricmc.fabric.mixin.registry.sync.RegistriesAccessor;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
	@Unique
	private static Logger FABRIC_LOGGER = LoggerFactory.getLogger(MinecraftClientMixin.class);

	@Inject(at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;thread:Ljava/lang/Thread;", shift = At.Shift.AFTER, ordinal = 0), method = "run")
	private void onStart(CallbackInfo ci) {
		// Freeze the registries on the client
		FABRIC_LOGGER.debug("Freezing registries");
		// Registries.bootstrap();
		// Quilt injects its init point at that method; We avoid its usage by doing this:
		// Registries.init();
		RegistriesAccessor.invokeFreezeRegistries();
		RegistriesAccessor.invokeValidate(Registries.REGISTRIES);
		BlockInitTracker.postFreeze();
		ItemGroups.collect();
	}
}
