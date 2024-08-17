
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;

import net.mcreator.mm.MmMod;

public class MmModParticleTypes {
	public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MmMod.MODID);
	public static final RegistryObject<SimpleParticleType> SMOKE_FADE_PARTICLE = REGISTRY.register("smoke_fade_particle", () -> new SimpleParticleType(false));
	public static final RegistryObject<SimpleParticleType> SMOKE_PARTICLE = REGISTRY.register("smoke_particle", () -> new SimpleParticleType(false));
}
