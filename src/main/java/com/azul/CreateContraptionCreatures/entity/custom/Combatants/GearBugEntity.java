package com.azul.CreateContraptionCreatures.entity.custom.Combatants;


import com.azul.CreateContraptionCreatures.entity.ModEntity;
import com.azul.CreateContraptionCreatures.entity.ai.goal.CollectCreateBlockGoal;
import com.azul.CreateContraptionCreatures.entity.custom.AbstractCogBotEntity;
import com.simibubi.create.AllBlocks;

import mod.azure.azurelib.animatable.GeoEntity;
import mod.azure.azurelib.animatable.SingletonGeoAnimatable;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager.ControllerRegistrar;
import mod.azure.azurelib.core.animation.Animation;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.InventoryOwner;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GearBugEntity extends AbstractCogBotEntity implements GeoEntity,InventoryOwner
{
	//
	private final SimpleInventory Inventory = new SimpleInventory(2);
	public static final TrackedData<Boolean> IS_DIGGING = DataTracker.registerData(GearBugEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	//


	private int convertionValue = 0;
	private float eatingSpeed = 2.5f;
	//
	//
	public GearBugEntity(EntityType<? extends AbstractCogBotEntity> entityType, World world)
	{
		super((EntityType<? extends AbstractCogBotEntity>)entityType, world);
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
		this.experiencePoints = 5;

	}

	public static DefaultAttributeContainer.Builder createCombatantGearBugAttributes()
	{
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2f)
                .add(EntityAttributes.GENERIC_ARMOR, 0.5f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4);
    }

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, event -> {
			if (event.isMoving())
				return event.setAndContinue(RawAnimation.begin().then("animation.gear_bug.walk", Animation.LoopType.LOOP));
			return event.setAndContinue(RawAnimation.begin().then("animation.gear_bug.idle", Animation.LoopType.LOOP));
		}).triggerableAnim("attack", RawAnimation.begin().then("animation.gear_bug.attack", LoopType.PLAY_ONCE)));
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

	@Override
    protected void initGoals()
	{
        this.goalSelector.add(1, new SwimGoal(this));;
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.add(8, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(10, new LookAroundGoal(this));

		this.targetSelector.add(3, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
		this.targetSelector.add(3, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));
		this.goalSelector.add(2, new CollectCreateBlockGoal(AllBlocks.ANDESITE_CASING.get(), this, eatingSpeed, 3));
		this.goalSelector.add(2, new CollectCreateBlockGoal(AllBlocks.COPPER_CASING.get(), this, eatingSpeed, 3));
		this.goalSelector.add(2, new CollectCreateBlockGoal(AllBlocks.COGWHEEL.get(), this, eatingSpeed, 3));
		this.goalSelector.add(2, new CollectCreateBlockGoal(AllBlocks.LARGE_COGWHEEL.get(), this, eatingSpeed, 3));
	}

	@SuppressWarnings("resource")
	@Override
    public void tick()
	{
        if (!this.getWorld().isClient && this.isAlive() && !this.isAiDisabled())
		{
            if (this.checkInventory())
			{
                this.CogConvertTo(convertionValue);
            }
        }
        super.tick();
    }

    private boolean checkInventory()
	{
		// Swtich to change value based on invetory item combos
		if (this.Inventory.getStack(1) != null && this.Inventory.getStack(2) != null)
		{
			if (checkStack(AllBlocks.ANDESITE_CASING.get(), AllBlocks.COPPER_CASING.get()))
			{
				convertionValue = 1;
				return true;
			}
			else if (checkStack(AllBlocks.ANDESITE_CASING.get(), AllBlocks.COGWHEEL.get()))
			{
				convertionValue = 2;
				return true;
			}
			else if (checkStack(AllBlocks.ANDESITE_CASING.get(), AllBlocks.LARGE_COGWHEEL.get()))
			{
				convertionValue = 3;
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean checkStack(Block item1, Block item2)
	{
		return this.getInventory().containsAny(stack -> stack.isOf(item1.asItem())) && this.getInventory().containsAny(stack -> stack.isOf(item2.asItem()));
	}

	public Boolean getDigging()
	{
        return this.dataTracker.get(IS_DIGGING);
    }

    public void setDigging(Boolean Bool)
	{
        this.dataTracker.set(IS_DIGGING, Bool);
	}
	//
	@Override
    protected void loot(ItemEntity item) {
        InventoryOwner.pickUpItem(this, this, item);
    }

    @Override
    public boolean canGather(ItemStack stack)
	{
        return true;
    }

	@Override
    public SimpleInventory getInventory() {
        return this.Inventory;
    }


    protected void CogConvertTo(int EntityChoice)
	{
		switch (EntityChoice)
		{
			case 1:
				this.convertTo(ModEntity.GEAR_DUMMY,false);
				break;
			case 2:
				this.convertTo(ModEntity.GEAR_DUMMY,false);
				break;
			case 3:
				this.convertTo(ModEntity.GEAR_DUMMY,false);
				break;
		}
    }
}
