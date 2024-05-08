package com.azul.CreateContraptionCreatures.entity.custom;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public abstract class AbstractCogBotEntity extends PathAwareEntity
implements net.minecraft.entity.mob.Monster,Angerable
{
	@Nullable
    private UUID angryAt;

    public AbstractCogBotEntity(EntityType<? extends PathAwareEntity> entityType, World world)
	{
		super((EntityType<? extends PathAwareEntity>)entityType, world);
		this.experiencePoints = 5;
		this.setPathfindingPenalty(PathNodeType.POWDER_SNOW, -1.0f);
        this.setPathfindingPenalty(PathNodeType.DANGER_POWDER_SNOW, -1.0f);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0f);
	}

	@Override
    protected void initGoals()
	{
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75f, 1));
        this.goalSelector.add(4, new LookAroundGoal(this));
		this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));

		this.targetSelector.add(3, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
        this.targetSelector.add(4, new ActiveTargetGoal<PlayerEntity>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

	public static DefaultAttributeContainer.Builder createHostileAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE);
    }

    @Override
    public void tickMovement()
	{
        this.tickHandSwing();
        super.tickMovement();
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_HOSTILE_SWIM;
    }

    @Override
    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_HOSTILE_SPLASH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_HOSTILE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_HOSTILE_DEATH;
    }

	@Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return -world.getPhototaxisFavor(pos);
    }

    @Override
    public LivingEntity.FallSounds getFallSounds() {
        return new LivingEntity.FallSounds(SoundEvents.ENTITY_HOSTILE_SMALL_FALL, SoundEvents.ENTITY_HOSTILE_BIG_FALL);
    }

    public static boolean isSpawnDark(ServerWorldAccess world, BlockPos pos, Random random)
	{
		// Check light level (spawn only in low light conditions)
		if (world.getLightLevel(pos, 0) <= 7) {
			// Check Y-level (spawn below ground)
			if (pos.getY() < 30) {
				return true;
			}
		}
		return false;
    }

    public static boolean canSpawnCog(EntityType<? extends PathAwareEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && AbstractCogBotEntity.isSpawnDark(world, pos, random) && AbstractCogBotEntity.canMobSpawn(type, world, spawnReason, pos, random);
    }

    @Override
    public boolean shouldDropXp()
	{
        return true;
    }

    @Override
    protected boolean shouldDropLoot()
	{
        return true;
    }

    @Nullable
    public UUID getAngryAt() {
        return this.angryAt;
    }

    public void setAngryAt(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }


    @Override
    public ItemStack getProjectileType(ItemStack stack) {
        if (stack.getItem() instanceof RangedWeaponItem) {
            Predicate<ItemStack> predicate = (Predicate<ItemStack>) ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
            ItemStack itemStack = RangedWeaponItem.getHeldProjectile(this, predicate);
            return itemStack.isEmpty() ? new ItemStack(Items.ARROW) : itemStack;
        }
        return ItemStack.EMPTY;
    }
}

