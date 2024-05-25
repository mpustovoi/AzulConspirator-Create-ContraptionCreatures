package com.azul.CreateContraptionCreatures.entity.custom.Projectiles;

import com.azul.CreateContraptionCreatures.item.ModItem;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TeleBulletEntity extends PersistentProjectileEntity implements GeoEntity
{
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private Vec3d targetPos;
    private boolean shouldTeleport = false;
    private int teleportDelay = 200; // 10 seconds (200 ticks)


    public TeleBulletEntity(EntityType<? extends TeleBulletEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = PickupPermission.DISALLOWED;
    }

    public TeleBulletEntity(World world, LivingEntity owner, float damage) {
        super(ModItem.TELE_BULLET, owner, world);
        this.setOwner(owner);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, event -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

	@Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        targetPos = blockHitResult.getPos();
        shouldTeleport = true;
        this.setVelocity(0, 0, 0); // Stop the projectile
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        targetPos = entityHitResult.getPos();
        shouldTeleport = true;
        this.setVelocity(0, 0, 0); // Stop the projectile
    }

    @Override
    public void tick() {
        super.tick();

        // Check if projectile has traveled more than 50 blocks without hitting anything
        if (this.age >= 50 && !shouldTeleport)
		{
			this.setVelocity(0, 0, 0); // Stop the projectile
            targetPos = this.getPos();
            shouldTeleport = true;
        }

        if (shouldTeleport)
		{
            // Create visible particles
            for (int i = 0; i < 10; i++)
			{
                this.getWorld().addParticle(ParticleTypes.PORTAL, true, targetPos.getX(), targetPos.getY(), targetPos.getZ(), 0, 0, 0);
            }

            // Teleport after the delay
            if (this.age >= teleportDelay)
			{
                teleportOwner(targetPos);
                if (!this.getWorld().isClient()) {
                    this.remove(RemovalReason.DISCARDED);
                }
            }
        }
    }

    private void teleportOwner(Vec3d hitPos) {
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity) {
            owner.requestTeleport(hitPos.x, hitPos.y, hitPos.z);
            this.getWorld().playSound(null, hitPos.x, hitPos.y, hitPos.z, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }



    @Override
    @Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
        return true;
    }

	@Override
	protected ItemStack asItemStack() {
		return new ItemStack(Items.AIR);
	}
}
