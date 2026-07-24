package net.tkg.ModernMayhem.server.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererNightVisionMixin {

    private static final float MODERNMAYHEM$NVG_BRIGHTNESS = 0.45F;

    private static final float MODERNMAYHEM$TVG_BRIGHTNESS = 0.5F;

    @Inject(method = "getNightVisionScale", at = @At("HEAD"), cancellable = true)
    private static void modernmayhem$goggleWorldBrightness(LivingEntity entity, float partialTicks, CallbackInfoReturnable<Float> cir) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || entity != mc.player) return;
        if (!mc.options.getCameraType().isFirstPerson()) return;
        if (mc.options.hideGui) return;

        ItemStack facewear = CuriosUtil.getFaceWearItem(mc.player);
        if (facewear == null) return;
        if (!GenericSpecialGogglesItem.getNVGCheck(facewear)) return;

        if (GenericSpecialGogglesItem.isNightVision(facewear)) {
            cir.setReturnValue(MODERNMAYHEM$NVG_BRIGHTNESS);
        } else if (GenericSpecialGogglesItem.isThermal(facewear)) {
            cir.setReturnValue(MODERNMAYHEM$TVG_BRIGHTNESS);
        }
    }
}