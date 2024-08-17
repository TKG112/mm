
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;

import net.mcreator.mm.entity.SmokeProjectileEntity;
import net.mcreator.mm.entity.MolotovProjectileEntity;
import net.mcreator.mm.entity.ImpactProjectileEntity;
import net.mcreator.mm.entity.FragProjectileEntity;
import net.mcreator.mm.entity.FlashbangProjectileEntity;
import net.mcreator.mm.MmMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MmModEntities {
	public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MmMod.MODID);
	public static final RegistryObject<EntityType<FragProjectileEntity>> FRAG_PROJECTILE = register("frag_projectile",
			EntityType.Builder.<FragProjectileEntity>of(FragProjectileEntity::new, MobCategory.MISC).setCustomClientFactory(FragProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<ImpactProjectileEntity>> IMPACT_PROJECTILE = register("impact_projectile", EntityType.Builder.<ImpactProjectileEntity>of(ImpactProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(ImpactProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<MolotovProjectileEntity>> MOLOTOV_PROJECTILE = register("molotov_projectile", EntityType.Builder.<MolotovProjectileEntity>of(MolotovProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(MolotovProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<FlashbangProjectileEntity>> FLASHBANG_PROJECTILE = register("flashbang_projectile", EntityType.Builder.<FlashbangProjectileEntity>of(FlashbangProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(FlashbangProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));
	public static final RegistryObject<EntityType<SmokeProjectileEntity>> SMOKE_PROJECTILE = register("smoke_projectile", EntityType.Builder.<SmokeProjectileEntity>of(SmokeProjectileEntity::new, MobCategory.MISC)
			.setCustomClientFactory(SmokeProjectileEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.5f, 0.5f));

	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTRY.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}

	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
		});
	}

	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
	}
}
