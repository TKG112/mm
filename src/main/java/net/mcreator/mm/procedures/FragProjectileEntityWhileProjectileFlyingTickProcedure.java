package net.mcreator.mm.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

import net.mcreator.mm.configuration.ModernMayhemConfiguration;

public class FragProjectileEntityWhileProjectileFlyingTickProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (ModernMayhemConfiguration.GRENADES_BREAK_BLOCKS.get() == true) {
			if (world instanceof Level _level && !_level.isClientSide())
				_level.explode(null, (entity.getX()), (entity.getY()), (entity.getZ()), 2, Level.ExplosionInteraction.TNT);
		} else if (ModernMayhemConfiguration.GRENADES_BREAK_BLOCKS.get() == false) {
			if (world instanceof Level _level && !_level.isClientSide())
				_level.explode(null, (entity.getX()), (entity.getY()), (entity.getZ()), 2, Level.ExplosionInteraction.NONE);
		}
	}
}
