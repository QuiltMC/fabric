/*
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

package net.fabricmc.fabric.impl.qsl.item.v1;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.item.setting.api.RecipeRemainderProvider;
import org.quiltmc.qsl.item.setting.impl.CustomItemSettingImpl;

import net.fabricmc.fabric.mixin.item.CustomItemSettingImplAccessor;

public class QuiltItemCompatMod implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		var accessor = ((CustomItemSettingImplAccessor<RecipeRemainderProvider>) CustomItemSettingImpl.RECIPE_REMAINDER_PROVIDER);
		accessor.setDefaultValue(() -> (original, recipe) -> original.getItem().getRecipeRemainder(original));
	}
}
