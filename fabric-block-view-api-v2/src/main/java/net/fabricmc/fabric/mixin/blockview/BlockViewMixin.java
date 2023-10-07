/*
<<<<<<< HEAD
 * Copyright 2016, 2017, 2018, 2019 FabricMC
 * Copyright 2023 The Quilt Project
=======
 * Copyright 2022 The Quilt Project
>>>>>>> Fabric/1.20.2
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

<<<<<<< HEAD
package net.fabricmc.fabric.mixin.blockview;

import org.spongepowered.asm.mixin.Mixin;

<<<<<<<< HEAD:fabric-block-view-api-v2/src/main/java/net/fabricmc/fabric/mixin/blockview/BlockViewMixin.java
=======
<<<<<<<< HEAD:fabric-registry-sync-v0/src/testmod/java/net/fabricmc/fabric/test/registry/sync/mixin/StatusEffectAccessor.java
package net.fabricmc.fabric.test.registry.sync.mixin;
========
package net.fabricmc.fabric.mixin.blockview;
>>>>>>>> Fabric/1.20.2:fabric-block-view-api-v2/src/main/java/net/fabricmc/fabric/mixin/blockview/BlockViewMixin.java

import org.spongepowered.asm.mixin.Mixin;

<<<<<<<< HEAD:fabric-registry-sync-v0/src/testmod/java/net/fabricmc/fabric/test/registry/sync/mixin/StatusEffectAccessor.java
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

@Mixin(StatusEffect.class)
public interface StatusEffectAccessor {
	@Invoker("<init>")
	static StatusEffect createNewStatusEffect(StatusEffectCategory category, int color) {
		throw new IllegalStateException("Mixin injection failed.");
	}
========
>>>>>>> Fabric/1.20.2
import net.minecraft.world.BlockView;

import net.fabricmc.fabric.api.blockview.v2.FabricBlockView;

@Mixin(BlockView.class)
public interface BlockViewMixin extends FabricBlockView {
<<<<<<< HEAD
========
import net.minecraft.network.NetworkState;
import net.minecraft.util.Identifier;

public interface ChannelInfoHolder {
	/**
	 * @return Channels which are declared as receivable by the other side but have not been declared yet.
	 */
	Collection<Identifier> getPendingChannelsNames(NetworkState state);
>>>>>>>> Fabric/1.20.2:fabric-networking-api-v1/src/main/java/net/fabricmc/fabric/impl/networking/ChannelInfoHolder.java
=======
>>>>>>>> Fabric/1.20.2:fabric-block-view-api-v2/src/main/java/net/fabricmc/fabric/mixin/blockview/BlockViewMixin.java
>>>>>>> Fabric/1.20.2
}
