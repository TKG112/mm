
package net.mcreator.mm.potion;

import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.mcreator.mm.procedures.BleedingOnEffectActiveTickProcedure;
import net.mcreator.mm.procedures.BleedingActiveTickConditionProcedure;

public class BleedingMobEffect extends MobEffect {
	public BleedingMobEffect() {
		super(MobEffectCategory.HARMFUL, -65536);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		BleedingOnEffectActiveTickProcedure.execute(entity.level(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return BleedingActiveTickConditionProcedure.execute(amplifier, duration);
	}

	@Override
	public void initializeClient(java.util.function.Consumer<IClientMobEffectExtensions> consumer) {
		consumer.accept(new IClientMobEffectExtensions() {
			@Override
			public boolean isVisibleInGui(MobEffectInstance effect) {
				return false;
			}
		});
	}
}
