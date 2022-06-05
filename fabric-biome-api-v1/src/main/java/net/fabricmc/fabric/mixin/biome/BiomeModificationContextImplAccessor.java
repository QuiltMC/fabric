package net.fabricmc.fabric.mixin.biome;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(org.quiltmc.qsl.worldgen.biome.impl.modification.BiomeModificationContextImpl.class)
public interface BiomeModificationContextImplAccessor {
	@Invoker("freeze")
	public void invokeFreeze();
}
