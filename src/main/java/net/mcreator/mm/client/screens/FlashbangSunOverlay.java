package net.mcreator.mm.client.screens;

import org.lwjgl.opengl.GL11;

import org.joml.Vector4f;
import org.joml.Vector3f;
import org.joml.Matrix4f;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;

import net.mcreator.mm.procedures.FlashbangOverlayReturnProcedure;
import net.mcreator.mm.entity.FlashbangProjectileEntity;

import com.mojang.blaze3d.systems.RenderSystem;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class FlashbangSunOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		Player player = Minecraft.getInstance().player;
		if (player == null)
			return;
		MobEffectInstance flashEffect = player.getEffect(MobEffects.BLINDNESS); // Assuming you're using Blindness for the Flash effect
		if (flashEffect != null && FlashbangOverlayReturnProcedure.execute(player)) {
			int duration = flashEffect.getDuration();
			// Assuming a fade-out period of 200 ticks (10 seconds)
			float fadeAlpha = duration > 200 ? 1.0f : duration / 200.0f;
			// Get the last explosion position from the FlashbangProjectileEntity
			Vec3 explosionPosVec = FlashbangProjectileEntity.getLastExplosionPosition();
			if (explosionPosVec == null)
				return;
			double flashbangX = explosionPosVec.x;
			double flashbangY = explosionPosVec.y;
			double flashbangZ = explosionPosVec.z;
			double playerX = player.getX();
			double playerY = player.getY() + player.getEyeHeight();
			double playerZ = player.getZ();
			Vector3f explosionPos = new Vector3f((float) (flashbangX - playerX), (float) (flashbangY - playerY), (float) (flashbangZ - playerZ));
			Vector4f screenPos = new Vector4f(explosionPos, 1.0f);
			// Get the projection matrix
			Matrix4f projectionMatrix = Minecraft.getInstance().gameRenderer.getProjectionMatrix(0.05f);
			// Get the view matrix from the main camera
			Matrix4f viewMatrix = Minecraft.getInstance().gameRenderer.getMainCamera().getPositionMatrix();
			// Transform the position into screen space
			screenPos.mul(viewMatrix);
			screenPos.mul(projectionMatrix);
			// Normalize the screen position
			screenPos.div(screenPos.w());
			int screenX = (int) ((screenPos.x() * 0.5f + 0.5f) * event.getWindow().getGuiScaledWidth());
			int screenY = (int) ((1.0f - (screenPos.y() * 0.5f + 0.5f)) * event.getWindow().getGuiScaledHeight());
			RenderSystem.setShader(GameRenderer::getPositionTexShader);
			RenderSystem.setShaderTexture(0, new ResourceLocation("mm", "textures/overlay/flashbang.png"));
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			// Modify the size of the overlay depending on the explosion distance
			double distance = player.distanceToSqr(explosionPosVec);
			int size = Math.max(64, (int) (300 / Math.sqrt(distance)));
			RenderSystem.enableDepthTest();
			RenderSystem.depthFunc(GL11.GL_ALWAYS);
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, fadeAlpha);
			event.getGuiGraphics().blit(new ResourceLocation("mm", "textures/overlay/flashbang.png"), screenX - size / 2, screenY - size / 2, 0, 0, size, size, 256, 256);
			RenderSystem.disableDepthTest();
			RenderSystem.disableBlend();
		}
	}
}
