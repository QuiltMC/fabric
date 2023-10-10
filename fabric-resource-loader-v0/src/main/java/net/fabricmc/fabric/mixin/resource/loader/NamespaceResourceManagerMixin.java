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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.quiltmc.quilted_fabric_api.fabric.resource.loader.v0.impl.QuiltedFabricResource;
import org.quiltmc.qsl.resource.loader.api.GroupResourcePack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.metadata.ResourceMetadata;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.impl.resource.loader.FabricResource;
import net.fabricmc.fabric.impl.resource.loader.ResourcePackSourceTracker;

@Mixin(NamespaceResourceManager.class)
public class NamespaceResourceManagerMixin {
	@Shadow
	@Final
	private ResourceType type;

	@Inject(method = "getAllResources", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", remap = false, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
	private void trackSourceOnGetAllResources(Identifier id, CallbackInfoReturnable<List<Resource>> cir, Identifier metadataId, List<Resource> list, boolean bl, int i, NamespaceResourceManager.FilterablePack filterablePack, ResourcePack pack, InputSupplier<InputStream> inputSupplier, InputSupplier<InputStream> inputSupplier2) {
		if (pack instanceof GroupResourcePack groupPack) {
			var innerPacks = groupPack.getPacks(id.getNamespace());

			for (ResourcePack innerPack : innerPacks) {
				try {
					// This is a terrible equivalent to ResourcePack.contains(), help
					if (innerPack != null && innerPack.open(type, id).get().available() != 0) {
						var entry = (QuiltedFabricResource) list.get(list.size() - 1);
						entry.setFabricIndividualSource(ResourcePackSourceTracker.getSource(innerPack));
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Inject(method = "getResource", at = @At(value = "RETURN", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
	private void trackSourceOnGetResource(Identifier identifier, CallbackInfoReturnable<Optional<Resource>> cir, int i, NamespaceResourceManager.FilterablePack filterablePack, ResourcePack pack, InputSupplier<InputStream> inputSupplier, InputSupplier<ResourceMetadata> inputSupplier2) {
		if (cir.getReturnValue().orElseThrow() instanceof FabricResource resource) {
			if (pack instanceof GroupResourcePack groupPack) {
				var innerPacks = groupPack.getPacks(identifier.getNamespace());

				for (ResourcePack innerPack : innerPacks) {
					// This is a kinda bad equivalent to ResourcePack.contains()
					if (innerPack != null && innerPack.open(type, identifier) != null) {
						((QuiltedFabricResource) resource).setFabricIndividualSource(ResourcePackSourceTracker.getSource(innerPack));
					}
				}
			}
		}
	}
}
