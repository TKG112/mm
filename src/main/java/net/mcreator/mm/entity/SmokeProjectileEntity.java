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
import net.minecraft.core.BlockPos;

import net.mcreator.mm.procedures.SmokeFadeProcedure;
import net.mcreator.mm.procedures.SmokeParticleSpawnProcedure;
import net.mcreator.mm.init.MmModItems;
import net.mcreator.mm.init.MmModEntities;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class SmokeProjectileEntity extends ThrowableItemProjectile implements ItemSupplier {
    public static final ItemStack PROJECTILE_ITEM = new ItemStack(MmModItems.GRENADE_SMOKE.get());

    public SmokeProjectileEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(MmModEntities.SMOKE_PROJECTILE.get(), world);
    }

    public SmokeProjectileEntity(EntityType<? extends SmokeProjectileEntity> type, Level world) {
        super(type, world);
    }

    public SmokeProjectileEntity(EntityType<? extends SmokeProjectileEntity> type, LivingEntity entity, Level world) {
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
        
        // Delay for 60 ticks before executing the procedure
        if (this.tickCount >= 60) {
            // Execute procedure every 20 ticks
            if (this.tickCount % 20 == 0) {
                SmokeParticleSpawnProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ());
            }
            
            // Despawn the entity after 200 ticks
            if (this.tickCount >= 260) {
                this.discard();
                SmokeFadeProcedure.execute(this.level(), this.getX(), this.getY(), this.getZ());
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return MmModItems.GRENADE_SMOKE.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        Vec3 currentVelocity = this.getDeltaMovement();
        Vec3i normalVec3i = result.getDirection().getNormal();
        Vec3 normal = new Vec3(normalVec3i.getX(), normalVec3i.getY(), normalVec3i.getZ());
        Vec3 reflectedVelocity = currentVelocity.subtract(normal.scale(2 * currentVelocity.dot(normal))).scale(0.4);

        // Threshold for stopping the entity
        double stopThreshold = 0.1;

        // If the new velocity is below the threshold, stop the entity
        if (reflectedVelocity.length() < stopThreshold) {
            this.setDeltaMovement(Vec3.ZERO);
        } else {
            this.setDeltaMovement(reflectedVelocity);
            this.level().playSound(null, BlockPos.containing(this.getX(), this.getY(), this.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("mm:nade-landing")), SoundSource.NEUTRAL, 1, 1);
        }
    }

    public static SmokeProjectileEntity shoot(Level world, LivingEntity entity, RandomSource source) {
        return shoot(world, entity, source, 1f, 5, 5);
    }

    public static SmokeProjectileEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        SmokeProjectileEntity entityProjectile = new SmokeProjectileEntity(MmModEntities.SMOKE_PROJECTILE.get(), entity, world);
        entityProjectile.shootFromRotation(entity, entity.getXRot(), entity.getYRot(), 0.0F, power * 2.0F, 1.0F);
        entityProjectile.setSilent(true);
        // Implement custom damage and knockback if needed
        world.addFreshEntity(entityProjectile);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
        return entityProjectile;
    }

    public static SmokeProjectileEntity shoot(LivingEntity entity, LivingEntity target) {
        SmokeProjectileEntity entityProjectile = new SmokeProjectileEntity(MmModEntities.SMOKE_PROJECTILE.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityProjectile.shoot(dx, dy - entityProjectile.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1f * 2, 12.0F);
        entityProjectile.setSilent(true);
        // Implement custom damage and knockback if needed
        entity.level().addFreshEntity(entityProjectile);
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.arrow.shoot")), SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
        return entityProjectile;
    }
}
