package net.tkg.ModernMayhem.server.config;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.body.BandoleerItem;
import net.tkg.ModernMayhem.server.item.curios.body.HexagonRigItem;
import net.tkg.ModernMayhem.server.item.curios.body.PlateCarrierItem;
import net.tkg.ModernMayhem.server.item.curios.body.ReconRigItem;
import net.tkg.ModernMayhem.server.item.curios.facewear.VisorItem; // [FIX] Import
import net.tkg.ModernMayhem.server.item.generic.GenericStatConfigurableArmorItem;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Map;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigReloadHandler {

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getModId().equals(ModernMayhemMod.ID)) {
            updatePlayerArmorStats();
        }
    }

    private static void updatePlayerArmorStats() {
        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            updateStandardArmor(player);
            updateCuriosItems(player);
        }
    }

    private static void updateStandardArmor(ServerPlayer player) {
        AttributeMap attributeMap = player.getAttributes();
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ARMOR) {
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.getItem() instanceof GenericStatConfigurableArmorItem) {
                    var modifiers = stack.getAttributeModifiers(slot);
                    attributeMap.removeAttributeModifiers(modifiers);
                    attributeMap.addTransientAttributeModifiers(modifiers);
                }
            }
        }
    }

    private static void updateCuriosItems(ServerPlayer player) {
        CuriosApi.getCuriosHelper().getCuriosHandler(player).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();

            curios.forEach((identifier, stacksHandler) -> {
                IDynamicStackHandler stackHandler = stacksHandler.getStacks();
                for (int i = 0; i < stackHandler.getSlots(); i++) {
                    ItemStack stack = stackHandler.getStackInSlot(i);

                    if (!stack.isEmpty() && isConfigurableCurio(stack)) {
                        ItemStack copy = stack.copy();
                        stackHandler.setStackInSlot(i, ItemStack.EMPTY);
                        stackHandler.setStackInSlot(i, copy);
                    }
                }
            });
        });
    }

    private static boolean isConfigurableCurio(ItemStack stack) {
        return stack.getItem() instanceof ReconRigItem
                || stack.getItem() instanceof BandoleerItem
                || stack.getItem() instanceof PlateCarrierItem
                || stack.getItem() instanceof HexagonRigItem
                || stack.getItem() instanceof VisorItem;
    }
}