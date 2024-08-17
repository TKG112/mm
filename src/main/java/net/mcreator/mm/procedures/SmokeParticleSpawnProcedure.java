package net.mcreator.mm.procedures;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.particles.SimpleParticleType;

import net.mcreator.mm.init.MmModParticleTypes;

public class SmokeParticleSpawnProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		if (world instanceof ServerLevel _level)
			_level.sendParticles((SimpleParticleType) (MmModParticleTypes.SMOKE_PARTICLE.get()), x, y, z, 100, 1.5, 1.5, 1.5, 0);
	}
}
