package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.mixinaccessor.PostChainAccess;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class RenderNVG {
    private static final ResourceLocation NVG_SHADER_PATH = new ResourceLocation(ModernMayhemMod.ID, "shaders/post/night-vision.json");
    private static int lastWidth = -1;
    private static int lastHeight = -1;
    public static PostPass passe = null;

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void renderNVGOverlay(RenderGuiEvent.Pre event) {
        int screenWidth = event.getWindow().getGuiScaledWidth();
        int screenHeight = event.getWindow().getGuiScaledHeight();
        Player player = Minecraft.getInstance().player;

        if (player != null) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            AtomicBoolean nvgOn = new AtomicBoolean(false);
            AtomicReference<ResourceLocation> overlayLocation = new AtomicReference<>(null);
            CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
                ICurioStacksHandler facewearCurio = curiosInventory.getCurios().get("facewear");
                if (facewearCurio != null) {
                    ItemStack nvgItem = (facewearCurio.getStacks().getStackInSlot(0));
                    if (nvgItem.getItem() instanceof GenericNVGGogglesItem genericNVGGogglesItem) {
                        nvgOn.set(GenericNVGGogglesItem.getNVGMode(nvgItem) == 1);
                        overlayLocation.set(genericNVGGogglesItem.getConfig().getOverlay());
                    }
                }
            });
            if (nvgOn.get() && overlayLocation.get() != null) {
                event.getGuiGraphics().blit(overlayLocation.get(), 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);
            }
            RenderSystem.depthMask(true);
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1, 1, 1, 1);
        }
    }


    @SubscribeEvent
    public static void renderNVGEffect(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) {
            return;
        }

        AtomicBoolean hasNvgItem = new AtomicBoolean(false);
        AtomicBoolean nvgOn = new AtomicBoolean(false);
        AtomicReference<GenericNVGGogglesItem.NVGConfig> nvgItemConfig = new AtomicReference<>(null);
        // Check if the player has an item inheriting from GenericNVGGooglesItem
        // and if it is turned on
        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            ICurioStacksHandler facewearCurio = curiosInventory.getCurios().get("facewear");
            if (facewearCurio != null) {
                ItemStack nvgItem = (facewearCurio.getStacks().getStackInSlot(0));
                if (nvgItem.getItem() instanceof GenericNVGGogglesItem genericNVGGogglesItem) {
                    hasNvgItem.set(true);
                    nvgOn.set(GenericNVGGogglesItem.getNVGMode(nvgItem) == 1);
                    nvgItemConfig.set(genericNVGGogglesItem.getConfig());
                }
            }
        });

        float nightVisionEnabled = (hasNvgItem.get() && nvgOn.get()) ? 1.0f : 0.0f;
        if (mc.gameRenderer.currentEffect() == null || !Objects.requireNonNull(mc.gameRenderer.currentEffect()).getName().equals(NVG_SHADER_PATH.toString())) {
            if (mc.options.getCameraType().isFirstPerson()) {
                initializeShader(mc);
            }
        }

        if (mc.gameRenderer.currentEffect() != null && Objects.requireNonNull(mc.gameRenderer.currentEffect()).getName().equals(NVG_SHADER_PATH.toString())) {
            updateShaderUniforms(nightVisionEnabled, nvgItemConfig.get());
        }
    }

    private static void initializeShader(Minecraft mc) {
        try {
            lastWidth = mc.getWindow().getWidth();
            lastHeight = mc.getWindow().getHeight();
            mc.gameRenderer.loadEffect(NVG_SHADER_PATH);
            Objects.requireNonNull(mc.gameRenderer.currentEffect()).resize(lastWidth, lastHeight);
            passe = ((PostChainAccess) Objects.requireNonNull(mc.gameRenderer.currentEffect())).test_master$getPasses().get(0);
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.error("Failed to load night vision shader : ", e);
        }
    }

    private static void updateShaderUniforms(float nightVisionEnabled, GenericNVGGogglesItem.NVGConfig config) {
        Minecraft mc = Minecraft.getInstance();
        try {
            int width = mc.getWindow().getWidth();
            int height = mc.getWindow().getHeight();
            if ((width != lastWidth || height != lastHeight) && mc.gameRenderer.currentEffect() != null) {
                lastWidth = width;
                lastHeight = height;
                Objects.requireNonNull(mc.gameRenderer.currentEffect()).resize(lastWidth, lastHeight);
            }
            if (passe != null) {
                if (passe.getEffect().getUniform("NightVisionEnabled") != null) {
                    if (nightVisionEnabled > 0.0f) {
                        passe.getEffect().safeGetUniform("NightVisionEnabled").set(nightVisionEnabled);
                        passe.getEffect().safeGetUniform("Brightness").set(config.getBrightness());
                        passe.getEffect().safeGetUniform("RedValue").set(config.getRedValue());
                        passe.getEffect().safeGetUniform("BlueValue").set(config.getBlueValue());
                        passe.getEffect().safeGetUniform("GreenValue").set(config.getGreenValue());
                    } else {
                        passe.getEffect().safeGetUniform("NightVisionEnabled").set(nightVisionEnabled);
                        passe.getEffect().safeGetUniform("Brightness").set(1.0f);
                    }
                }
            }
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.warn("Failed to update night vision shader uniforms : ", e);
        }
    }
}