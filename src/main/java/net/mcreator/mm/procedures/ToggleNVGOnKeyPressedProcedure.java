package net.mcreator.mm.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.mcreator.mm.init.MmModItems;

public class ToggleNVGOnKeyPressedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_GPNVG.get(), lv).isPresent() : false) {
			if (entity instanceof LivingEntity lv) {
				CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_GPNVG.get()).forEach(item -> {
					ItemStack itemstackiterator = item.stack();
					if (itemstackiterator.getOrCreateTag().getBoolean("NvgCheck") == false) {
						itemstackiterator.getOrCreateTag().putBoolean("NvgCheck", true);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvon")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvon")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
					} else if (itemstackiterator.getOrCreateTag().getBoolean("NvgCheck") == true) {
						itemstackiterator.getOrCreateTag().putBoolean("NvgCheck", false);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvoff")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvoff")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
					}
				});
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_NVG_21.get(), lv).isPresent() : false) {
			if (entity instanceof LivingEntity lv) {
				CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_NVG_21.get()).forEach(item -> {
					ItemStack itemstackiterator = item.stack();
					if (itemstackiterator.getOrCreateTag().getBoolean("NvgCheck") == false) {
						itemstackiterator.getOrCreateTag().putBoolean("NvgCheck", true);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvon")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvon")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
					} else if (itemstackiterator.getOrCreateTag().getBoolean("NvgCheck") == true) {
						itemstackiterator.getOrCreateTag().putBoolean("NvgCheck", false);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvoff")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvoff")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
					}
				});
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_VISOR.get(), lv).isPresent() : false) {
			if (entity instanceof LivingEntity lv) {
				CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_VISOR.get()).forEach(item -> {
					ItemStack itemstackiterator = item.stack();
					if (itemstackiterator.getOrCreateTag().getBoolean("VisorDown") == false) {
						itemstackiterator.getOrCreateTag().putBoolean("VisorDown", true);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvgturnknob")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvgturnknob")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
					} else if (itemstackiterator.getOrCreateTag().getBoolean("VisorDown") == true) {
						itemstackiterator.getOrCreateTag().putBoolean("VisorDown", false);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvgturnknob")), SoundSource.NEUTRAL, 1, 1);
							} else {
								_level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nvgturnknob")), SoundSource.NEUTRAL, 1, 1, false);
							}
						}
					}
				});
			}
		}
	}
}
