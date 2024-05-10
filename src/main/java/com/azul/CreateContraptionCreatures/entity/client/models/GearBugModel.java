package com.azul.CreateContraptionCreatures.entity.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.GearBugEntity;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class GearBugModel extends GeoModel<GearBugEntity>
{

    @Override
    public Identifier getModelResource(GearBugEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/gear_bug.geo.json");
    }

    @Override
    public Identifier getTextureResource(GearBugEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/gear_bug.png");
    }

    @Override
    public Identifier getAnimationResource(GearBugEntity animatable)
	{

        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/gear_bug.animation.json");
    }
}
