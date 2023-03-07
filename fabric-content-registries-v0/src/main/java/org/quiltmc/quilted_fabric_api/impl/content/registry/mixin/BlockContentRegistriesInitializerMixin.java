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

import org.quiltmc.qsl.block.content.registry.impl.BlockContentRegistriesInitializer;
import org.quiltmc.qsl.registry.attachment.api.RegistryEntryAttachment;
import org.quiltmc.quilted_fabric_api.impl.content.registry.util.QuiltDeferringQueues;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockContentRegistriesInitializer.class)
public class BlockContentRegistriesInitializerMixin {
	@Unique
	private static RegistryEntryAttachment<?, ?> qfapi$capturedAttachment;

	@Inject(method = "addMapToAttachment", at = @At("HEAD"), remap = false)
	private static <T, V> void captureAttachment(Map<T, V> map, RegistryEntryAttachment<T, V> attachment, CallbackInfo ci) {
		qfapi$capturedAttachment = attachment;
	}

	@SuppressWarnings("unchecked")
	@Redirect(method = "addMapToAttachment", at = @At(value = "INVOKE", target = "Ljava/util/Map;forEach(Ljava/util/function/BiConsumer;)V"), remap = false)
	private static <K, V> void redirectMapPut(Map<K, V> instance, BiConsumer<? super K, ? super V> v) {
		instance.forEach((key, value) -> {
			QuiltDeferringQueues.addEntry((RegistryEntryAttachment<K, V>) qfapi$capturedAttachment, key, value);
		});
	}
}
