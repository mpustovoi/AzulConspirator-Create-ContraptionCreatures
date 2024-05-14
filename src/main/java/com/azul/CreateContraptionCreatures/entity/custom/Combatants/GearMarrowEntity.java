package com.azul.CreateContraptionCreatures.entity.custom.Combatants;

import com.azul.CreateContraptionCreatures.entity.custom.AbstractHostileCogBotEntity;

import mod.azure.azurelib.animatable.SingletonGeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.WanderNearTargetGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class GearMarrowEntity extends AbstractHostileCogBotEntity
{
	private AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public GearMarrowEntity(net.minecraft.entity.EntityType<? extends AbstractHostileCogBotEntity> entityType, World world){
		super(entityType, world);
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
	}

	public static DefaultAttributeContainer.Builder createCombatantGearMarrowAttributes()
	{
        return MobEntity.createMobAttributes()
		.add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0f)
		.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5f)
		.add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 1f)
		.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 6f)
		.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0f)
		.add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.3f);
    }

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, event -> {
			if (event.isMoving() && !event.isCurrentAnimationStage("animation.gear_marrow.attack") && !event.isCurrentAnimationStage("animation.gear_marrow.end_attack"))
				return event.setAndContinue(RawAnimation.begin().then("animation.gear_marrow.walk", Animation.LoopType.LOOP));
			return event.setAndContinue(RawAnimation.begin().then("animation.gear_marrow.idle", Animation.LoopType.LOOP));
		}).triggerableAnim("death", RawAnimation.begin().then("animation.gear_marrow.death", LoopType.HOLD_ON_LAST_FRAME))
		.triggerableAnim("attack", RawAnimation.begin().then("animation.gear_marrow.attack", LoopType.HOLD_ON_LAST_FRAME))
		.triggerableAnim("end_attack", RawAnimation.begin().then("animation.gear_marrow.end_attack", LoopType.PLAY_ONCE)));
	}

	@Override
    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(this.getDamageSources().mobAttack(this), (int)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        if (bl) {
			this.triggerAnim("base_controller", "attack");
            this.applyDamageEffects(this, target);
			this.triggerAnim("base_controller", "end_attack");
        }
        return bl;
    }

		// Goal & Convert
	@Override
    protected void initGoals()
	{
		this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(4, new AttackGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
		this.goalSelector.add(5, new WanderNearTargetGoal(this, 0.8,8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));

		this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
		this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));
		this.targetSelector.add(3, new ActiveTargetGoal<WitherSkeletonEntity>((MobEntity)this, WitherSkeletonEntity.class, true));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}


}
