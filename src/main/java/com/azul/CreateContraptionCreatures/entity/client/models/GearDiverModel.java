package com.azul.CreateContraptionCreatures.entity.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.GearDiverEntity;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class GearDiverModel extends GeoModel<GearDiverEntity>
{

    @Override
    public Identifier getModelResource(GearDiverEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/gear_diver.geo.json");
    }

    @Override
    public Identifier getTextureResource(GearDiverEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/gear_diver.png");
    }

    @Override
    public Identifier getAnimationResource(GearDiverEntity animatable)
	{

        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/gear_diver.animation.json");
    }
}
