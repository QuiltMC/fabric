/*
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

<<<<<<<< HEAD:fabric-item-api-v1/src/main/java/net/fabricmc/fabric/mixin/item/CustomItemSettingImplAccessor.java
package net.fabricmc.fabric.mixin.item;

import java.util.function.Supplier;
========
package net.fabricmc.fabric.mixin.networking.client.accessor;
>>>>>>>> Fabric/1.20.2:fabric-networking-api-v1/src/client/java/net/fabricmc/fabric/mixin/networking/client/accessor/ClientCommonNetworkHandlerAccessor.java

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

<<<<<<<< HEAD:fabric-item-api-v1/src/main/java/net/fabricmc/fabric/mixin/item/CustomItemSettingImplAccessor.java
@Mixin(org.quiltmc.qsl.item.setting.impl.CustomItemSettingImpl.class)
public interface CustomItemSettingImplAccessor<T> {
	@Mutable
	@Accessor(remap = false)
	void setDefaultValue(Supplier<T> defaultValue);
========
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.ClientConnection;

@Mixin(ClientCommonNetworkHandler.class)
public interface ClientCommonNetworkHandlerAccessor {
	@Accessor
	ClientConnection getConnection();
>>>>>>>> Fabric/1.20.2:fabric-networking-api-v1/src/client/java/net/fabricmc/fabric/mixin/networking/client/accessor/ClientCommonNetworkHandlerAccessor.java
}
