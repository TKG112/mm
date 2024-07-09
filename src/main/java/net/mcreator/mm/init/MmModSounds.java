
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;

import net.mcreator.mm.MmMod;

public class MmModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MmMod.MODID);
	public static final RegistryObject<SoundEvent> NVON = REGISTRY.register("nvon", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("mm", "nvon")));
	public static final RegistryObject<SoundEvent> NVOFF = REGISTRY.register("nvoff", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("mm", "nvoff")));
	public static final RegistryObject<SoundEvent> NVGTURNKNOB = REGISTRY.register("nvgturnknob", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("mm", "nvgturnknob")));
}
