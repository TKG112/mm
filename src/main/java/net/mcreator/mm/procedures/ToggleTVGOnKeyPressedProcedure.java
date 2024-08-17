package net.mcreator.mm.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.mm.network.MmModVariables;

public class ToggleTVGOnKeyPressedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).TVG_Check == false) {
			{
				boolean _setval = true;
				entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.TVG_Check = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvon")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvon")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
		} else if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).TVG_Check == true) {
			{
				boolean _setval = false;
				entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
					capability.TVG_Check = _setval;
					capability.syncPlayerVariables(entity);
				});
			}
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvoff")), SoundSource.NEUTRAL, 1, 1);
				} else {
					_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvoff")), SoundSource.NEUTRAL, 1, 1, false);
				}
			}
		}
	}
}
