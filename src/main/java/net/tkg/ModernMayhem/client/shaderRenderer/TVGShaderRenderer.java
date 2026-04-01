package net.tkg.ModernMayhem.client.shaderRenderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.shaderController.TVGShaderController;

@OnlyIn(Dist.CLIENT)
public final class TVGShaderRenderer extends ShaderRendererBase{
    public static TVGShaderRenderer INSTANCE = new TVGShaderRenderer();

    public TVGShaderRenderer getInstance() { return INSTANCE; }

    private TVGShaderRenderer() {
        super(ResourceLocation.fromNamespaceAndPath(ModernMayhemMod.ID, "shaders/post/thermal-vision.json"));
    }

    @Override
    public void render() {
        TVGShaderController.recomputeUniforms();
        if (TVGShaderController.isEnabled()) super.render();
    }

    @Override
    protected void syncUniforms() {

    }
}
