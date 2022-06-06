package net.fabricmc.fabric.mixin.biome;

import org.quiltmc.qsl.worldgen.biome.impl.modification.BiomeSelectionContextImpl;
import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;

@Mixin(BiomeSelectionContextImpl.class)
public abstract class BiomeSelectionContextImplMixin implements BiomeSelectionContext { }
