package com.azul.CreateContraptionCreatures.entity.ai.goal;

import com.azul.CreateContraptionCreatures.entity.custom.Combatants.GearBugEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

public class CollectCreateBlockGoal extends MoveToTargetPosGoal
{
	private Block targetBlock;
    private final GearBugEntity EaterMob;
	private float targetHardness;
    private float progress;
	private float ratio;


    public CollectCreateBlockGoal(Block targetBlocks, GearBugEntity mob, double speed, int maxYDifference)
	{
        super(mob, speed, 24);
        this.targetBlock = targetBlocks;
        this.EaterMob = mob;
    }

    @Override
    public boolean canStart()
	{
		final World world = this.EaterMob.getWorld();

        if (!world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
		{
            return false;
        }
        if (this.findTargetPos())
		{
            this.cooldown = CollectCreateBlockGoal.toGoalTicks(20);
			targetHardness = targetBlock.getHardness() * 2;
			if (targetHardness < 0)
			{
				return false;
			}
			ratio = 10 / targetHardness;
			return targetHardness < 20;
        }
        return false;
    }

    @Override
    public void stop() {
        super.stop();
        this.EaterMob.fallDistance = 1.0f;
		this.EaterMob.updateGoal();
    }

    @Override
    public void start()
	{
        super.start();
    }

    public void tickStepping(WorldAccess world, BlockPos pos) {
    }

    public void onDestroyBlock(World world, BlockPos pos) {
    }

    @Override
    public void tick() {
        super.tick();
        World world = this.EaterMob.getWorld();
        BlockPos blockPosMob = this.EaterMob.getBlockPos();
        BlockPos blockPosDestination = this.tweakToProperPos(blockPosMob, world);
		progress += 0.05f;
        if (this.hasReached() && blockPosDestination != null)
		{
			if (progress >= targetHardness)
			{
				if(this.EaterMob.getSlot1() == "nil" && this.EaterMob.getSlot1() != this.targetBlock.asItem().toString())
				{
					this.EaterMob.setSlot1(this.targetBlock.asItem().toString());
					this.EaterMob.getWorld().breakBlock(blockPosDestination, false);
					return;
				}
				else if(this.EaterMob.getSlot2() == "nil" && this.EaterMob.getSlot1() != this.targetBlock.asItem().toString())
				{
					this.EaterMob.setSlot2(this.targetBlock.asItem().toString());
					this.EaterMob.getWorld().breakBlock(blockPosDestination, false);
					return;
				}
				else
				{
					this.EaterMob.getWorld().breakBlock(blockPosDestination, true);
					return;
				}

			}
			this.EaterMob.getWorld().setBlockBreakingInfo(this.EaterMob.getId(), blockPosDestination, (int) (progress * ratio));
        }
    }

	// Util

    public static float blockPosDistance(Entity entity, BlockPos pos)
    {
        return blockPosDistance(entity.getBlockPos(), pos);
    }
	public static float blockPosDistance(Entity entity1, Entity entity2)
    {
        return blockPosDistance(entity1.getBlockPos(), entity2.getBlockPos());
	}
	public static float blockPosDistance(BlockPos pos1, BlockPos pos2)
    {
        float x = (pos1.getX() - pos2.getX());
        float y = (pos1.getY() - pos2.getY());
        float z = (pos1.getZ() - pos2.getZ());
        return MathHelper.sqrt(x * x + y * y + z * z);
    }

    private BlockPos tweakToProperPos(BlockPos pos, BlockView world)
	{
        @SuppressWarnings("unused")
		BlockPos[] blockPoss;
        if (world.getBlockState(pos).isOf(this.targetBlock)) {
            return pos;
        }
        for (BlockPos blockPos : blockPoss = new BlockPos[]{pos.down(), pos.west(), pos.east(), pos.north(), pos.south(), pos.down().down()}) {
            if (!world.getBlockState(blockPos).isOf(this.targetBlock)) continue;
            return blockPos;
        }
        return null;
    }

    @Override
    protected boolean isTargetPos(WorldView world, BlockPos pos) {
        Chunk chunk = world.getChunk(ChunkSectionPos.getSectionCoord(pos.getX()), ChunkSectionPos.getSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
        if (chunk != null) {
            return chunk.getBlockState(pos).isOf(this.targetBlock);
        }
        return false;
    }
}
