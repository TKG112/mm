package net.tkg.ModernMayhem.server.registry;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;

public class AttributesRegistryMM {
    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(ForgeRegistries.ATTRIBUTES, ModernMayhemMod.ID);

    public static final RegistryObject<Attribute> SAFE_FALL_DISTANCE = ATTRIBUTES.register("safe_fall_distance",
            () -> new RangedAttribute("attribute.name.generic.safe_fall_distance", 3.0D, -1024.0D, 1024.0D).setSyncable(true));



    public static void init(IEventBus modEventBus) {
        ATTRIBUTES.register(modEventBus);
    }
}