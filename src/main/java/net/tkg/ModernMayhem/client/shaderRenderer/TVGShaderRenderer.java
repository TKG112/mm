package net.tkg.ModernMayhem.client.shaderRenderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.shaderController.TVGShaderController;
import net.tkg.ModernMayhem.client.thermal.render.ThermalRenderer;
import net.tkg.ModernMayhem.server.item.curios.facewear.TVGGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class TVGShaderRenderer extends ShaderRendererBase {
    public static TVGShaderRenderer INSTANCE = new TVGShaderRenderer();

    public TVGShaderRenderer getInstance() { return INSTANCE; }

    private static final float BASE_PHOSPHOR = 0.8f;
    private static final float REF_FPS = 80.0f;
    private static final float CUTOFF_FPS = 12.0f;
    private long lastFrameNanos = 0L;
    private float smoothedDt = 1.0f / REF_FPS;
    private boolean wasActive = false;

    private TVGShaderRenderer() {
        super(ResourceLocation.fromNamespaceAndPath(ModernMayhemMod.ID, "shaders/post/thermal-vision.json"));
    }

    @Override
    public void render() {
        TVGShaderController.recomputeUniforms();
        boolean enabled = TVGShaderController.isEnabled();

        if (enabled && !wasActive) {
            ItemStack face = CuriosUtil.getFaceWearItem(Minecraft.getInstance().player);
            if (face != null && face.getItem() instanceof TVGGogglesItem) {
                ThermalRenderer.setThermalPalette(TVGGogglesItem.getThermalPalette(face));
            }
        }
        wasActive = enabled;

        if (enabled) super.render();
    }

    @Override
    protected Map<String, RenderTarget> getExternalTargets() {
        Map<String, RenderTarget> targets = new HashMap<>();
        RenderTarget blur = ThermalRenderer.getBlurTarget();
        RenderTarget mask = ThermalRenderer.getOccludedMaskTarget();
        if (blur != null) targets.put("thermalBlur", blur);
        if (mask != null) targets.put("thermalMask", mask);
        return targets;
    }

    @Override
    protected void syncUniforms() {
        if (postChain == null) return;

        postChain.setUniform1f("RenderMode",
                ThermalRenderer.getRenderMode() == ThermalRenderer.RenderMode.OVERLAY ? 2.0f : 1.0f);
        postChain.setUniform1f("UseSourceColor",
                ThermalRenderer.isUseSourceColor() ? 1.0f : 0.0f);
        postChain.setUniform4f("OutlineColor",
                ThermalRenderer.getOutlineR(), ThermalRenderer.getOutlineG(),
                ThermalRenderer.getOutlineB(), ThermalRenderer.getOutlineA());
        postChain.setUniform1f("ThermalPalette", (float) ThermalRenderer.getThermalPalette());
        postChain.setUniform1f("DetailStrength", ThermalRenderer.getDetailStrength());

        postChain.setUniform3f("TintColor",
                ThermalRenderer.getWorldTintR(), ThermalRenderer.getWorldTintG(), ThermalRenderer.getWorldTintB());
        postChain.setUniform1f("InvertWorld", ThermalRenderer.isWorldInverted() ? 1.0f : 0.0f);
        postChain.setUniform1f("HandCull", ThermalRenderer.isIrisShaderpackActive() ? 0.0f : 1.0f);

        float phosphor = computePhosphor();
        postChain.setUniform3f("Phosphor", phosphor, phosphor, phosphor);
    }

    private float computePhosphor() {
        long now = System.nanoTime();
        if (lastFrameNanos != 0L) {
            float dt = (now - lastFrameNanos) / 1.0e9f;
            dt = Math.min(Math.max(dt, 1.0e-4f), 0.25f);
            smoothedDt += (dt - smoothedDt) * 0.1f;
        }
        lastFrameNanos = now;

        float fps = 1.0f / smoothedDt;
        if (fps <= CUTOFF_FPS) return 0.0f;
        float factor = (float) Math.pow(BASE_PHOSPHOR, REF_FPS * smoothedDt);
        return Math.max(0.0f, Math.min(BASE_PHOSPHOR, factor));
    }
}