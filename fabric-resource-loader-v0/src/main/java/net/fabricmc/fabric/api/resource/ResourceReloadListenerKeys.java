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

package net.fabricmc.fabric.api.resource;

import org.quiltmc.qsl.resource.loader.api.reloader.ResourceReloaderKeys;

import net.minecraft.util.Identifier;

/**
 * This class contains default keys for various Minecraft resource reload listeners.
 *
 * @see IdentifiableResourceReloadListener
 * @deprecated Use Quilt Resource Loader API's {@link org.quiltmc.qsl.resource.loader.api.reloader.ResourceReloaderKeys} instead.
 */
@Deprecated
public final class ResourceReloadListenerKeys {
	// client
	public static final Identifier SOUNDS = ResourceReloaderKeys.Client.SOUNDS;
	public static final Identifier FONTS = ResourceReloaderKeys.Client.FONTS;
	public static final Identifier MODELS = ResourceReloaderKeys.Client.MODELS;
	public static final Identifier LANGUAGES = ResourceReloaderKeys.Client.LANGUAGES;
	public static final Identifier TEXTURES = ResourceReloaderKeys.Client.TEXTURES;

	// server
	public static final Identifier TAGS = ResourceReloaderKeys.Server.TAGS;
	public static final Identifier RECIPES = ResourceReloaderKeys.Server.RECIPES;
	public static final Identifier ADVANCEMENTS = ResourceReloaderKeys.Server.ADVANCEMENTS;
	public static final Identifier FUNCTIONS = ResourceReloaderKeys.Server.FUNCTIONS;
	public static final Identifier LOOT_TABLES = ResourceReloaderKeys.Server.LOOT_TABLES;

	private ResourceReloadListenerKeys() {
	}
}
