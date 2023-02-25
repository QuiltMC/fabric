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

package net.fabricmc.fabric.mixin.resource.loader;

import org.quiltmc.quilted_fabric_api.fabric.resource.loader.v0.impl.QuiltedFabricResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourcePackSource;

import net.fabricmc.fabric.impl.resource.loader.FabricResource;
import net.fabricmc.fabric.impl.resource.loader.ResourcePackSourceTracker;

/**
 * Implements {@link FabricResource} (resource source getter/setter)
 * for vanilla's basic {@link Resource} used for most game resources.
 *
 * @see NamespaceResourceManagerMixin the usage site for this mixin
 */
@Mixin(Resource.class)
class ResourceMixin implements QuiltedFabricResource {
	@Unique
	private ResourcePackSource source = null;

	@SuppressWarnings("ConstantConditions")
	@Override
	public ResourcePackSource getFabricPackSource() {
		Resource self = (Resource) (Object) this;
		return this.source == null ? ResourcePackSourceTracker.getSource(self.getPack()) : this.source;
	}

	@Override
	public void setFabricIndividualSource(ResourcePackSource source) {
		this.source = source;
	}
}
