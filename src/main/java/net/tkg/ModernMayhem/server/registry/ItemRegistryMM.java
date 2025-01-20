package net.tkg.ModernMayhem.server.registry;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.NVGGoggleList;
import net.tkg.ModernMayhem.server.item.armor.CustomArmorItem;
import net.tkg.ModernMayhem.server.item.curios.back.BackpackItem;
import net.tkg.ModernMayhem.server.item.curios.body.BandoleerItem;
import net.tkg.ModernMayhem.server.item.curios.body.PlateCarrierItem;
import net.tkg.ModernMayhem.server.item.curios.body.ReconRigItem;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.util.ArmorProperties;

public class ItemRegistryMM {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ModernMayhemMod.ID);

    public static final RegistryObject<Item> BLACK_GPNVG = REGISTRY.register("black_gpnvg",
            () -> new NVGGogglesItem(NVGGoggleList.BLACK_GPNVG));
    public static final RegistryObject<Item> BLACK_NVG21 = REGISTRY.register("black_nvg21",
            () -> new NVGGogglesItem(NVGGoggleList.BLACK_NVG21));
    public static final RegistryObject<Item> ULTRA_GAMER_GPNVG = REGISTRY.register("ultra_gamer_gpnvg",
            () -> new NVGGogglesItem(NVGGoggleList.GAMER_GPNVG));
    public static final RegistryObject<Item> TAN_GPNVG = REGISTRY.register("tan_gpnvg",
            () -> new NVGGogglesItem(NVGGoggleList.TAN_GPNVG));
    public static final RegistryObject<Item> TAN_NVG21 = REGISTRY.register("tan_nvg21",
            () -> new NVGGogglesItem(NVGGoggleList.TAN_NVG21));
    public static final RegistryObject<Item> GREEN_NVG21 = REGISTRY.register("green_nvg21",
            () -> new NVGGogglesItem(NVGGoggleList.GREEN_NVG21));

    public static final RegistryObject<Item> BLACK_BACKPACK_T1 = REGISTRY.register("black_backpack_t1",
            () -> new BackpackItem(0, 1));
    public static final RegistryObject<Item> BLACK_BACKPACK_T2 = REGISTRY.register("black_backpack_t2",
            () -> new BackpackItem(0, 2));
    public static final RegistryObject<Item> BLACK_BACKPACK_T3 = REGISTRY.register("black_backpack_t3",
            () -> new BackpackItem(0, 3));
    public static final RegistryObject<Item> GREEN_BACKPACK_T1 = REGISTRY.register("green_backpack_t1",
            () -> new BackpackItem(1, 1));
    public static final RegistryObject<Item> GREEN_BACKPACK_T2 = REGISTRY.register("green_backpack_t2",
            () -> new BackpackItem(1, 2));
    public static final RegistryObject<Item> GREEN_BACKPACK_T3 = REGISTRY.register("green_backpack_t3",
            () -> new BackpackItem(1, 3));
    public static final RegistryObject<Item> TAN_BACKPACK_T1 = REGISTRY.register("tan_backpack_t1",
            () -> new BackpackItem(2, 1));
    public static final RegistryObject<Item> TAN_BACKPACK_T2 = REGISTRY.register("tan_backpack_t2",
            () -> new BackpackItem(2, 2));
    public static final RegistryObject<Item> TAN_BACKPACK_T3 = REGISTRY.register("tan_backpack_t3",
            () -> new BackpackItem(2, 3));

    public static final RegistryObject<Item> BLACK_PLATE_CARRIER = REGISTRY.register("black_plate_carrier",
            () -> new PlateCarrierItem("default", 0));
    public static final RegistryObject<Item> BLACK_PLATE_CARRIER_AMMO = REGISTRY.register("black_plate_carrier_ammo",
            () -> new PlateCarrierItem("ammo", 0));
    public static final RegistryObject<Item> BLACK_PLATE_CARRIER_POUCHES = REGISTRY.register("black_plate_carrier_pouches",
            () -> new PlateCarrierItem("pouches", 0));
    public static final RegistryObject<Item> TAN_PLATE_CARRIER = REGISTRY.register("tan_plate_carrier",
            () -> new PlateCarrierItem("default", 1));
    public static final RegistryObject<Item> TAN_PLATE_CARRIER_AMMO = REGISTRY.register("tan_plate_carrier_ammo",
            () -> new PlateCarrierItem("ammo", 1));
    public static final RegistryObject<Item> TAN_PLATE_CARRIER_POUCHES = REGISTRY.register("tan_plate_carrier_pouches",
            () -> new PlateCarrierItem("pouches", 1));
    public static final RegistryObject<Item> TAN_BANDOLEER = REGISTRY.register("tan_bandoleer", BandoleerItem::new);
    public static final RegistryObject<Item> GREEN_RECON = REGISTRY.register("green_recon_rig", ReconRigItem::new);

    public static final RegistryObject<Item> BLACK_KEVLAR_CHESTPLATE = REGISTRY.register("black_kevlar_chestplate",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.CHESTPLATE,0));


    public static final RegistryObject<Item> MENU_ITEM = REGISTRY.register("menu_item",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static void init(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
