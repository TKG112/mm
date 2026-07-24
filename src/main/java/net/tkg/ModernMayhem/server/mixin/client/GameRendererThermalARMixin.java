package net.tkg.ModernMayhem.server.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import net.tkg.ModernMayhem.client.compat.ar.ARCompat;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererThermalARMixin {

    @Unique
    private boolean modernmayhem$pushedArVanillaPipeline;

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void modernmayhem$forceVanillaEntityPipelineForThermal(float partialTick, long nanos, PoseStack poseStack, CallbackInfo ci) {
        this.modernmayhem$pushedArVanillaPipeline = false;
        if (ARCompat.isLoaded() && modernmayhem$thermalActive()) {
            ARCompat.useVanillaEntityPipeline();
            this.modernmayhem$pushedArVanillaPipeline = true;
        }
    }

    @Inject(method = "renderLevel", at = @At("RETURN"))
    private void modernmayhem$restoreEntityPipelineAfterThermal(float partialTick, long nanos, PoseStack poseStack, CallbackInfo ci) {
        if (this.modernmayhem$pushedArVanillaPipeline) {
            ARCompat.resetEntityPipeline();
            this.modernmayhem$pushedArVanillaPipeline = false;
        }
    }

    @Unique
    private boolean modernmayhem$thermalActive() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return false;
        if (!mc.options.getCameraType().isFirstPerson()) return false;
        ItemStack facewear = CuriosUtil.getFaceWearItem(mc.player);
        return facewear != null
                && GenericSpecialGogglesItem.isThermal(facewear)
                && GenericSpecialGogglesItem.getNVGCheck(facewear);
    }
}
