package net.mcreator.mm.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;

import net.mcreator.mm.network.MmModVariables;
import net.mcreator.mm.init.MmModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class GogglesShaderProcedure {
	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			execute(event, event.player);
		}
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (Minecraft.getInstance().options.getCameraType().isFirstPerson()
				&& ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).NVG_Black_Check == true && entity instanceof LivingEntity lv
						? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.NVG.get(), lv).isPresent()
						: false)) {
			{
				boolean _setval = true;
				entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.NGV_Black_Shader = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		} else if (Minecraft.getInstance().options.getCameraType().isFirstPerson()
				&& ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).NVG_Black_Check == false && entity instanceof LivingEntity lv
						? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.NVG.get(), lv).isPresent()
						: false)) {
			{
				boolean _setval = false;
				entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.NGV_Black_Shader = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		} else if (!(entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.NVG.get(), lv).isPresent() : false)) {
			{
				boolean _setval = false;
				entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.NGV_Black_Shader = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		} else {
			{
				boolean _setval = false;
				entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.NGV_Black_Shader = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
		}
	}
}
