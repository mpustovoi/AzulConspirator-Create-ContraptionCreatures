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
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
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
		if (stack.getItem() == Items.WHEAT_SEEDS) {}
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

		// Check if the hit result is indeed an entity hit and the entity matches the hit result entity
		if (entityHitResult.getType() != HitResult.Type.ENTITY || !((EntityHitResult) entityHitResult).getEntity().equals(entity))
		{
			if (!this.getWorld().isClient()) {
				// If it's not a valid entity hit or on the client side, remove the projectile
				this.remove(RemovalReason.DISCARDED);
			}
			return;
		}

		var entity2 = this.getOwner();  // Get the entity that fired the projectile
		DamageSource damageSource2;

		// Determine the damage source based on the owner of the projectile
		if (entity2 == null) {
			// If there is no owner, the projectile itself is the damage source
			damageSource2 = getDamageSources().arrow(this, this);
		} else {
			// If there is an owner, the owner is the damage source
			damageSource2 = getDamageSources().arrow(this, entity2);

			// If the owner is a LivingEntity, register the attack
			if (entity2 instanceof LivingEntity) {
				((LivingEntity) entity2).onAttacking(entity);
			}
		}

		// Apply damage to the hit entity
		if (entity.damage(damageSource2, bulletdamage))
		{
			if (entity instanceof LivingEntity)
			{
				var livingEntity = (LivingEntity) entity;

				// Perform additional server-side checks if the owner is a LivingEntity
				if (!this.getWorld().isClient() && entity2 instanceof LivingEntity)
				{
					LivingEntity target = (LivingEntity) entity;
					Vec3d hitPos = entityHitResult.getPos();  // Get the position where the hit occurred

					// Get the bounding box of the entity's head
					Box headBox = getEntityHeadBoundingBox(target);

					// Check if the hit position is within the head bounding box
					boolean isHeadshot = headBox.contains(hitPos);

					if (isHeadshot) {
						// Apply additional damage for headshots
						entity.damage(damageSource2, bulletdamage * 5); // Double damage for headshot
					}
					// Apply enchantment effects on damage
					EnchantmentHelper.onUserDamaged(livingEntity, entity2);
					EnchantmentHelper.onTargetDamaged((LivingEntity) entity2, livingEntity);
				}
				this.onHit(livingEntity);  // Perform additional hit logic
				// Send hit notification to the player who fired the projectile, if applicable
				if (entity2 != null && livingEntity != entity2 && livingEntity instanceof PlayerEntity && entity2 instanceof ServerPlayerEntity && !this.isSilent()) {
					((ServerPlayerEntity) entity2).networkHandler.sendPacket(new GameStateChangeS2CPacket(GameStateChangeS2CPacket.PROJECTILE_HIT_PLAYER, GameStateChangeS2CPacket.DEMO_OPEN_SCREEN));
				}
			}
		}
		else if (!this.getWorld().isClient())
		{
			// If the entity was not damaged and this is not on the client side, remove the projectile
			this.remove(RemovalReason.DISCARDED);
		}
	}

	@Override
	protected boolean tryPickup(PlayerEntity player)
	{
		return false;
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


	// Utility method to get the head bounding box of an entity
	private Box getEntityHeadBoundingBox(LivingEntity entity)
	{
		if (entity instanceof PlayerEntity) {
			return new Box(
				entity.getX() - 0.25, entity.getY() + entity.getEyeHeight(entity.getPose()) - 0.25,
				entity.getZ() - 0.25, entity.getX() + 0.25, entity.getY() + entity.getEyeHeight(entity.getPose()) + 0.25,
				entity.getZ() + 0.25
			);
		}
		// Adjust the bounding box based on the entity's head height and width
		double headHeight = entity.getHeight() * 0.15;
		double headWidth = entity.getWidth() * 0.5;
		return new Box(
			entity.getX() - headWidth, entity.getY() + entity.getHeight() - headHeight,
			entity.getZ() - headWidth, entity.getX() + headWidth, entity.getY() + entity.getHeight(),
			entity.getZ() + headWidth
		);
	}
}
