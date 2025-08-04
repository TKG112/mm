package net.tkg.ModernMayhem.server.attribute;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.registry.AttributesRegistryMM;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SafeFallDistanceAttribute {
    @SubscribeEvent
    public static void onFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        AttributeInstance attribute = player.getAttribute(AttributesRegistryMM.SAFE_FALL_DISTANCE.get());
        if (attribute == null) return;

        double safeFall = attribute.getValue() - 3.0D;
        float distance = event.getDistance();

        if (distance <= safeFall) {
            event.setCanceled(true);
        } else {
            float newDistance = (float)(distance - safeFall);
            event.setDistance(newDistance);
        }
    }
}
