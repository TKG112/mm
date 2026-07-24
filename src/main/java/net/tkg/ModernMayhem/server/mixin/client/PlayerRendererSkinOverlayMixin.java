package net.tkg.ModernMayhem.server.mixin.client;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.content.item.DataArmorItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererSkinOverlayMixin {

    @Inject(method = "setModelProperties", at = @At("TAIL"))
    private void modernmayhem$hideSkinOverlayUnderArmor(AbstractClientPlayer player, CallbackInfo ci) {
        PlayerModel<AbstractClientPlayer> model = ((PlayerRenderer) (Object) this).getModel();

        if (modernmayhem$hidesOverlay(player, EquipmentSlot.HEAD)) {
            model.hat.visible = false;
        }
        if (modernmayhem$hidesOverlay(player, EquipmentSlot.CHEST)) {
            model.jacket.visible = false;
            model.leftSleeve.visible = false;
            model.rightSleeve.visible = false;
        }

        if (modernmayhem$hidesOverlay(player, EquipmentSlot.LEGS)
                || modernmayhem$hidesOverlay(player, EquipmentSlot.FEET)) {
            model.leftPants.visible = false;
            model.rightPants.visible = false;
        }
    }

    @Unique
    private static boolean modernmayhem$hidesOverlay(AbstractClientPlayer player, EquipmentSlot slot) {
        ItemStack stack = player.getItemBySlot(slot);
        return stack.getItem() instanceof DataArmorItem armor && armor.definition().hidesSkinOverlay();
    }
}
