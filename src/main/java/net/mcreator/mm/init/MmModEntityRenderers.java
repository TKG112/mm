
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MmModEntityRenderers {
	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(MmModEntities.FRAG_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MmModEntities.IMPACT_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MmModEntities.MOLOTOV_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MmModEntities.FLASHBANG_PROJECTILE.get(), ThrownItemRenderer::new);
		event.registerEntityRenderer(MmModEntities.SMOKE_PROJECTILE.get(), ThrownItemRenderer::new);
	}
}
