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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.GameStateChangeS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;


public class SeedAmmoEntity extends PersistentProjectileEntity implements GeoEntity
{

	protected int timeInAir;
	protected boolean inAir;
	private static float bulletdamage;
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public SeedAmmoEntity(EntityType<? extends SeedAmmoEntity> entityType, World world) {
		super(entityType, world);
		this.pickupType = PickupPermission.DISALLOWED;
	}

	public SeedAmmoEntity(World world, net.minecraft.entity.LivingEntity owner, Float damage) {
		super(ModItem.SEED_BULLETS, owner, world);
		bulletdamage = damage;
	}

	protected SeedAmmoEntity(EntityType<? extends SeedAmmoEntity> type, double x, double y, double z, World world) {
		this(type, world);
	}

	protected SeedAmmoEntity(EntityType<? extends SeedAmmoEntity> type, LivingEntity owner, World world) {
        this(type, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), world);
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PickupPermission.ALLOWED;
        }
    }
	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, event -> {
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}


	@Override
	protected void onHit(LivingEntity living) {
		super.onHit(living);
		if (!(living instanceof PlayerEntity))
		{
			living.hurtTime = 0;
			living.setVelocity(0, 0, 0);
		}
	}

	@Override
	public void setVelocity(Entity shooter, float pitch, float yaw, float roll, float speed, float divergence)
	{
		super.setVelocity(shooter,pitch,yaw,roll,speed,divergence);
    }

	@Override
	public void setVelocity(double x, double y, double z, float speed, float divergence)
	{
		super.setVelocity(x, y, z, speed, divergence);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.age >= 80)
			this.remove(RemovalReason.DISCARDED);
		if (this.getWorld().isClient()) {
			double x = this.getX() + (this.random.nextDouble()) * (double) this.getWidth() * 0.5D;
			double z = this.getZ() + (this.random.nextDouble()) * (double) this.getWidth() * 0.5D;
			this.getWorld().addParticle(ParticleTypes.SMOKE, true, x, this.getY(), z, 0, 0, 0);
		}
	}

	public void initFromStack(ItemStack stack) {
		if (stack.getItem() == Items.WHEAT_SEEDS) {
		}
	}

	@Override
	public boolean hasNoGravity()
	{
		if (this.isTouchingWaterOrRain())
			return false;
		return true;
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
		if (!this.getWorld().isClient())
			this.remove(RemovalReason.DISCARDED);
		this.setSound(SoundEvents.ITEM_ARMOR_EQUIP_IRON);
	}

	@SuppressWarnings("resource")
	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		var entity = entityHitResult.getEntity();

		if (entityHitResult.getType() != HitResult.Type.ENTITY || !((EntityHitResult) entityHitResult).getEntity().equals(entity))
			if (!this.getWorld().isClient())
				this.remove(RemovalReason.DISCARDED);

		var entity2 = this.getOwner();
		DamageSource damageSource2;

		if (entity2 == null)
		{
			damageSource2 = getDamageSources().arrow(this, this);
		}
		else
		{
			damageSource2 = getDamageSources().arrow(this, entity2);

            if (entity2 instanceof LivingEntity) {
                ((LivingEntity)entity2).onAttacking(entity);
            }
		}

		if (entity.damage(damageSource2, bulletdamage))
		{
			if (entity instanceof LivingEntity)
			{
				var livingEntity = (LivingEntity) entity;
				if (!this.getWorld().isClient && entity2 instanceof LivingEntity)
				{
                    EnchantmentHelper.onUserDamaged(livingEntity, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity);
                }
                this.onHit(livingEntity);
                if (entity2 != null && livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent())
				{
                    ((ServerPlayerEntity)entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
                }
			}
		} else if (!this.getWorld().isClient())
			this.remove(RemovalReason.DISCARDED);
	}

	@Override
	public ItemStack asItemStack()
	{
		return new ItemStack(Items.WHEAT_SEEDS);
	}



	@Override
	@Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
		return true;
    }
}
