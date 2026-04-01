package net.tkg.ModernMayhem.client.shaderController;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.facewear.TVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

@OnlyIn(Dist.CLIENT)
public final class TVGShaderController {

    private static final Minecraft MC = Minecraft.getInstance();

    private static boolean enabled = false;

    //Add uniform states here if needed in the future

    public static boolean isEnabled() { return enabled; }

    public static void recomputeUniforms() {
        LocalPlayer player = MC.player;
        if (player == null) return;

        boolean shouldRender = false;
        ItemStack facewearItem;

        if (CuriosUtil.hasNVGEquipped(player)) {
            facewearItem = CuriosUtil.getFaceWearItem(player);
            if (facewearItem.getItem() instanceof TVGGogglesItem thermalItem) {
                if (thermalItem.shouldRenderShader()) {
                    shouldRender = GenericSpecialGogglesItem.getNVGMode(facewearItem) == 1;
                }
            }
        }

        if (shouldRender && !isEnabled()) {
            enabled = true;
        } else if (!shouldRender && isEnabled()) {
            enabled = false;
        }
    }
}
