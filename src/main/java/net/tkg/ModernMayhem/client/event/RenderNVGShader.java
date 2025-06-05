package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.ShaderRenderer;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import net.tkg.ModernMayhem.server.util.NVGConfigs;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderNVGShader {

    private static final ShaderRenderer NVG_SHADER_RENDERER = new ShaderRenderer(fromNamespaceAndPath(ModernMayhemMod.ID, "shaders/post/night-vision.json"));

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        LocalPlayer player = mc.player;
        if (player == null || !CuriosUtil.hasNVGEquipped(mc.player)) return;

        boolean shouldRender = false;
        GenericNVGGogglesItem.NVGConfig nvgItemConfig = null;
        ItemStack facewearItem = null;
        // Check if the player has an item inheriting from GenericNVGGooglesItem
        // and if it is turned on
        if (CuriosUtil.hasNVGEquipped(player)) {
            facewearItem = CuriosUtil.getFaceWearItem(player);
            if (facewearItem.getItem() instanceof GenericNVGGogglesItem genericNVGGogglesItem) {
                shouldRender = GenericNVGGogglesItem.getNVGMode(CuriosUtil.getFaceWearItem(player)) == 1;
                nvgItemConfig = GenericNVGGogglesItem.getCurrentConfig(facewearItem);
            }
        }
        if (shouldRender && !NVG_SHADER_RENDERER.isActive()) {
            NVG_SHADER_RENDERER.activate();
        } else if (!shouldRender && NVG_SHADER_RENDERER.isActive()) {
            NVG_SHADER_RENDERER.deactivate();
            return; // No need to render if the shader is not active
        }

        if (!NVG_SHADER_RENDERER.isActive()) return;
        boolean isUltraGamer = (facewearItem.getItem() instanceof NVGGogglesItem nvgGogglesItem && ((NVGGogglesItem) facewearItem.getItem()).isGamerNVG());
        NVG_SHADER_RENDERER.render(event.getPartialTick());
        NVG_SHADER_RENDERER.setFloatUniform("Brightness", nvgItemConfig.getBrightness());
        NVG_SHADER_RENDERER.setFloatUniform("RedValue", isUltraGamer ? NVGConfigs.getUltraGamerRedValue() : nvgItemConfig.getRedValue());
        NVG_SHADER_RENDERER.setFloatUniform("GreenValue", isUltraGamer ? NVGConfigs.getUltraGamerGreenValue() : nvgItemConfig.getGreenValue());
        NVG_SHADER_RENDERER.setFloatUniform("BlueValue", isUltraGamer ? NVGConfigs.getUltraGamerBlueValue() : nvgItemConfig.getBlueValue());


    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderNVGOverlay(RenderGuiEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return; // No player, no overlay
        if (!NVG_SHADER_RENDERER.isActive()) return; // Only render overlay if NVG shader is active
        int screenWidth = event.getWindow().getGuiScaledWidth();
        int screenHeight = event.getWindow().getGuiScaledHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
        if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
            if (GenericNVGGogglesItem.getNVGMode(facewearItem) == 1 && Minecraft.getInstance().options.getCameraType().isFirstPerson() && GenericNVGGogglesItem.getCurrentConfig(facewearItem).getOverlay() != null) {
                event.getGuiGraphics().blit(GenericNVGGogglesItem.getCurrentConfig(facewearItem).getOverlay(), 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
            }
        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);

    }
}
