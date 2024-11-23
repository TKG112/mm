package net.tkg.ModernMayhem.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.item.GenericNVGGogglesItem;
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
                    passe.getEffect().safeGetUniform("NightVisionEnabled").set(nightVisionEnabled);
                    passe.getEffect().safeGetUniform("Brightness").set(config.getBrightness());
                    passe.getEffect().safeGetUniform("RedValue").set(config.getRedValue());
                    passe.getEffect().safeGetUniform("BlueValue").set(config.getBlueValue());
                    passe.getEffect().safeGetUniform("GreenValue").set(config.getGreenValue());
                }
            }
        } catch (Exception e) {
            ModernMayhemMod.LOGGER.warn("Failed to update night vision shader uniforms : ", e);
        }
    }
}