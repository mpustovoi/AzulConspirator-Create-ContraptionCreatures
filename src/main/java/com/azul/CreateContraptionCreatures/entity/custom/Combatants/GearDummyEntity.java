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
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;

public class GearDummyEntity extends AbstractHostileCogBotEntity
{
	private AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);

	public GearDummyEntity(net.minecraft.entity.EntityType<? extends AbstractHostileCogBotEntity> entityType, World world){
		super(entityType, world);
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
	}

	public static DefaultAttributeContainer.Builder createCombatantGearDummyAttributes()
	{
        return MobEntity.createMobAttributes()
			.add(EntityAttributes.GENERIC_MAX_HEALTH, 35)
			.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
			.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3);
    }

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, event -> {
			if (event.isMoving())
				return event.setAndContinue(RawAnimation.begin().then("animation.gear_dummy.walk", Animation.LoopType.LOOP));
			return event.setAndContinue(RawAnimation.begin().then("animation.gear_dummy.idle", Animation.LoopType.LOOP));
		}).triggerableAnim("death", RawAnimation.begin().then("animation.gear_dummy.death", LoopType.HOLD_ON_LAST_FRAME))
		.triggerableAnim("attack", RawAnimation.begin().then("animation.gear_dummy.attack", LoopType.PLAY_ONCE)));
	}

	@Override
    public boolean tryAttack(Entity target) {
        boolean bl = target.damage(this.getDamageSources().mobAttack(this), (int)this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        if (bl) {
			this.triggerAnim("base_controller", "attack");
            this.applyDamageEffects(this, target);
        }
        return bl;
    }

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
