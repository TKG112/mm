
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;

import net.mcreator.mm.block.entity.DuffelbagBlockBlockEntity;
import net.mcreator.mm.MmMod;

public class MmModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MmMod.MODID);
	public static final RegistryObject<BlockEntityType<?>> DUFFELBAG_BLOCK = register("duffelbag_block", MmModBlocks.DUFFELBAG_BLOCK, DuffelbagBlockBlockEntity::new);

	private static RegistryObject<BlockEntityType<?>> register(String registryname, RegistryObject<Block> block, BlockEntityType.BlockEntitySupplier<?> supplier) {
		return REGISTRY.register(registryname, () -> BlockEntityType.Builder.of(supplier, block.get()).build(null));
	}
}
