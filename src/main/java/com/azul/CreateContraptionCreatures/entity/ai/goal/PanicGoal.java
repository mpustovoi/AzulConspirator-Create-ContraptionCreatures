package com.azul.CreateContraptionCreatures.entity.ai.goal;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.predicate.entity.EntityPredicates;

public class PanicGoal extends EscapeDangerGoal
{
	protected final PathAwareEntity mob;
	@Nullable
    protected PathAwareEntity targetEntity;
	@Nullable
    protected Path fleePath;
	protected final EntityNavigation fleeingEntityNavigation;

	public PanicGoal(PathAwareEntity mob, double speed)
	{
		super(mob, speed);
		this.mob = mob;
		this.fleeingEntityNavigation = mob.getNavigation();
	}

	@Override
    public void start()
	{
		if (!mob.getWorld().isClient())
		{
			List<PathAwareEntity> list = mob.getWorld().getEntitiesByClass(PathAwareEntity.class, mob.getBoundingBox().expand(16.0F),EntityPredicates.EXCEPT_SPECTATOR);
			for (int i = 0; i < list.size(); ++i)
			{
				PathAwareEntity entity = (PathAwareEntity) list.get(i);
				if (entity.getType() == mob.getType())
				{
					if (mob.getAttacker() != null && entity.getAttacker() == null && mob.distanceTo(entity) < 16.0F && mob.distanceTo(mob.getAttacker()) < 16.0F / 2.0F)
					{
						entity.setAttacker(mob.getAttacker());
					}
				}
			}
		}
    }

}

