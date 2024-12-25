package net.tkg.ModernMayhem.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.curios.back.*;
import net.tkg.ModernMayhem.item.curios.body.*;
import net.tkg.ModernMayhem.item.curios.facewear.*;

public class ItemRegistryMM {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ModernMayhemMod.ID);

    public static final RegistryObject<Item> BLACK_GPNVG = REGISTRY.register("black_gpnvg", BlackGPNVGItem::new);
    public static final RegistryObject<Item> BLACK_NVG21 = REGISTRY.register("black_nvg21", BlackNVG21Item::new);
    public static final RegistryObject<Item> ULTRA_GAMER_GPNVG = REGISTRY.register("ultra_gamer_gpnvg", UltraGamerGPNVGItem::new);
    public static final RegistryObject<Item> TAN_GPNVG = REGISTRY.register("tan_gpnvg", TanGPNVGItem::new);
    public static final RegistryObject<Item> TAN_NVG21 = REGISTRY.register("tan_nvg21", TanNVG21Item::new);
    public static final RegistryObject<Item> GREEN_NVG21 = REGISTRY.register("green_nvg21", GreenNVG21Item::new);
    public static final RegistryObject<Item> MENU_ITEM = REGISTRY.register("menu_item", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BLACK_BACKPACK_T1 = REGISTRY.register("black_backpack_t1", BlackBackpackT1Item::new);
    public static final RegistryObject<Item> BLACK_BACKPACK_T2 = REGISTRY.register("black_backpack_t2", BlackBackpackT2Item::new);
    public static final RegistryObject<Item> BLACK_BACKPACK_T3 = REGISTRY.register("black_backpack_t3", BlackBackpackT3Item::new);
    public static final RegistryObject<Item> TAN_BACKPACK_T1 = REGISTRY.register("tan_backpack_t1", TanBackpackT1Item::new);
    public static final RegistryObject<Item> TAN_BACKPACK_T2 = REGISTRY.register("tan_backpack_t2", TanBackpackT2Item::new);
    public static final RegistryObject<Item> TAN_BACKPACK_T3 = REGISTRY.register("tan_backpack_t3", TanBackpackT3Item::new);

    public static final RegistryObject<Item> BLACK_PLATE_CARRIER = REGISTRY.register("black_plate_carrier", BlackPlateCarrierItem::new);
    public static final RegistryObject<Item> BLACK_PLATE_CARRIER_AMMO = REGISTRY.register("black_plate_carrier_ammo", BlackPlateCarrierAmmoItem::new);
    public static final RegistryObject<Item> BLACK_PLATE_CARRIER_POUCHES = REGISTRY.register("black_plate_carrier_pouches", BlackPlateCarrierPouchesItem::new);
    public static final RegistryObject<Item> TAN_BANDOLEER = REGISTRY.register("tan_bandoleer", TanBandoleerItem::new);
    public static final RegistryObject<Item> GREEN_RECON = REGISTRY.register("green_recon_rig", GreenReconRigItem::new);


    public static void init(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
