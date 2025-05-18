package net.tkg.ModernMayhem.server.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.block.DuffelBagBlock;

import java.util.function.Supplier;

public class BlockRegistryMM {
    public static final DeferredRegister<Block> REGISTRY =
            DeferredRegister.create(ForgeRegistries.BLOCKS, ModernMayhemMod.ID);




    public static final RegistryObject<Block> DUFFEL_BAG_BLOCK = REGISTRY.register("duffel_bag_block",
            () -> new DuffelBagBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL).strength(0.5F).noOcclusion()));




    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = REGISTRY.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ItemRegistryMM.REGISTRY.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }



    public static void init(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
