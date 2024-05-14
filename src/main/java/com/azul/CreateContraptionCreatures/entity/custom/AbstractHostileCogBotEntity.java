package com.azul.CreateContraptionCreatures.entity.custom;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.PounceAtTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
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
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public abstract class AbstractHostileCogBotEntity extends AbstractCogBotEntity
{
    public AbstractHostileCogBotEntity(EntityType<? extends AbstractCogBotEntity> entityType, World world)
	{
		super((EntityType<? extends AbstractCogBotEntity>)entityType, world);
		this.experiencePoints = 15;
	}

	@Override
	protected void initGoals()
	{
		this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.4f));
        this.goalSelector.add(4, new AttackGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));

		this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
		this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));
	}

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

	public static DefaultAttributeContainer.Builder createHostileAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE);
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
    public ItemStack getProjectileType(ItemStack stack) {
        if (stack.getItem() instanceof RangedWeaponItem) {
            Predicate<ItemStack> predicate = (Predicate<ItemStack>) ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
            ItemStack itemStack = RangedWeaponItem.getHeldProjectile(this, predicate);
            return itemStack.isEmpty() ? new ItemStack(Items.ARROW) : itemStack;
        }
        return ItemStack.EMPTY;
    }

	public static boolean isSpawnDark(ServerWorldAccess world, BlockPos pos, Random random)
	{
        if (world.getLightLevel(LightType.SKY, pos) > random.nextInt(32)) {
            return false;
        }
        DimensionType dimensionType = world.getDimension();
        int i = dimensionType.monsterSpawnBlockLightLimit();
        if (i < 15 && world.getLightLevel(LightType.BLOCK, pos) > i) {
            return false;
        }
        int j = world.toServerWorld().isThundering() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos);
        return j <= dimensionType.monsterSpawnLightTest().get(random);
    }

    public static boolean canSpawnCog(EntityType<? extends AbstractHostileCogBotEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && AbstractHostileCogBotEntity.isSpawnDark(world, pos, random) && AbstractHostileCogBotEntity.canMobSpawn(type, world, spawnReason, pos, random);
    }
}

