
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

import net.mcreator.mm.block.DuffelbagBlockBlock;
import net.mcreator.mm.MmMod;

public class MmModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, MmMod.MODID);
	public static final RegistryObject<Block> DUFFELBAG_BLOCK = REGISTRY.register("duffelbag_block", () -> new DuffelbagBlockBlock());
	// Start of user code block custom blocks
	// End of user code block custom blocks
}
