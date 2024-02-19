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

package net.fabricmc.fabric.impl.resource.loader;

import java.util.function.Consumer;

import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;

/**
 * Represents a resource pack provider for mods and built-in mods resource packs.
 */
public class ModResourcePackCreator implements ResourcePackProvider {
	public static final ResourcePackSource RESOURCE_PACK_SOURCE = text -> Text.translatable("pack.nameAndSource", text, Text.translatable("pack.source.fabricmod"));
	public static final ModResourcePackCreator CLIENT_RESOURCE_PACK_PROVIDER = new ModResourcePackCreator(ResourceType.CLIENT_RESOURCES);
	public static final ModResourcePackCreator SERVER_RESOURCE_PACK_PROVIDER = new ModResourcePackCreator(ResourceType.SERVER_DATA);
	private final ResourcePackProfile.Factory factory;
	private final ResourceType type;

	public ModResourcePackCreator(ResourceType type) {
		this.type = type;
		this.factory = (name, text, bl, supplier, metadata, initialPosition, source) ->
				new ResourcePackProfile(name, text, bl, supplier, metadata, type, initialPosition, source);
	}

	/**
	 * Registers the resource packs.
	 *
	 * @param consumer The resource pack profile consumer.
	 */
	public void register(Consumer<ResourcePackProfile> consumer) {
		this.register(consumer, this.factory);
	}

	@Override
	public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory factory) {
		// This should stay as it's been used in *some* mods, it's bad I know, but it's an easy way to inject resource
		// packs, it highlights the need for an API.
	}
}
