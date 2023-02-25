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

package net.fabricmc.fabric.api.object.builder.v1.block;

import java.util.function.Function;
import java.util.function.ToIntFunction;

import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.featuretoggle.FeatureFlag;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.mixin.object.builder.AbstractBlockAccessor;

/**
 * Fabric's version of Block.Settings. Adds additional methods and hooks
 * not found in the original class.
 *
 * <p>Make note that this behaves slightly different from the
 * vanilla counterpart, copying some settings that vanilla does not.
 *
 * <p>To use it, simply replace Block.Settings.of() with
 * FabricBlockSettings.of().
 *
 * @deprecated Use Quilt Block Extensions API's {@link org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings} instead.
 */
@Deprecated
public class FabricBlockSettings extends QuiltBlockSettings {
	protected FabricBlockSettings(Material material, MapColor color) {
		super(material, color);
	}

	protected FabricBlockSettings(Material material, Function<BlockState, MapColor> mapColorProvider) {
		super(material, mapColorProvider);
	}

	protected FabricBlockSettings(AbstractBlock.Settings settings) {
		super(settings);
	}

	public static FabricBlockSettings of(Material material) {
		return of(material, material.getColor());
	}

	public static FabricBlockSettings of(Material material, MapColor color) {
		return new FabricBlockSettings(material, color);
	}

	public static FabricBlockSettings of(Material material, DyeColor color) {
		return new FabricBlockSettings(material, color.getMapColor());
	}

	public static FabricBlockSettings of(Material material, Function<BlockState, MapColor> mapColor) {
		return new FabricBlockSettings(material, mapColor);
	}

	public static FabricBlockSettings copyOf(AbstractBlock block) {
		return new FabricBlockSettings(((AbstractBlockAccessor) block).getSettings());
	}

	public static FabricBlockSettings copyOf(AbstractBlock.Settings settings) {
		return new FabricBlockSettings(settings);
	}

	@Override
	public FabricBlockSettings noCollision() {
		super.noCollision();
		return this;
	}

	@Override
	public FabricBlockSettings nonOpaque() {
		super.nonOpaque();
		return this;
	}

	@Override
	public FabricBlockSettings slipperiness(float value) {
		super.slipperiness(value);
		return this;
	}

	@Override
	public FabricBlockSettings velocityMultiplier(float velocityMultiplier) {
		super.velocityMultiplier(velocityMultiplier);
		return this;
	}

	@Override
	public FabricBlockSettings jumpVelocityMultiplier(float jumpVelocityMultiplier) {
		super.jumpVelocityMultiplier(jumpVelocityMultiplier);
		return this;
	}

	@Override
	public FabricBlockSettings sounds(BlockSoundGroup group) {
		super.sounds(group);
		return this;
	}

	/**
	 * @deprecated Please use {@link FabricBlockSettings#luminance(ToIntFunction)}.
	 */
	@Deprecated
	public FabricBlockSettings lightLevel(ToIntFunction<BlockState> levelFunction) {
		return this.luminance(levelFunction);
	}

	@Override
	public FabricBlockSettings luminance(ToIntFunction<BlockState> luminanceFunction) {
		super.luminance(luminanceFunction);
		return this;
	}

	@Override
	public FabricBlockSettings strength(float hardness, float resistance) {
		super.strength(hardness, resistance);
		return this;
	}

	@Override
	public FabricBlockSettings breakInstantly() {
		super.breakInstantly();
		return this;
	}

	public FabricBlockSettings strength(float strength) {
		super.strength(strength);
		return this;
	}

	@Override
	public FabricBlockSettings ticksRandomly() {
		super.ticksRandomly();
		return this;
	}

	@Override
	public FabricBlockSettings dynamicBounds() {
		super.dynamicBounds();
		return this;
	}

	@Override
	public FabricBlockSettings dropsNothing() {
		super.dropsNothing();
		return this;
	}

	@Override
	public FabricBlockSettings dropsLike(Block block) {
		super.dropsLike(block);
		return this;
	}

	@Override
	public FabricBlockSettings air() {
		super.air();
		return this;
	}

	@Override
	public FabricBlockSettings allowsSpawning(AbstractBlock.TypedContextPredicate<EntityType<?>> predicate) {
		super.allowsSpawning(predicate);
		return this;
	}

	@Override
	public FabricBlockSettings solidBlock(AbstractBlock.ContextPredicate predicate) {
		super.solidBlock(predicate);
		return this;
	}

	@Override
	public FabricBlockSettings suffocates(AbstractBlock.ContextPredicate predicate) {
		super.suffocates(predicate);
		return this;
	}

	@Override
	public FabricBlockSettings blockVision(AbstractBlock.ContextPredicate predicate) {
		super.blockVision(predicate);
		return this;
	}

	@Override
	public FabricBlockSettings postProcess(AbstractBlock.ContextPredicate predicate) {
		super.postProcess(predicate);
		return this;
	}

	@Override
	public FabricBlockSettings emissiveLighting(AbstractBlock.ContextPredicate predicate) {
		super.emissiveLighting(predicate);
		return this;
	}

	/**
	 * Make the block require tool to drop and slows down mining speed if the incorrect tool is used.
	 */
	@Override
	public FabricBlockSettings requiresTool() {
		super.requiresTool();
		return this;
	}

	@Override
	public FabricBlockSettings mapColor(MapColor color) {
		super.mapColor(color);
		return this;
	}

	@Override
	public FabricBlockSettings hardness(float hardness) {
		super.hardness(hardness);
		return this;
	}

	@Override
	public FabricBlockSettings resistance(float resistance) {
		super.resistance(resistance);
		return this;
	}

	@Override
	public FabricBlockSettings offsetType(AbstractBlock.OffsetType offsetType) {
		super.offsetType(offsetType);
		return this;
	}

	@Override
	public FabricBlockSettings offsetType(Function<BlockState, AbstractBlock.OffsetType> offsetType) {
		super.offsetType(offsetType);
		return this;
	}

	@Override
	public FabricBlockSettings noBlockBreakParticles() {
		super.noBlockBreakParticles();
		return this;
	}

	@Override
	public FabricBlockSettings requires(FeatureFlag... features) {
		super.requires(features);
		return this;
	}

	/* FABRIC ADDITIONS*/

	/**
	 * @deprecated Please use {@link FabricBlockSettings#luminance(int)}.
	 */
	@Deprecated
	public FabricBlockSettings lightLevel(int lightLevel) {
		this.luminance(lightLevel);
		return this;
	}

	public FabricBlockSettings luminance(int luminance) {
		this.luminance(ignored -> luminance);
		return this;
	}

	public FabricBlockSettings drops(Identifier dropTableId) {
		super.drops(dropTableId);
		return this;
	}

	/* FABRIC DELEGATE WRAPPERS */

	/**
	 * @deprecated Please migrate to {@link FabricBlockSettings#mapColor(MapColor)}
	 */
	@Deprecated
	public FabricBlockSettings materialColor(MapColor color) {
		return this.mapColor(color);
	}

	/**
	 * @deprecated Please migrate to {@link FabricBlockSettings#mapColor(DyeColor)}
	 */
	@Deprecated
	public FabricBlockSettings materialColor(DyeColor color) {
		return this.mapColor(color);
	}

	public FabricBlockSettings mapColor(DyeColor color) {
		return this.mapColor(color.getMapColor());
	}

	public FabricBlockSettings collidable(boolean collidable) {
		super.collidable(collidable);
		return this;
	}
}
