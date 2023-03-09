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

package net.fabricmc.fabric.impl.resource.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.quiltmc.qsl.resource.loader.api.ResourceLoader;

import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;

public class ResourceManagerHelperImpl implements ResourceManagerHelper {
	private static final Map<ResourceType, ResourceManagerHelperImpl> registryMap = new HashMap<>();

	private final ResourceType type;

	private Identifier lastResourceReloaderIdentifier = null;

	private ResourceManagerHelperImpl(ResourceType type) {
		this.type = type;
	}

	public static ResourceManagerHelperImpl get(ResourceType type) {
		return registryMap.computeIfAbsent(type, ResourceManagerHelperImpl::new);
	}

	@Override
	public void registerReloadListener(IdentifiableResourceReloadListener listener) {
		ResourceLoader resourceLoader = ResourceLoader.get(this.type);
		resourceLoader.registerReloader(listener);

		// Inject a synthetic ordering between listeners registered on the same namespace that are registered after each other
		// This matches the existing behavior of fabric-api where listeners are called in registration order, fixing some compatibility issues.
		// We split on namespaces to prevent grouping all fabric based listeners into one long chain and causing potential issues in actual ordering.
		// see i.e https://gitlab.com/cable-mc/cobblemon/-/issues/148 or https://github.com/apace100/calio/issues/3
		if (
				lastResourceReloaderIdentifier != null
						&& Objects.equals(lastResourceReloaderIdentifier.getNamespace(), listener.getQuiltId().getNamespace())
		) {
			resourceLoader.addReloaderOrdering(lastResourceReloaderIdentifier, listener.getQuiltId());
		}

		lastResourceReloaderIdentifier = listener.getQuiltId();
	}
}
