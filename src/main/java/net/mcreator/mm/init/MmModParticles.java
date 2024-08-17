
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.mcreator.mm.client.particle.SmokeParticleParticle;
import net.mcreator.mm.client.particle.SmokeFadeParticleParticle;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MmModParticles {
	@SubscribeEvent
	public static void registerParticles(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(MmModParticleTypes.SMOKE_FADE_PARTICLE.get(), SmokeFadeParticleParticle::provider);
		event.registerSpriteSet(MmModParticleTypes.SMOKE_PARTICLE.get(), SmokeParticleParticle::provider);
	}
}
