/*
 * Copyright 2022, 2023 QuiltMC
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

import java.util.Iterator;
import java.util.List;

import org.quiltmc.qsl.resource.loader.api.GroupResourcePack;
import org.quiltmc.qsl.resource.loader.impl.ResourceLoaderImpl;
import org.quiltmc.qsl.resource.loader.mixin.NamespaceResourceManagerAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.resource.ResourcePack;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.impl.resource.loader.FabricNamespaceResourceManagerEntry;
import net.fabricmc.fabric.impl.resource.loader.ResourcePackSourceTracker;

@Mixin(ResourceLoaderImpl.class)
abstract class ResourceLoaderImplMixin {
	@Inject(method = "appendResourcesFromGroup", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
	private static void setSourcesForGroupResources(NamespaceResourceManagerAccessor manager, Identifier id, GroupResourcePack groupResourcePack, List<NamespaceResourceManager.Entry> resources, CallbackInfo ci, List<ResourcePack> packs, Identifier metadataId, Iterator<ResourcePack> iterator, ResourcePack pack, NamespaceResourceManager casted, NamespaceResourceManager.Entry resource) {
		((FabricNamespaceResourceManagerEntry) resource).setFabricPackSource(ResourcePackSourceTracker.getSource(pack));
	}
}
