package net.tkg.ModernMayhem.client.shaderRenderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.shaderController.NVGShaderController;

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
    protected void syncUniforms() {
        // Since both "mm:autogain" and "mm:night-vision" have the uniform "AutoGainEnabled", changing its value affect both (so no need to do it for each shader separately)
        this.postChain.setUniform1f("AutoGainEnabled", NVGShaderController.getAutoGainEnabled());
        this.postChain.setUniform1f("MinGain", NVGShaderController.getMinGain());
        this.postChain.setUniform1f("MaxGain", NVGShaderController.getMaxGain());
        this.postChain.setUniform1f("AutoGainSpeed", NVGShaderController.getAutoGainSpeed());

        // The same goes for "AutoGatingEnabled" in "mm:autogating" and "mm:night-vision"
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
