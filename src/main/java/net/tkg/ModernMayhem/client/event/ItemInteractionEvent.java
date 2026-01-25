package net.tkg.ModernMayhem.client.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.PotionItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.config.CommonConfig;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

public class ItemInteractionEvent {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(ItemInteractionEvent.class);
    }

    /// Prevent eating and drinking with NVG on face if config is set to false
    @SubscribeEvent
    public static void onRightClickItem(LivingEntityUseItemEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) return; // Only care about players
        if (!CuriosUtil.hasNVGEquipped(player)) return; // Only care if the player has NVG equipped
        ItemStack stack = CuriosUtil.getFaceWearItem(player); // Get the facewear item from curios
        ItemStack eventItem = event.getItem();
        if (!(eventItem.isEdible() || eventItem.getItem() instanceof MilkBucketItem || eventItem.getItem() instanceof PotionItem)) return; // Only care if the item is edible, milk, or potion
        if (GenericSpecialGogglesItem.isNVGOnFace(stack) && !CommonConfig.CAN_EAT_WITH_FACEWEAR_DOWN.get()) {
            event.setCanceled(true);
        }
        ModernMayhemMod.LOGGER.info("Item is edible: {}, player can eat with facewear down: {}, nvg on face: {}", event.getItem().isEdible(), CommonConfig.CAN_EAT_WITH_FACEWEAR_DOWN.get(), GenericSpecialGogglesItem.isNVGOnFace(stack));
    }
}
