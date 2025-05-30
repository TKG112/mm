package net.tkg.ModernMayhem.server.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.block.entity.DuffelBagBlockEntity;

public class BlockEntityRegistryMM {

    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ModernMayhemMod.ID);

    public static final RegistryObject<BlockEntityType<DuffelBagBlockEntity>> DUFFEL_BAG = REGISTRY.register("duffel_bag",
                    () -> BlockEntityType.Builder.of(DuffelBagBlockEntity::new, BlockRegistryMM.DUFFEL_BAG_BLOCK.get()).build(null));



    public static void init(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
