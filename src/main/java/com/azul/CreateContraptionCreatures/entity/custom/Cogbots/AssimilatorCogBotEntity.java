package com.azul.CreateContraptionCreatures.entity.custom.Cogbots;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

	// Mob that hunts down create blocks (excluding casing, cogs and shafts)
	// it consumes the block and converts into a CogBorg
	// Each Cogborg specializes in that Block specific uses.

public class AssimilatorCogBotEntity extends AbstractCogBotEntity
{

	 private static final TrackedData<Optional<BlockState>> CARRIED_BLOCK = DataTracker.registerData(AssimilatorCogBotEntity.class, TrackedDataHandlerRegistry.OPTIONAL_BLOCK_STATE);

	public AssimilatorCogBotEntity(EntityType<? extends AbstractCogBotEntity> entityType, World world)
	{
		super(entityType, world);
	}

	    @Override
    protected void initGoals()
	{
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal((PathAwareEntity)this, 1.0, 0.0f));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(11, new PickUpBlockGoal(this));
    }

    public void setCarriedBlock(@Nullable BlockState state) {
        this.dataTracker.set(CARRIED_BLOCK, Optional.ofNullable(state));
    }

    @Nullable
    public BlockState getCarriedBlock() {
        return this.dataTracker.get(CARRIED_BLOCK).orElse(null);
    }

	static class PickUpBlockGoal
    extends Goal {
        private final AssimilatorCogBotEntity Assimilator;

        public PickUpBlockGoal(AssimilatorCogBotEntity assimilatorCogBotEntity) {
            this.Assimilator = assimilatorCogBotEntity;
        }

        @Override
        public boolean canStart() {
            if (this.Assimilator.getCarriedBlock() != null) {
                return false;
            }
            if (!this.Assimilator.getWorld().getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                return false;
            }
            return this.Assimilator.getRandom().nextInt(PickUpBlockGoal.toGoalTicks(20)) == 0;
        }

        @Override
        public void tick() {
            Random random = this.Assimilator.getRandom();
            World world = this.Assimilator.getWorld();
            int i = MathHelper.floor(this.Assimilator.getX() - 2.0 + random.nextDouble() * 4.0);
            int j = MathHelper.floor(this.Assimilator.getY() + random.nextDouble() * 3.0);
            int k = MathHelper.floor(this.Assimilator.getZ() - 2.0 + random.nextDouble() * 4.0);
            BlockPos blockPos = new BlockPos(i, j, k);
            BlockState blockState = world.getBlockState(blockPos);
            Vec3d vec3d = new Vec3d((double)this.Assimilator.getBlockX() + 0.5, (double)j + 0.5, (double)this.Assimilator.getBlockZ() + 0.5);
            Vec3d vec3d2 = new Vec3d((double)i + 0.5, (double)j + 0.5, (double)k + 0.5);
            BlockHitResult blockHitResult = world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, this.Assimilator));
            boolean bl = blockHitResult.getBlockPos().equals(blockPos);
            if (blockState.isIn(BlockTags.ENDERMAN_HOLDABLE) && bl)
			{
                world.removeBlock(blockPos, false);
                world.emitGameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Emitter.of(this.Assimilator, blockState));
                this.Assimilator.setCarriedBlock(blockState.getBlock().getDefaultState());
            }
        }
    }
}
