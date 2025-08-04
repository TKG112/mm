package net.tkg.ModernMayhem.server.attribute;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.registry.AttributesRegistryMM;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AttributeAttacher {

    @SubscribeEvent
    public static void onEntityAttributeModification(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, AttributesRegistryMM.SAFE_FALL_DISTANCE.get());
    }

}
