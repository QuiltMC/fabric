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

package net.fabricmc.fabric.mixin.resource.loader;

import java.util.function.Consumer;

import org.quiltmc.qsl.resource.loader.impl.ModResourcePackProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourceType;

import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;

@SuppressWarnings("UnstableApiUsage")
@Mixin(ModResourcePackProvider.class)
public class ModResourcePackProviderMixin {
	@Shadow
	@Final
	private ResourceType type;

	@Inject(method = "register(Ljava/util/function/Consumer;)V", at = @At("RETURN"))
	private void onRegister(Consumer<ResourcePackProfile> profileAdder, CallbackInfo ci) {
		switch (this.type) {
		case CLIENT_RESOURCES -> ModResourcePackCreator.CLIENT_RESOURCE_PACK_PROVIDER.register(profileAdder);
		case SERVER_DATA -> new ModResourcePackCreator(ResourceType.SERVER_DATA).register(profileAdder);
		}
	}
}
