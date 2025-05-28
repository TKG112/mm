package net.tkg.ModernMayhem.server.registry;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.DuffelBagItem;
import net.tkg.ModernMayhem.server.item.NVGGoggleList;
import net.tkg.ModernMayhem.server.item.armor.CustomArmorItem;
import net.tkg.ModernMayhem.server.item.curios.back.BackpackItem;
import net.tkg.ModernMayhem.server.item.curios.body.BandoleerItem;
import net.tkg.ModernMayhem.server.item.curios.body.PlateCarrierItem;
import net.tkg.ModernMayhem.server.item.curios.body.ReconRigItem;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.curios.head.HeadGearItems;
import net.tkg.ModernMayhem.server.util.ArmorProperties;

public class ItemRegistryMM {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, ModernMayhemMod.ID);

    public static final RegistryObject<Item> BLACK_GPNVG = REGISTRY.register("black_gpnvg",
            () -> new NVGGogglesItem(NVGGoggleList.BLACK_GPNVG));
    public static final RegistryObject<Item> BLACK_PVS14 = REGISTRY.register("black_pvs14",
            () -> new NVGGogglesItem(NVGGoggleList.BLACK_PVS14));
    public static final RegistryObject<Item> ULTRA_GAMER_GPNVG = REGISTRY.register("ultra_gamer_gpnvg",
            () -> new NVGGogglesItem(NVGGoggleList.GAMER_GPNVG));
    public static final RegistryObject<Item> TAN_GPNVG = REGISTRY.register("tan_gpnvg",
            () -> new NVGGogglesItem(NVGGoggleList.TAN_GPNVG));
    public static final RegistryObject<Item> TAN_PVS14 = REGISTRY.register("tan_pvs14",
            () -> new NVGGogglesItem(NVGGoggleList.TAN_PVS14));
    public static final RegistryObject<Item> GREEN_PVS14 = REGISTRY.register("green_pvs14",
            () -> new NVGGogglesItem(NVGGoggleList.GREEN_PVS14));
    public static final RegistryObject<Item> BLACK_PVS7 = REGISTRY.register("black_pvs7",
            () -> new NVGGogglesItem(NVGGoggleList.BLACK_PVS7));


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

    public static final RegistryObject<Item> BLACK_COMBAT_HELMET = REGISTRY.register("black_combat_helmet",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.HELMET,0, true));
    public static final RegistryObject<Item> BLACK_KEVLAR_CHESTPLATE = REGISTRY.register("black_kevlar_chestplate",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.CHESTPLATE,0));
    public static final RegistryObject<Item> BLACK_KEVLAR_LEGGINGS = REGISTRY.register("black_kevlar_leggings",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.LEGGINGS,0));
    public static final RegistryObject<Item> BLACK_KEVLAR_BOOTS = REGISTRY.register("black_kevlar_boots",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.BOOTS,0));
    public static final RegistryObject<Item> GREEN_SSH68_HELMET = REGISTRY.register("green_ssh68_helmet",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.HELMET,1, true));
    public static final RegistryObject<Item> GREEN_KEVLAR_CHESTPLATE = REGISTRY.register("green_kevlar_chestplate",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.CHESTPLATE,1));
    public static final RegistryObject<Item> GREEN_KEVLAR_LEGGINGS = REGISTRY.register("green_kevlar_leggings",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.LEGGINGS,1));
    public static final RegistryObject<Item> GREEN_KEVLAR_BOOTS = REGISTRY.register("green_kevlar_boots",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.BOOTS,1));
    public static final RegistryObject<Item> TAN_COMBAT_HELMET = REGISTRY.register("tan_combat_helmet",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.HELMET,2, true));
    public static final RegistryObject<Item> TAN_KEVLAR_CHESTPLATE = REGISTRY.register("tan_kevlar_chestplate",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.CHESTPLATE,2));
    public static final RegistryObject<Item> TAN_KEVLAR_LEGGINGS = REGISTRY.register("tan_kevlar_leggings",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.LEGGINGS,2));
    public static final RegistryObject<Item> TAN_KEVLAR_BOOTS = REGISTRY.register("tan_kevlar_boots",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.BOOTS,2));
    public static final RegistryObject<Item> YELLOW_HAZMAT_HELMET = REGISTRY.register("yellow_hazmat_helmet",
            () -> new CustomArmorItem(ArmorProperties.HAZMAT, ArmorItem.Type.HELMET,0));
    public static final RegistryObject<Item> YELLOW_HAZMAT_CHESTPLATE = REGISTRY.register("yellow_hazmat_chestplate",
            () -> new CustomArmorItem(ArmorProperties.HAZMAT, ArmorItem.Type.CHESTPLATE,0));
    public static final RegistryObject<Item> YELLOW_HAZMAT_LEGGINGS = REGISTRY.register("yellow_hazmat_leggings",
            () -> new CustomArmorItem(ArmorProperties.HAZMAT, ArmorItem.Type.LEGGINGS,0));
    public static final RegistryObject<Item> ORANGE_HAZMAT_HELMET = REGISTRY.register("orange_hazmat_helmet",
            () -> new CustomArmorItem(ArmorProperties.HAZMAT, ArmorItem.Type.HELMET,1));
    public static final RegistryObject<Item> ORANGE_HAZMAT_CHESTPLATE = REGISTRY.register("orange_hazmat_chestplate",
            () -> new CustomArmorItem(ArmorProperties.HAZMAT, ArmorItem.Type.CHESTPLATE,1));
    public static final RegistryObject<Item> ORANGE_HAZMAT_LEGGINGS = REGISTRY.register("orange_hazmat_leggings",
            () -> new CustomArmorItem(ArmorProperties.HAZMAT, ArmorItem.Type.LEGGINGS,1));
    public static final RegistryObject<Item> BLACK_HEAD_MOUNT = REGISTRY.register("black_head_mount",
            () -> new CustomArmorItem(ArmorProperties.NOTHING, ArmorItem.Type.HELMET,0, true));
    public static final RegistryObject<Item> BLACK_RONIN = REGISTRY.register("black_ronin",
            () -> new CustomArmorItem(ArmorProperties.RONIN, ArmorItem.Type.HELMET,0, true));

    public static final RegistryObject<Item> BLACK_IOLA = REGISTRY.register("black_iola",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.LEGGINGS,3));
    public static final RegistryObject<Item> GREEN_IOLA = REGISTRY.register("green_iola",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.LEGGINGS,4));
    public static final RegistryObject<Item> TAN_IOLA = REGISTRY.register("tan_iola",
            () -> new CustomArmorItem(ArmorProperties.KEVLAR, ArmorItem.Type.LEGGINGS,5));


    public static final RegistryObject<Item> BALACLAVA = REGISTRY.register("balaclava",
            () -> new HeadGearItems(0, 0));
    public static final RegistryObject<Item> BLACK_GLASSES = REGISTRY.register("black_glasses",
            () -> new HeadGearItems(1, 0));
    public static final RegistryObject<Item> BLACK_GOGGLES = REGISTRY.register("black_goggles",
            () -> new HeadGearItems(2, 0));
    public static final RegistryObject<Item> BLACK_HEADSET = REGISTRY.register("black_headset",
            () -> new HeadGearItems(3, 0));
    public static final RegistryObject<Item> BLACK_MILITARY_BALACLAVA = REGISTRY.register("black_military_balaclava",
            () -> new HeadGearItems(4, 0));


    public static final RegistryObject<Item> DUFFEL_BAG = REGISTRY.register("duffel_bag",
            () -> new DuffelBagItem(new Item.Properties().stacksTo(1), 256));


    public static final RegistryObject<Item> WHITE_PHOSPHOR = REGISTRY.register("white_phosphor",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> GREEN_PHOSPHOR = REGISTRY.register("green_phosphor",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> RED_PHOSPHOR = REGISTRY.register("red_phosphor",
            () -> new Item(new Item.Properties().stacksTo(64)));


    public static final RegistryObject<Item> MENU_ITEM = REGISTRY.register("menu_item",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static void init(IEventBus modEventBus) {
        REGISTRY.register(modEventBus);
    }
}
