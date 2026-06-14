package net.tkg.ModernMayhem.client.shaderRenderer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.shaderController.NVGShaderController;
import net.tkg.ModernMayhem.client.thermal.render.ThermalRenderer;

import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public final class NVGShaderRenderer extends ShaderRendererBase {
    public static NVGShaderRenderer INSTANCE = new NVGShaderRenderer();

    public NVGShaderRenderer getInstance() { return INSTANCE; }

    private NVGShaderRenderer() {
        super(ResourceLocation.fromNamespaceAndPath(ModernMayhemMod.ID, "shaders/post/night-vision.json"));
    }

    @Override
    public void render() {
        NVGShaderController.recomputeUniforms();
        if (NVGShaderController.isEnabled()) super.render();
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
        if (this.postChain == null) return;

        this.postChain.setUniform1f("RenderMode",
                ThermalRenderer.getRenderMode() == ThermalRenderer.RenderMode.OVERLAY ? 2.0f : 1.0f);
        this.postChain.setUniform1f("UseSourceColor",
                ThermalRenderer.isUseSourceColor() ? 1.0f : 0.0f);
        this.postChain.setUniform4f("OutlineColor",
                ThermalRenderer.getOutlineR(), ThermalRenderer.getOutlineG(),
                ThermalRenderer.getOutlineB(), ThermalRenderer.getOutlineA());
        this.postChain.setUniform1f("ThermalPalette", (float) ThermalRenderer.getThermalPalette());
        this.postChain.setUniform1f("DetailStrength", ThermalRenderer.getDetailStrength());
        this.postChain.setUniform1f("HandCull", ThermalRenderer.isIrisShaderpackActive() ? 0.0f : 1.0f);

        this.postChain.setUniform1f("AutoGainEnabled", NVGShaderController.getAutoGainEnabled());
        this.postChain.setUniform1f("MinGain", NVGShaderController.getMinGain());
        this.postChain.setUniform1f("MaxGain", NVGShaderController.getMaxGain());
        this.postChain.setUniform1f("AutoGainSpeed", NVGShaderController.getAutoGainSpeed());

        this.postChain.setUniform1f("AutoGatingEnabled", NVGShaderController.getAutoGatingEnabled());
        this.postChain.setUniform1f("AutoGatingOffset", NVGShaderController.getAutoGatingOffset());
        this.postChain.setUniform1f("AutoGatingSpeed", NVGShaderController.getAutoGatingSpeed());

        this.postChain.setUniform1f("NightVisionEnabled", NVGShaderController.isEnabled() ? 1.0f : 0.0f);
        this.postChain.setUniform1f("Brightness", NVGShaderController.getBrightness());
        this.postChain.setUniform1f("RedValue", NVGShaderController.getRedValue());
        this.postChain.setUniform1f("GreenValue", NVGShaderController.getGreenValue());
        this.postChain.setUniform1f("BlueValue", NVGShaderController.getBlueValue());
        this.postChain.setUniform1f("NoiseMultiplier", NVGShaderController.getNoiseMultiplier());
    }
}