package net.mcreator.mm.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.mm.network.MmModVariables;
import net.mcreator.mm.init.MmModItems;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class TubeGainChangeProcedure {
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
		if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_GPNVG.get(), lv).isPresent() : false) {
			if (entity instanceof LivingEntity lv) {
				CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_GPNVG.get()).forEach(item -> {
					ItemStack itemstackiterator = item.stack();
					if (itemstackiterator.getOrCreateTag().getDouble("TubeGain") == 0) {
						{
							double _setval = 0;
							entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.Black_GPNVG_Tube_Gain = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
					} else if (itemstackiterator.getOrCreateTag().getDouble("TubeGain") == 1) {
						{
							double _setval = 1;
							entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.Black_GPNVG_Tube_Gain = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
					} else if (itemstackiterator.getOrCreateTag().getDouble("TubeGain") == 2) {
						{
							double _setval = 2;
							entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.Black_GPNVG_Tube_Gain = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
					}
				});
			}
		}
		if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_NVG_21.get(), lv).isPresent() : false) {
			if (entity instanceof LivingEntity lv) {
				CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_NVG_21.get()).forEach(item -> {
					ItemStack itemstackiterator = item.stack();
					if (itemstackiterator.getOrCreateTag().getDouble("TubeGain") == 0) {
						{
							double _setval = 0;
							entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.Black_NVG21_Tube_Gain = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
					} else if (itemstackiterator.getOrCreateTag().getDouble("TubeGain") == 1) {
						{
							double _setval = 1;
							entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.Black_NVG21_Tube_Gain = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
					} else if (itemstackiterator.getOrCreateTag().getDouble("TubeGain") == 2) {
						{
							double _setval = 2;
							entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
								capability.Black_NVG21_Tube_Gain = _setval;
								capability.syncPlayerVariables(entity);
							});
						}
					}
				});
			}
		}
	}
}
