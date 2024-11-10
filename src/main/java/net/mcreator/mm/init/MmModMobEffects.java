
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.effect.MobEffect;

import net.mcreator.mm.potion.VignetteMobEffect;
import net.mcreator.mm.potion.FlashMobEffect;
import net.mcreator.mm.potion.EarRingMobEffect;
import net.mcreator.mm.MmMod;

public class MmModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MmMod.MODID);
	public static final RegistryObject<MobEffect> FLASH = REGISTRY.register("flash", () -> new FlashMobEffect());
	public static final RegistryObject<MobEffect> VIGNETTE = REGISTRY.register("vignette", () -> new VignetteMobEffect());
	public static final RegistryObject<MobEffect> EAR_RING = REGISTRY.register("ear_ring", () -> new EarRingMobEffect());
}
