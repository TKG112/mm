
package net.mcreator.mm.potion;

import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import net.mcreator.mm.procedures.EarRingOnEffectActiveTickProcedure;
import net.mcreator.mm.procedures.EarRingEffectExpiresProcedure;
import net.mcreator.mm.procedures.EarRingActiveTickConditionProcedure;

public class EarRingMobEffect extends MobEffect {
	public EarRingMobEffect() {
		super(MobEffectCategory.NEUTRAL, -1);
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		EarRingOnEffectActiveTickProcedure.execute(entity.level(), entity);
	}

	@Override
	public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
		super.removeAttributeModifiers(entity, attributeMap, amplifier);
		EarRingEffectExpiresProcedure.execute(entity.level(), entity);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return EarRingActiveTickConditionProcedure.execute(amplifier, duration);
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
