package com.azul.CreateContraptionCreatures.entity.custom.Combatants;

import java.util.Set;

import javax.annotation.Nullable;

import com.azul.CreateContraptionCreatures.entity.ModEntity;
import com.azul.CreateContraptionCreatures.entity.ai.goal.CollectCreateBlockGoal;
import com.azul.CreateContraptionCreatures.entity.custom.AbstractCogBotEntity;
import com.google.common.collect.ImmutableSet;
import com.simibubi.create.AllBlocks;

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
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LightType;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
public class GearBugEntity extends AbstractCogBotEntity
{
	private AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private int convertionValue = 0;
	private float eatingSpeed = 1.2f;
	String[] SlotContent = {};
	//
	//private final SimpleInventory Inventory = new SimpleInventory(2);
	public static final TrackedData<Boolean> IS_DIGGING = DataTracker.registerData(GearBugEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<String> SLOT1_DATA = DataTracker.registerData(GearBugEntity.class, TrackedDataHandlerRegistry.STRING);
	public static final TrackedData<String> SLOT2_DATA = DataTracker.registerData(GearBugEntity.class, TrackedDataHandlerRegistry.STRING);

	private static final Set<String> Wool_List = ImmutableSet.of(
		Blocks.WHITE_WOOL.asItem().toString(),
		Blocks.ORANGE_WOOL.asItem().toString(),
		Blocks.MAGENTA_WOOL.asItem().toString(),
		Blocks.LIGHT_BLUE_WOOL.asItem().toString(),
		Blocks.YELLOW_WOOL.asItem().toString(),
		Blocks.LIME_WOOL.asItem().toString(),
		Blocks.PINK_WOOL.asItem().toString(),
		Blocks.GRAY_WOOL.asItem().toString(),
		Blocks.LIGHT_GRAY_WOOL.asItem().toString(),
		Blocks.CYAN_WOOL.asItem().toString(),
		Blocks.PURPLE_WOOL.asItem().toString(),
		Blocks.BLUE_WOOL.asItem().toString(),
		Blocks.BROWN_WOOL.asItem().toString(),
		Blocks.GREEN_WOOL.asItem().toString(),
		Blocks.RED_WOOL.asItem().toString(),
		Blocks.BLACK_WOOL.asItem().toString());

	private boolean HaveAndesiteCasing = false;
	private boolean HaveCopperCasing = false;
	private boolean HaveCogWheel = false;
	private boolean HaveLargeCogWheel = false;

	@Nullable
	private CollectCreateBlockGoal ANDESITE_CASING_Goal;
	@Nullable
	private CollectCreateBlockGoal COPPER_CASING_Goal;
	@Nullable
	private CollectCreateBlockGoal LARGE_COGWHEEL_Goal;
	@Nullable
	private CollectCreateBlockGoal COGWHEEL_Goal;
	//
	//
	public GearBugEntity(EntityType<? extends AbstractCogBotEntity> entityType, World world)
	{
		super((EntityType<? extends AbstractCogBotEntity>)entityType, world);
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
		this.experiencePoints = 5;
		this.setStepHeight(1.0f);
		this.updateGoal();
	}
	@Override
    protected void initDataTracker()
	{
        super.initDataTracker();
        this.getDataTracker().startTracking(IS_DIGGING , false);
        this.getDataTracker().startTracking(SLOT1_DATA,"nil");
        this.getDataTracker().startTracking(SLOT2_DATA, "nil");
    }

	public static DefaultAttributeContainer.Builder createCombatantGearBugAttributes()
	{
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 10)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4f)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2);
    }

	@Override
	public void registerControllers(ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(this, event -> {
			if (event.isMoving())
				return event.setAndContinue(RawAnimation.begin().then("animation.gear_bug.walk", Animation.LoopType.LOOP));
			return event.setAndContinue(RawAnimation.begin().then("animation.gear_bug.idle", Animation.LoopType.LOOP));
		})
		.triggerableAnim("attack", RawAnimation.begin().then("animation.gear_bug.attack", LoopType.PLAY_ONCE))
		.triggerableAnim("convert", RawAnimation.begin().thenPlayAndHold("animation.gear_bug.convert"))
		.triggerableAnim("death", RawAnimation.begin().thenPlayAndHold("animation.gear_bug.death")));
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
    public boolean onKilledOther(ServerWorld world, LivingEntity other) {
        boolean bl = super.onKilledOther(world, other);
        if (other instanceof SkeletonEntity)
		{
			this.CogConvertTo(200);
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
		this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(3, new PounceAtTargetGoal(this, 0.4f));
        this.goalSelector.add(4, new AttackGoal(this));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));

		this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
		this.targetSelector.add(2, new ActiveTargetGoal<PlayerEntity>((MobEntity)this, PlayerEntity.class, true));
		this.targetSelector.add(3, new ActiveTargetGoal<SkeletonEntity>((MobEntity)this, SkeletonEntity.class, true));
	}

	public void updateGoal()
	{
        if (this.ANDESITE_CASING_Goal == null)
		{
            this.ANDESITE_CASING_Goal = new CollectCreateBlockGoal(AllBlocks.ANDESITE_CASING.get(), this, eatingSpeed, 3);
        }
		if (this.COPPER_CASING_Goal == null)
		{
            this.COPPER_CASING_Goal = new CollectCreateBlockGoal(AllBlocks.COPPER_CASING.get(), this, eatingSpeed, 3);
        }
		if (this.COGWHEEL_Goal == null)
		{
            this.COGWHEEL_Goal = new CollectCreateBlockGoal(AllBlocks.COGWHEEL.get(), this, eatingSpeed, 3);
        }
		if (this.LARGE_COGWHEEL_Goal == null)
		{
            this.LARGE_COGWHEEL_Goal = new CollectCreateBlockGoal(AllBlocks.LARGE_COGWHEEL.get(), this, eatingSpeed, 3);
        }
		this.GoalFilter();
		if (this.CanConvertTo())
		{
			this.triggerAnim("base_controller", "convert");
			this.CogConvertTo(convertionValue);
			return;
		}
		return;
    }

	private void GoalFilter()
	{
		this.checkItemContent();
		if (this.HaveAndesiteCasing)
			{
				this.targetSelector.remove(this.ANDESITE_CASING_Goal);
			}
			else
			{
				this.goalSelector.add(2, this.ANDESITE_CASING_Goal);
			}
		if (this.HaveCopperCasing)
			{
				this.targetSelector.remove(this.COPPER_CASING_Goal);
			}
			else
			{
				this.goalSelector.add(2, this.COPPER_CASING_Goal);
			}
		if (this.HaveCogWheel)
			{
				this.targetSelector.remove(this.COGWHEEL_Goal);
			}
			else
			{
				this.goalSelector.add(2, this.COGWHEEL_Goal);
			}
		if (this.HaveLargeCogWheel)
			{
				this.targetSelector.remove(this.LARGE_COGWHEEL_Goal);
			}
			else
			{
				this.goalSelector.add(2, this.LARGE_COGWHEEL_Goal);
			}
	}
	// Conversion

	protected void CogConvertTo(int EntityChoice)
	{
		switch (EntityChoice)
		{
			case 1:
				this.convertTo(ModEntity.GEAR_DUMMY,false);
				break;
			case 2:
				this.convertTo(ModEntity.GEAR_DIVER,false);
				break;
			case 3:
				this.convertTo(ModEntity.GEAR_KNIGHT,false);
				break;
			case 200:
				this.convertTo(ModEntity.GEAR_MARROW,false);
				break;
		}
    }

	private boolean CanConvertTo()
	{
		// Swtich to change value based on invetory item combos
		if (this.getSlot1() != "nil" && this.getSlot2() != "nil")
		{
			if (this.HaveAndesiteCasing && this.HaveCogWheel)
			{
				// Gear Puppet
				this.convertionValue = 1;
				return true;
			}
			else if (this.HaveCopperCasing && this.HaveCogWheel)
			{
				//Gear diver
				this.convertionValue = 2;
				return true;
			}
			else if (this.HaveAndesiteCasing && this.HaveLargeCogWheel)
			{
				//Gear Knight
				this.convertionValue = 3;
				return true;
			}
			return false;
		}
		return false;
	}

	private void checkItemContent()
	{
		this.SlotContent = new String[]{this.getSlot1(),this.getSlot2()};

		for (String slot : this.SlotContent)
		{
			if (slot.equals(AllBlocks.ANDESITE_CASING.get().asItem().toString()))
			{
				HaveAndesiteCasing = true;
			}
			if (slot.equals(AllBlocks.COPPER_CASING.get().asItem().toString()))
			{
				HaveCopperCasing = true;
			}
			if (slot.equals(AllBlocks.COGWHEEL.get().asItem().toString()))
			{
				HaveCogWheel = true;
			}
			if (slot.equals(AllBlocks.LARGE_COGWHEEL.get().asItem().toString()))
			{
				HaveLargeCogWheel = true;
			}
		}
	}


	// Digging Checker
	public Boolean getDigging()
	{
        return this.dataTracker.get(IS_DIGGING);
    }

    public void setDigging(Boolean Bool)
	{
        this.dataTracker.set(IS_DIGGING, Bool);
	}

	public String getSlot1()
	{
        return this.dataTracker.get(SLOT1_DATA);
    }

    public void setSlot1(String item)
	{
        this.dataTracker.set(SLOT1_DATA, item);
	}

	public String getSlot2()
	{
        return this.dataTracker.get(SLOT2_DATA);
    }

    public void setSlot2(String item)
	{
        this.dataTracker.set(SLOT2_DATA, item);
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

    public static boolean canSpawnCog(EntityType<? extends GearBugEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && GearBugEntity.isSpawnDark(world, pos, random) && GearBugEntity.canMobSpawn(type, world, spawnReason, pos, random);
    }
}
