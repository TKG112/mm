package net.tkg.ModernMayhem.client.event;


import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.ShaderRenderer;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderTestShader {
    public static final ShaderRenderer TEST_SHADER_RENDERER = new ShaderRenderer(fromNamespaceAndPath(ModernMayhemMod.ID, "shaders/post/test_shader.json"));

    @SubscribeEvent
    public static void afterLevelRender(RenderLevelStageEvent event) {
        TEST_SHADER_RENDERER.render();
    }
}
