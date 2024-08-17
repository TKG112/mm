package net.mcreator.mm.procedures;

import net.minecraftforge.registries.ForgeRegistries;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import net.mcreator.mm.init.MmModMobEffects;

import java.util.List;
import java.util.Comparator;

public class BulletVignetteSpawnProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if ((ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString()).equals("tacz:bullet")) {
			{
				final Vec3 _center = new Vec3((entity.getX()), (entity.getY()), (entity.getZ()));
				List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
				for (Entity entityiterator : _entfound) {
					if (!(ForgeRegistries.ENTITY_TYPES.getKey(entityiterator.getType()).toString()).equals("tacz:bullet")) {
						if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MmModMobEffects.VIGNETTE.get(), 20, 0, false, false));
					}
				}
			}
		}
	}
}
