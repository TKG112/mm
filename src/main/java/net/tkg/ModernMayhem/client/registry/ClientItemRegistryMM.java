package net.tkg.ModernMayhem.client.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;

@OnlyIn(Dist.CLIENT)
public class ClientItemRegistryMM {
    public static final DeferredRegister<Item> CLIENT_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModernMayhemMod.ID);

    public static final RegistryObject<Item> FIRST_PERSON_NVG = CLIENT_ITEMS.register("first_person_nvg",
            NVGFirstPersonFakeItem::new);

    public static void init(IEventBus modEventBus) {
        CLIENT_ITEMS.register(modEventBus);
    }
}