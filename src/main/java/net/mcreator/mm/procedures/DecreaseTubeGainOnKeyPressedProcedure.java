package net.mcreator.mm.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.mm.network.MmModVariables;
import net.mcreator.mm.init.MmModItems;

public class DecreaseTubeGainOnKeyPressedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.NVG.get(), lv).isPresent() : false) {
			if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).NVG_Black_Tube_Gain == 2) {
				{
					double _setval = 1;
					entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.NVG_Black_Tube_Gain = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvgturnknob")), SoundSource.NEUTRAL, (float) 0.05,
								(float) (Mth.nextDouble(RandomSource.create(), 8, 12) * 0.1));
					} else {
						_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvgturnknob")), SoundSource.NEUTRAL, (float) 0.05,
								(float) (Mth.nextDouble(RandomSource.create(), 8, 12) * 0.1), false);
					}
				}
			} else if ((entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new MmModVariables.PlayerVariables())).NVG_Black_Tube_Gain == 1) {
				{
					double _setval = 0;
					entity.getCapability(MmModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
						capability.NVG_Black_Tube_Gain = _setval;
						capability.syncPlayerVariables(entity);
					});
				}
				if (world instanceof Level _level) {
					if (!_level.isClientSide()) {
						_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvgturnknob")), SoundSource.NEUTRAL, (float) 0.05,
								(float) (Mth.nextDouble(RandomSource.create(), 8, 12) * 0.1));
					} else {
						_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvgturnknob")), SoundSource.NEUTRAL, (float) 0.05,
								(float) (Mth.nextDouble(RandomSource.create(), 8, 12) * 0.1), false);
					}
				}
			}
		}
	}
}
