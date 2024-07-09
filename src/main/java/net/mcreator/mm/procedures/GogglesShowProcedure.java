package net.mcreator.mm.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.Minecraft;

import net.mcreator.mm.network.MmModVariables;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GogglesShowProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player.level(), event.player);
		}
	}

	public static void execute(LevelAccessor world, Entity entity) {
		execute(null, world, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (world.isClientSide() && entity instanceof Player) {
			if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).NGV_Black_Shader) {
				if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).NVG_Black_Tube_Gain == 0) {
					if (Minecraft.getInstance().gameRenderer.currentEffect() == null) {
						Minecraft.getInstance().gameRenderer.togglePostEffect();
						Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation("minecraft:shaders/post/night-vision-wp-0.json"));
					} else if (!Minecraft.getInstance().gameRenderer.currentEffect().getName().equals("minecraft:shaders/post/night-vision-wp-0.json")) {
						Minecraft.getInstance().gameRenderer.shutdownEffect();
					}
				}
				if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).NVG_Black_Tube_Gain == 1) {
					if (Minecraft.getInstance().gameRenderer.currentEffect() == null) {
						Minecraft.getInstance().gameRenderer.togglePostEffect();
						Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation("minecraft:shaders/post/night-vision-wp-1.json"));
					} else if (!Minecraft.getInstance().gameRenderer.currentEffect().getName().equals("minecraft:shaders/post/night-vision-wp-1.json")) {
						Minecraft.getInstance().gameRenderer.shutdownEffect();
					}
				}
				if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).NVG_Black_Tube_Gain == 2) {
					if (Minecraft.getInstance().gameRenderer.currentEffect() == null) {
						Minecraft.getInstance().gameRenderer.togglePostEffect();
						Minecraft.getInstance().gameRenderer.loadEffect(new ResourceLocation("minecraft:shaders/post/night-vision-wp-2.json"));
					} else if (!Minecraft.getInstance().gameRenderer.currentEffect().getName().equals("minecraft:shaders/post/night-vision-wp-2.json")) {
						Minecraft.getInstance().gameRenderer.shutdownEffect();
					}
				}
			} else {
				if (Minecraft.getInstance().gameRenderer.currentEffect() != null) {
					Minecraft.getInstance().gameRenderer.shutdownEffect();
				}
			}
		}
	}
}
