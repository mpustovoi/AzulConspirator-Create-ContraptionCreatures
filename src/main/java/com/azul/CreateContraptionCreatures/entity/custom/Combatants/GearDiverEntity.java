package com.azul.CreateContraptionCreatures.entity.custom.Combatants;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.azul.CreateContraptionCreatures.entity.custom.AbstractHostileCogBotEntity;

import mod.azure.azurelib.animatable.SingletonGeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.NoPenaltyTargeting;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class GearDiverEntity  extends AbstractHostileCogBotEntity
{
	private AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	boolean targetingUnderwater;
	protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;

	public GearDiverEntity(net.minecraft.entity.EntityType<? extends AbstractHostileCogBotEntity> entityType, World world){
		super(entityType, world);
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        this.waterNavigation = new SwimNavigation(this, world);
        this.landNavigation = new MobNavigation(this, world);
    }

	public static DefaultAttributeContainer.Builder createCombatantGearDiverAttributes()
	{
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 135)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_ARMOR, 15f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5)
				.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5)
				.add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 1.5);
    }
	@Override
	public void initGoals()
	{
		this.goalSelector.add(1, new WanderAroundOnSurfaceGoal(this, 1.0));
        this.goalSelector.add(5, new LeaveWaterGoal(this, 1.0));
        this.goalSelector.add(6, new TargetAboveWaterGoal(this, 1.0, this.getWorld().getSeaLevel()));
        this.goalSelector.add(7, new WanderAroundGoal(this, 1.0));

        this.goalSelector.add(4, new AttackGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));

		this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
		this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));

	}

	@Override
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater() && this.isTargetingUnderwater()) {
            this.updateVelocity(0.01f, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9));
        } else {
            super.travel(movementInput);
        }
    }

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, event -> {
			if (event.isMoving())
				return event.setAndContinue(RawAnimation.begin().then("animation.gear_diver.walk", Animation.LoopType.LOOP));
			return event.setAndContinue(RawAnimation.begin().then("animation.gear_diver.idle", Animation.LoopType.LOOP));
		}).triggerableAnim("death", RawAnimation.begin().then("animation.gear_diver.death", LoopType.HOLD_ON_LAST_FRAME))
		.triggerableAnim("attack", RawAnimation.begin().then("animation.gear_diver.attack", LoopType.PLAY_ONCE))
		.triggerableAnim("attack_smash", RawAnimation.begin().then("animation.gear_diver.attack_smash", LoopType.PLAY_ONCE)));
	}

	@Override
    public boolean tryAttack(Entity target)
	{
        boolean bl = target.damage(this.getDamageSources().mobAttack(this), (int)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        if (bl)
		{
			if(random.nextInt(5) > 2)
			{
				bl = target.damage(this.getDamageSources().mobAttack(this), (int)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE)+5);
				this.triggerAnim("base_controller", "attack_smash");
				this.applyDamageEffects(this, target);
				return bl;
			}
			else
			{
				this.triggerAnim("base_controller", "attack");
				this.applyDamageEffects(this, target);
				return bl;
			}
        }
        return bl;
    }

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	protected boolean hasFinishedCurrentPath() {
        double d;
        BlockPos blockPos;
        Path path = this.getNavigation().getCurrentPath();
        return path != null && (blockPos = path.getTarget()) != null && (d = this.squaredDistanceTo(blockPos.getX(), blockPos.getY(), blockPos.getZ())) < 4.0;
    }

	boolean isTargetingUnderwater() {
        if (this.targetingUnderwater) {
            return true;
        }
        LivingEntity livingEntity = this.getTarget();
        return livingEntity != null && livingEntity.isTouchingWater();
    }

	public void setTargetingUnderwater(boolean targetingUnderwater) {
        this.targetingUnderwater = targetingUnderwater;
    }

	@Override
    public void updateSwimming() {
        if (!this.getWorld().isClient) {
            if (this.canMoveVoluntarily() && this.isTouchingWater() && this.isTargetingUnderwater()) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.navigation = this.landNavigation;
                this.setSwimming(false);
            }
        }
    }
	static class DrownedMoveControl
    extends MoveControl {
        private final GearDiverEntity Gdiver;

        public DrownedMoveControl(GearDiverEntity Gdiver) {
            super(Gdiver);
            this.Gdiver = Gdiver;
        }

        @Override
        public void tick()
		{
            LivingEntity livingEntity = this.Gdiver.getTarget();
            if (this.Gdiver.isTargetingUnderwater() && this.Gdiver.isTouchingWater()) {
                if (livingEntity != null && livingEntity.getY() > this.Gdiver.getY() || this.Gdiver.targetingUnderwater) {
                    this.Gdiver.setVelocity(this.Gdiver.getVelocity().add(0.0, 0.002, 0.0));
                }
                if (this.state != MoveControl.State.MOVE_TO || this.Gdiver.getNavigation().isIdle()) {
                    this.Gdiver.setMovementSpeed(0.0f);
                    return;
                }
                double d = this.targetX - this.Gdiver.getX();
                double e = this.targetY - this.Gdiver.getY();
                double f = this.targetZ - this.Gdiver.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float)(MathHelper.atan2(f, d) * 57.2957763671875) - 90.0f;
                this.Gdiver.setYaw(this.wrapDegrees(this.Gdiver.getYaw(), h, 90.0f));
                this.Gdiver.bodyYaw = this.Gdiver.getYaw();
                float i = (float)(this.speed * this.Gdiver.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                float j = MathHelper.lerp(0.125f, this.Gdiver.getMovementSpeed(), i);
                this.Gdiver.setMovementSpeed(j);
                this.Gdiver.setVelocity(this.Gdiver.getVelocity().add((double)j * d * 0.005, (double)j * e * 0.1, (double)j * f * 0.005));
            } else {
                if (!this.Gdiver.isOnGround()) {
                    this.Gdiver.setVelocity(this.Gdiver.getVelocity().add(0.0, -0.008, 0.0));
                }
                super.tick();
            }
        }
    }

    static class WanderAroundOnSurfaceGoal
    extends Goal {
        private final PathAwareEntity mob;
        private double x;
        private double y;
        private double z;
        private final double speed;
        private final World world;

        public WanderAroundOnSurfaceGoal(PathAwareEntity mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            this.world = mob.getWorld();
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            if (!this.world.isDay()) {
                return false;
            }
            if (this.mob.isTouchingWater()) {
                return false;
            }
            Vec3d vec3d = this.getWanderTarget();
            if (vec3d == null) {
                return false;
            }
            this.x = vec3d.x;
            this.y = vec3d.y;
            this.z = vec3d.z;
            return true;
        }

        @Override
        public boolean shouldContinue() {
            return !this.mob.getNavigation().isIdle();
        }

        @Override
        public void start() {
            this.mob.getNavigation().startMovingTo(this.x, this.y, this.z, this.speed);
        }

        @Nullable
        private Vec3d getWanderTarget() {
            Random random = this.mob.getRandom();
            BlockPos blockPos = this.mob.getBlockPos();
            for (int i = 0; i < 10; ++i) {
                BlockPos blockPos2 = blockPos.add(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
                if (!this.world.getBlockState(blockPos2).isOf(Blocks.WATER)) continue;
                return Vec3d.ofBottomCenter(blockPos2);
            }
            return null;
        }
    }

    static class LeaveWaterGoal
    extends MoveToTargetPosGoal {
        private final GearDiverEntity GDiver;

        public LeaveWaterGoal(GearDiverEntity GDiver, double speed) {
            super(GDiver, speed, 8, 2);
            this.GDiver = GDiver;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !this.GDiver.getWorld().isDay() && this.GDiver.isTouchingWater() && this.GDiver.getY() >= (double)(this.GDiver.getWorld().getSeaLevel() - 3);
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue();
        }

        @Override
        protected boolean isTargetPos(WorldView world, BlockPos pos) {
            BlockPos blockPos = pos.up();
            if (!world.isAir(blockPos) || !world.isAir(blockPos.up())) {
                return false;
            }
            return world.getBlockState(pos).hasSolidTopSurface(world, pos, this.GDiver);
        }

        @Override
        public void start() {
            this.GDiver.setTargetingUnderwater(false);
            this.GDiver.navigation = this.GDiver.landNavigation;
            super.start();
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    static class TargetAboveWaterGoal
    extends Goal {
        private final GearDiverEntity GDiver;
        private final double speed;
        private final int minY;
        private boolean foundTarget;

        public TargetAboveWaterGoal(GearDiverEntity GDiver, double speed, int minY) {
            this.GDiver = GDiver;
            this.speed = speed;
            this.minY = minY;
        }

        @Override
        public boolean canStart() {
            return !this.GDiver.getWorld().isDay() && this.GDiver.isTouchingWater() && this.GDiver.getY() < (double)(this.minY - 2);
        }

        @Override
        public boolean shouldContinue() {
            return this.canStart() && !this.foundTarget;
        }

        @Override
        public void tick() {
            if (this.GDiver.getY() < (double)(this.minY - 1) && (this.GDiver.getNavigation().isIdle() || this.GDiver.hasFinishedCurrentPath())) {
                Vec3d vec3d = NoPenaltyTargeting.findTo(this.GDiver, 4, 8, new Vec3d(this.GDiver.getX(), this.minY - 1, this.GDiver.getZ()), 1.5707963705062866);
                if (vec3d == null) {
                    this.foundTarget = true;
                    return;
                }
                this.GDiver.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
            }
        }

        @Override
        public void start() {
            this.GDiver.setTargetingUnderwater(true);
            this.foundTarget = false;
        }

        @Override
        public void stop() {
            this.GDiver.setTargetingUnderwater(false);
        }
    }
}
