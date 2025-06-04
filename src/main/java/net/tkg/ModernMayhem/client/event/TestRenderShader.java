package net.tkg.ModernMayhem.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.ShaderRenderer;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class TestRenderShader {

    public static final ShaderRenderer NVG_SHADER_RENDERER = new ShaderRenderer(fromNamespaceAndPath(ModernMayhemMod.ID, "shaders/post/night-vision.json"));

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        if (NVG_SHADER_RENDERER.isActive()) {
            NVG_SHADER_RENDERER.render(event.getPartialTick());
            NVG_SHADER_RENDERER.setFloatUniform("GreenValue", 0.0f);
            NVG_SHADER_RENDERER.setFloatUniform("BlueValue", 0.0f);
        }
    }
}
