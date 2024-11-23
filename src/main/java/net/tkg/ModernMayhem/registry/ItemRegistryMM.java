package net.tkg.ModernMayhem.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.BlackGPNVGItem;
import net.tkg.ModernMayhem.item.BlackNVG21Item;

public class ItemRegistryMM {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ModernMayhemMod.ID);

    public static final RegistryObject<Item> BLACK_GPNVG = REGISTRY.register("black_gpnvg", () -> new BlackGPNVGItem());
    public static final RegistryObject<Item> BLACK_NVG21 = REGISTRY.register("black_nvg21", () -> new BlackNVG21Item());

    public static void init(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
