package net.tkg.ModernMayhem.server.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.CotiItem;
import net.tkg.ModernMayhem.server.item.DuffelBagItem;

public class ItemRegistryMM {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModernMayhemMod.ID);

    public static final RegistryObject<Item> DUFFEL_BAG = ITEMS.register("duffel_bag",
            () -> new DuffelBagItem(new Item.Properties().stacksTo(1), 256));


    public static final RegistryObject<Item> COTI = ITEMS.register("coti",
            () -> new CotiItem(new Item.Properties().stacksTo(1)));


    public static final RegistryObject<Item> WHITE_PHOSPHOR = ITEMS.register("white_phosphor",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> GREEN_PHOSPHOR = ITEMS.register("green_phosphor",
            () -> new Item(new Item.Properties().stacksTo(64)));
    public static final RegistryObject<Item> RED_PHOSPHOR = ITEMS.register("red_phosphor",
            () -> new Item(new Item.Properties().stacksTo(64)));


    public static final RegistryObject<Item> MENU_ITEM = ITEMS.register("menu_item",
            () -> new Item(new Item.Properties().stacksTo(1)));

    public static void init(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
