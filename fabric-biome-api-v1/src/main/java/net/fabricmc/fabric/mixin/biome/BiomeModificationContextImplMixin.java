package net.fabricmc.fabric.mixin.biome;

import org.quiltmc.qsl.worldgen.biome.impl.modification.BiomeModificationContextImpl;
import org.spongepowered.asm.mixin.Mixin;

import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;

@Mixin(BiomeModificationContextImpl.class)
public abstract class BiomeModificationContextImplMixin implements BiomeModificationContext { }
