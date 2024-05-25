package com.azul.CreateContraptionCreatures.entity.custom.Projectiles;

import com.azul.CreateContraptionCreatures.item.ModItem;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EchoBlastEntity extends PersistentProjectileEntity implements GeoEntity {

    protected int timeInAir;
    protected boolean inAir;
    private static float bulletdamage;
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

    public EchoBlastEntity(EntityType<? extends EchoBlastEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public EchoBlastEntity(World world, LivingEntity owner, Float damage) {
        super(ModItem.SEED_BULLETS, owner, world);
        bulletdamage = damage;
    }

    protected EchoBlastEntity(EntityType<? extends EchoBlastEntity> type, double x, double y, double z, World world) {
        this(type, world);
    }

    protected EchoBlastEntity(EntityType<? extends EchoBlastEntity> type, LivingEntity owner, World world) {
        this(type, owner.getX(), owner.getEyeY() - 0.1f, owner.getZ(), world);
        this.setOwner(owner);
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, event -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    @Override
    public void setVelocity(Entity shooter, float pitch, float yaw, float roll, float speed, float divergence) {
        super.setVelocity(shooter, pitch, yaw, roll, speed, divergence);
    }

    @Override
    public void setVelocity(double x, double y, double z, float speed, float divergence) {
        super.setVelocity(x, y, z, speed, divergence);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age >= 80) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public boolean hasNoGravity() {
        return !this.isTouchingWaterOrRain();
    }

    public SoundEvent sound = this.getHitSound();

    @Override
    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_IRON;
    }

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		createSonicBoom(blockHitResult.getPos(), null);
		if (!this.getWorld().isClient()) {
			this.remove(RemovalReason.DISCARDED);
		}
		this.setSound(SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity entity = entityHitResult.getEntity();
		if (entity instanceof LivingEntity) {
			LivingEntity target = (LivingEntity) entity;
			createSonicBoom(entityHitResult.getPos(), target);
			if (!this.getWorld().isClient()) {
				this.remove(RemovalReason.DISCARDED);
			}
		}
	}

	private void createSonicBoom(Vec3d hitPos, LivingEntity target) {
		Vec3d shooterPos = this.getOwner().getPos().add(0.0, 1.6f, 0.0);
		Vec3d targetPos = hitPos.subtract(shooterPos);
		Vec3d direction = targetPos.normalize();

		for (int i = 1; i < MathHelper.floor(targetPos.length()) + 7; ++i) {
			Vec3d particlePos = shooterPos.add(direction.multiply(i));
			((ServerWorld)this.getWorld()).spawnParticles(ParticleTypes.SONIC_BOOM, particlePos.x, particlePos.y, particlePos.z, 1, 0.0, 0.0, 0.0, 0.0);
		}
		this.playSound(SoundEvents.ENTITY_WARDEN_SONIC_BOOM, 3.0f, 1.0f);

		if (target != null) {
			target.damage(this.getWorld().getDamageSources().sonicBoom(this.getOwner()), bulletdamage);

			double knockbackResist = 1.0 - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE);
			double d = 0.5 * knockbackResist;
			double e = 2.5 * knockbackResist;

			target.addVelocity(direction.getX() * e, direction.getY() * d, direction.getZ() * e);
		}
	}

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return false;
    }

    @Override
    public ItemStack asItemStack() {
        return new ItemStack(Items.WHEAT_SEEDS);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
        return true;
    }
}
