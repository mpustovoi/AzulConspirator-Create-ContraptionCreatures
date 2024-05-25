package com.azul.CreateContraptionCreatures.entity.custom.Projectiles;

import java.util.List;

import com.azul.CreateContraptionCreatures.item.ModItem;
import com.simibubi.create.foundation.utility.VecHelper;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;


public class MelonAmmoEntity extends PersistentProjectileEntity implements GeoEntity
{

	protected int timeInAir;
	protected boolean inAir;
	private static float bulletdamage;
	private static int EffectPotency;

	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public MelonAmmoEntity(EntityType<? extends MelonAmmoEntity> entityType, World world) {
		super(entityType, world);
		this.pickupType = PickupPermission.DISALLOWED;
	}

	public MelonAmmoEntity(World world, net.minecraft.entity.LivingEntity owner, Float damage, int Potency) {
		super(ModItem.MELON_BULLETS, owner, world);
		bulletdamage = damage;
		EffectPotency = Potency;
	}

	protected MelonAmmoEntity(EntityType<? extends MelonAmmoEntity> type, double x, double y, double z, World world) {
		this(type, world);
	}

	protected MelonAmmoEntity(EntityType<? extends MelonAmmoEntity> type, LivingEntity owner, World world) {
        this(type, owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ(), world);
        this.setOwner(owner);
    }
	@Override
	public void registerControllers(ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, event -> {
			return PlayState.CONTINUE;
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@Override
	protected void onHit(LivingEntity hitResult)
	{
		super.onHit(hitResult);
	}

	@SuppressWarnings("resource")
	@Override
    protected void onBlockHit(BlockHitResult blockHitResult)
	{
		this.spawnImpactParticles();
        super.onBlockHit(blockHitResult);
		if (!this.getWorld().isClient())
		{
            this.createLingeringEffect(EffectPotency);
            this.setSound(SoundEvents.BLOCK_HONEY_BLOCK_BREAK);
            this.remove(RemovalReason.DISCARDED);
        }

    }

    @SuppressWarnings("resource")
	@Override
    protected void onEntityHit(EntityHitResult entityHitResult)
	{
		this.spawnImpactParticles();
        super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient())
		{
            this.applyDamageToNearbyEntities();
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private void createLingeringEffect(int Potency)
	{
		StatusEffectInstance regenerationEffect = new StatusEffectInstance(StatusEffects.REGENERATION, 200, Potency);
        AreaEffectCloudEntity areaEffectCloud = new AreaEffectCloudEntity(this.getWorld(), this.getX(), this.getY(), this.getZ());

        Entity entity = this.getOwner();
        if (entity instanceof LivingEntity)
		{
            areaEffectCloud.setOwner((LivingEntity)entity);
        }

        areaEffectCloud.setRadius(2.0F);
        areaEffectCloud.setDuration(100);
        areaEffectCloud.setRadiusOnUse(-0.5F);
        areaEffectCloud.setWaitTime(10);
        areaEffectCloud.setRadiusGrowth(-areaEffectCloud.getRadius() / areaEffectCloud.getDuration());
        areaEffectCloud.addEffect(new StatusEffectInstance(regenerationEffect));
        areaEffectCloud.setColor(11028530);
		this.getWorld().spawnEntity(areaEffectCloud);
    }

	private void applyDamageToNearbyEntities()
	{
        double radius = 2.0; // Adjust the radius as needed
        List<Entity> entities = this.getWorld().getOtherEntities(this, new Box(this.getX() - radius, this.getY() - radius, this.getZ() - radius, this.getX() + radius, this.getY() + radius, this.getZ() + radius));

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity && entity != this.getOwner())
			{
                ((LivingEntity) entity).damage(getDamageSources().arrow(this, this.getOwner()), bulletdamage);
            }
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
		if (this.age >= 80 && !this.getWorld().isClient())
			this.remove(RemovalReason.DISCARDED);
	}

	public void initFromStack(ItemStack stack) {
		if (stack.getItem() == Items.MELON) {
		}
	}

	public SoundEvent sound = this.getHitSound();

	@Override
    public void setSound(SoundEvent sound) {
        this.sound = sound;
    }

	@Override
	protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_SCULK_CATALYST_BREAK;
    }

	@Override
	protected boolean tryPickup(PlayerEntity player)
	{
		return false;
	}

	@Override
	public ItemStack asItemStack()
	{
		return new ItemStack(Items.MELON_SLICE.asItem());
	}



	@Override
	@Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
		return true;
    }

	@SuppressWarnings("resource")
	@Environment(EnvType.CLIENT)
	private void spawnImpactParticles()
	{
		BlockPos loc = this.getBlockPos();
		Random random = this.getWorld().random;
		Vec3d center = VecHelper.getCenterOf(loc).add(0, 5 / 16f, 0);

		for (int i = 0; i < 30; i++) {
			Vec3d motion = VecHelper.offsetRandomly(new Vec3d(0, 0.25f, 0), random, .125f);
			this.getWorld().addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.asItemStack()),
				center.x, center.y, center.z,
				motion.x, motion.y, motion.z);
		}
	}

}
