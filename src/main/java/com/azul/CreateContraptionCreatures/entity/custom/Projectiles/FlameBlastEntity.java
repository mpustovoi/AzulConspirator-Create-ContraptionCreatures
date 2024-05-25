package com.azul.CreateContraptionCreatures.entity.custom.Projectiles;

import org.joml.Math;

import com.azul.CreateContraptionCreatures.item.ModItem;

import mod.azure.azurelib.util.AzureLibUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class FlameBlastEntity extends PersistentProjectileEntity {

	private static final TrackedData<Float> FORCED_YAW = DataTracker.registerData(FlameBlastEntity.class, TrackedDataHandlerRegistry.FLOAT);
	public SoundEvent sound;
    @SuppressWarnings("unused")
	private LivingEntity shooter;
	private static float bulletdamage;
    private int idleTicks = 0;

    @SuppressWarnings("static-access")
	public FlameBlastEntity(EntityType<? extends FlameBlastEntity> entityType, World world) {
        super(entityType, world);
        this.pickupType = pickupType.DISALLOWED;
    }

    public FlameBlastEntity(World world, LivingEntity owner, float damage)
	{
        super(ModItem.FLAME_BLAST, owner, world);
		bulletdamage = damage;
        this.shooter = owner;
    }

    protected FlameBlastEntity(EntityType<? extends FlameBlastEntity> type, double x, double y, double z, World world)
	{
        this(type, world);
    }

	protected FlameBlastEntity(EntityType<? extends FlameBlastEntity> type, LivingEntity owner, World world)
	{
        this(type, owner.getX(), owner.getEyeY() - 0.10000000149011612D, owner.getZ(), world);
        this.setOwner(owner);
    }

    public FlameBlastEntity(World world, double x, double y, double z) {
        super(ModItem.FLAME_BLAST, x, y, z, world);
        this.setNoGravity(true);
        this.setDamage(0);
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
    protected void initDataTracker()
	{
        super.initDataTracker();
        this.getDataTracker().startTracking(FORCED_YAW , 0f);
    }


    @SuppressWarnings("resource")
	@Override
    public void tick()
	{
        var idleOpt = 100;
        if (getVelocity().lengthSquared() < 0.01)
			idleTicks++;
        else
			idleTicks = 0;
        if (idleOpt <= 0 || idleTicks < idleOpt)
			super.tick();
        if (this.age >= 15)
			this.remove(RemovalReason.DISCARDED);

        var isInsideWaterBlock = this.getWorld().isWater(getBlockPos());
        AzureLibUtil.spawnLightSource(this, isInsideWaterBlock);

        if (getOwner() instanceof PlayerEntity)
			setYaw(this.dataTracker.get(FORCED_YAW));
        if (this.age % 16 == 2)
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.PLAYERS, 0.5F, 1.0F);
        if (this.getWorld().isClient())
		{
            var x = this.getX() + (this.random.nextDouble() * 2.0D - 1.0D) * this.getWidth() * 0.5D;
            var y = this.getY() + 0.05D + this.random.nextDouble();
            var z = this.getZ() + (this.random.nextDouble() * 2.0D - 1.0D) * this.getWidth() * 0.5D;
			if(random.nextBetween(1, 10) > 8)
				this.getWorld().addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, true, x, y, z, 0, 0, 0);
			else
				this.getWorld().addParticle(ParticleTypes.FLAME, true, x, y, z, 0, 0, 0);
        }
        this.getWorld().getEntitiesByClass(LivingEntity.class, this.getBoundingBox().expand(2), (entity) -> entity.isAlive() && !this.isOwner(entity))
			.forEach(entity ->
			{
				entity.damage(getDamageSources().inFire(), bulletdamage);
				entity.setOnFireFor(90);
			});
    }

    @Override
    public boolean hasNoGravity() {
        return !this.isTouchingWaterOrRain();
    }

/*     @Override
    public void setSoundEvent(SoundEvent soundIn) {
        this.hitSound = soundIn;
    }
 */
    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_FIRE_AMBIENT;
    }

	@Override
    protected void onBlockHit(BlockHitResult blockHitResult)
	{
		super.onBlockHit(blockHitResult);
        if (this.getWorld().isClient())
		{
            return;
        }
        if (this.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
		{
            BlockPos blockPos = blockHitResult.getBlockPos().offset(blockHitResult.getSide());
            if (this.getWorld().isAir(blockPos))
			{
                this.getWorld().setBlockState(blockPos, AbstractFireBlock.getState(this.getWorld(), blockPos));
            }
        }
		this.setSound(SoundEvents.BLOCK_FIRE_AMBIENT);
    }

    @SuppressWarnings("resource")
	@Override
    protected void onEntityHit(EntityHitResult entityHitResult)
	{
		super.onEntityHit(entityHitResult);
        if (!this.getWorld().isClient)
			this.remove(RemovalReason.DISCARDED);
    }

	@Override
	@Environment(EnvType.CLIENT)
    public boolean shouldRender(double distance) {
		return true;
    }

    public void setProperties(float pitch, float yaw, float roll, float modifierZ)
	{
        var f = 0.017453292F;
        var x = -Math.sin(yaw * f) * Math.cos(pitch * f);
        var y = -Math.sin((pitch + roll) * f);
        var z = Math.cos(yaw * f) * Math.cos(pitch * f);
        this.setVelocity(x, y, z, modifierZ, 0);
    }

	@Override
	protected boolean tryPickup(PlayerEntity player)
	{
		return false;
	}

	@Override
	protected ItemStack asItemStack()
	{
		return new ItemStack(Items.BLAZE_ROD);
	}

}
