package com.azul.CreateContraptionCreatures.entity.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.GearKnightEntity;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class GearKnightModel extends GeoModel<GearKnightEntity>
{

    @Override
    public Identifier getModelResource(GearKnightEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/gear_knight.geo.json");
    }

    @Override
    public Identifier getTextureResource(GearKnightEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/gear_knight.png");
    }

    @Override
    public Identifier getAnimationResource(GearKnightEntity animatable)
	{

        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/gear_knight.animation.json");
    }
}
