/*
 * Copyright 2023 QuiltMC
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

package org.quiltmc.quilted_fabric_api.impl.content.registry.mixin;

import java.util.Map;
import java.util.function.BiConsumer;

import org.quiltmc.qsl.item.content.registry.api.ItemContentRegistries;
import org.quiltmc.qsl.item.content.registry.impl.ItemContentRegistriesInitializer;
import org.quiltmc.qsl.registry.attachment.api.RegistryEntryAttachment;
import org.quiltmc.quilted_fabric_api.impl.content.registry.util.QuiltDeferringQueues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.item.Item;

@Mixin(ItemContentRegistriesInitializer.class)
public class ItemContentRegistriesInitializerMixin {
	@Redirect(method = "onInitialize", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V", ordinal = 0), remap = false)
	private void redirectInitialFuelTimesPut(Map<Item, Integer> instance, BiConsumer<?, ?> v) {
		instance.forEach((item, integer) -> {
			QuiltDeferringQueues.addEntry(ItemContentRegistries.FUEL_TIME, item, integer);
		});
	}

	@Redirect(method = "lambda$onInitialize$1", at = @At(value = "INVOKE", target = "Lorg/quiltmc/qsl/registry/attachment/api/RegistryEntryAttachment;put(Ljava/lang/Object;Ljava/lang/Object;)V"), remap = false)
	private static <R, V> void redirectInitialCompostChancePut(RegistryEntryAttachment<R, V> instance, R r, V v) {
		QuiltDeferringQueues.addEntry(instance, r, v);
	}
}
