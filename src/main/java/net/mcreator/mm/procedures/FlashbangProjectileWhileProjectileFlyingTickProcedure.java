package net.mcreator.mm.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.client.Minecraft;

import net.mcreator.mm.init.MmModMobEffects;
import net.mcreator.mm.init.MmModItems;

import java.util.UUID;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;

public class FlashbangProjectileWhileProjectileFlyingTickProcedure {
	@Mod.EventBusSubscriber
	private static class FlashHandler {
		private static final Map<UUID, Long> FlashEffects = new HashMap<>();

		@SubscribeEvent
		public static void onServerTick(TickEvent.ServerTickEvent event) {
			if (!event.phase.equals(TickEvent.Phase.END))
				return;
			Iterator<Map.Entry<UUID, Long>> iterator = FlashEffects.entrySet().iterator();
			long currentTime = System.currentTimeMillis();
			while (iterator.hasNext()) {
				Map.Entry<UUID, Long> entry = iterator.next();
				if (currentTime >= entry.getValue()) {
					iterator.remove();
					Player player = Minecraft.getInstance().level.getPlayerByUUID(entry.getKey());
				}
			}
		}
	}

	public static void execute(Level world, double x, double y, double z, Entity entity) {
		if (world instanceof ServerLevel) {
			world.playSound(null, x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:fb-explosion")), SoundSource.NEUTRAL, 1, 1);
			if (world instanceof ServerLevel _level) {
				_level.sendParticles(ParticleTypes.FLASH, x, y, z, 1, 0, 0, 0, 0);
			}
			if (entity instanceof Player player) {
				double distance = Math.sqrt(player.distanceToSqr(x, y, z)); // Calculate distance
				if (distance < 16) { // Only apply the effect if the player is within 16 blocks
					int duration = (int) Math.max(1, 15 - (distance - 1)); // Calculate duration with dynamic distance system
					playEarRingingSound(player, 16 - (int) distance, duration, entity); // Pass the duration to the playEarRingingSound method
					Vec3 playerViewVec = player.getViewVector(1.0F).normalize();
					Vec3 toPosVec = new Vec3(x - player.getX(), y - player.getEyeY(), z - player.getZ()).normalize();
					double dotProduct = playerViewVec.dot(toPosVec);
					if (dotProduct > 0.5) { // Check if player is looking towards the point
						Vec3 startVec = new Vec3(player.getX(), player.getEyeY(), player.getZ());
						Vec3 endVec = new Vec3(x, y, z);
						ClipContext context = new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
						if (world.clip(context).getType() == HitResult.Type.BLOCK) {
							return; // There is a block in the way, so do not apply the effect
						}
						if (duration > 0) {
							if (!((entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.MILITARY_GLASSES.get(), lv).isPresent() : false) || (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.MILITARY_GOGGLES.get(), lv).isPresent() : false))) {
								player.addEffect(new MobEffectInstance(MmModMobEffects.FLASH.get(), duration * 20, 0, false, false)); // duration in ticks
							}
						}
					}
				}
			}
		}
	}

	private static void playEarRingingSound(Player player, int soundIndex, int duration, Entity entity) {
		if (!(entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.MILITARY_HEADSET.get(), lv).isPresent() : false)) {
			player.addEffect(new MobEffectInstance(MmModMobEffects.EAR_RING.get(), duration * 20, 0, false, false)); // duration in ticks
		}
	}

	static {
		MinecraftForge.EVENT_BUS.register(FlashHandler.class);
	}
}
