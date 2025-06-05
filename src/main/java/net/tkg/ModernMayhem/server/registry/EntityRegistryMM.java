package net.tkg.ModernMayhem.server.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tkg.ModernMayhem.ModernMayhemMod;

public class EntityRegistryMM {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModernMayhemMod.ID);

    // Put the entity registrations here

    public static void init(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }

}
