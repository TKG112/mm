
package net.mcreator.mm.client.screens;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;

import net.mcreator.mm.procedures.FlashbangOverlayReturnProcedure;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class FlashbangSunOverlay {
	private static long lastEffectEndTime = 0;
	private static boolean isEffectActive = false;

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getWindow().getGuiScaledWidth();
		int h = event.getWindow().getGuiScaledHeight();
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			boolean currentlyActive = FlashbangOverlayReturnProcedure.execute(entity);
			if (currentlyActive) {
				isEffectActive = true;
			} else {
				if (isEffectActive) {
					lastEffectEndTime = System.currentTimeMillis();
					isEffectActive = false;
				}
			}
			long currentTime = System.currentTimeMillis();
			float alpha = 0.0f;
			if (isEffectActive) {
				alpha = 1.0f;
			} else if (currentTime - lastEffectEndTime < 2000) {
				alpha = 1.0f - ((currentTime - lastEffectEndTime) / 2000.0f);
			}
			if (alpha > 0.0f) {
				Level world = entity.level();
				double x = entity.getX();
				double y = entity.getY();
				double z = entity.getZ();
				RenderSystem.disableDepthTest();
				RenderSystem.depthMask(false);
				RenderSystem.enableBlend();
				RenderSystem.setShader(GameRenderer::getPositionTexShader);
				RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
				RenderSystem.setShaderColor(1, 1, 1, alpha);
				event.getGuiGraphics().blit(new ResourceLocation("mm:textures/screens/flashbang.png"), w / 2 + -51, h / 2 + -55, 0, 0, 100, 120, 100, 120);
				RenderSystem.depthMask(true);
				RenderSystem.defaultBlendFunc();
				RenderSystem.enableDepthTest();
				RenderSystem.disableBlend();
				RenderSystem.setShaderColor(1, 1, 1, 1);
			}
		}
	}
}
