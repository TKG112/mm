package net.mcreator.mm.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.util.RandomSource;
import net.minecraft.sounds.SoundSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;

import net.mcreator.mm.init.MmModItems;
import net.mcreator.mm.init.MmModEntities;
import net.mcreator.mm.procedures.MolotovProjectileProjectileHitsBlockProcedure;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class MolotovProjectileEntity extends ThrowableItemProjectile implements ItemSupplier {
	public static final ItemStack PROJECTILE_ITEM = new ItemStack(MmModItems.GRENADE_MOLOTOV.get());

	public MolotovProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
		super(MmModEntities.FRAG_PROJECTILE.get(), world);
	}

	public MolotovProjectileEntity(EntityType<? extends MolotovProjectileEntity> type, Level world) {
		super(type, world);
	}

	public MolotovProjectileEntity(EntityType<? extends MolotovProjectileEntity> type, LivingEntity entity, Level world) {
		super(type, entity, world);
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem() {
		return PROJECTILE_ITEM;
	}

	public void tick() {
    	super.tick();
	}

	@Override
	protected Item getDefaultItem() {
		return MmModItems.GRENADE_MOLOTOV.get();
	}

	@Override
	protected void onHitBlock(BlockHitResult result) {
		super.onHitBlock(result);
		MolotovProjectileProjectileHitsBlockProcedure.execute(this.level(), result.getBlockPos(), this.getOwner());
		this.discard();
	}

	public static MolotovProjectileEntity shoot(Level world, LivingEntity entity, RandomSource source) {
		return shoot(world, entity, source, 1f, 5, 5);
	}

	public static MolotovProjectileEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
		MolotovProjectileEntity entityProjectile = new MolotovProjectileEntity(MmModEntities.MOLOTOV_PROJECTILE.get(), entity, world);
		entityProjectile.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, power * 2.0F, 1.0F);
		entityProjectile.setSilent(true);

		world.addFreshEntity(entityProjectile);
		world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
		return entityProjectile;
	}

	public static MolotovProjectileEntity shoot(LivingEntity entity, LivingEntity target) {
		MolotovProjectileEntity entityProjectile = new MolotovProjectileEntity(MmModEntities.MOLOTOV_PROJECTILE.get(), entity, entity.level());
		double dx = target.getX() - entity.getX();
		double dy = target.getY() + target.getEyeHeight() - 1.1;
		double dz = target.getZ() - entity.getZ();
		entityProjectile.shoot(dx, dy - entityProjectile.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
		entityProjectile.setSilent(true);

		entity.level().addFreshEntity(entityProjectile);
		entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
		return entityProjectile;
	}
}
