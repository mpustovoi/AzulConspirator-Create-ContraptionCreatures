package com.azul.CreateContraptionCreatures.entity.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.GearDummyEntity;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class GearDummyModel extends GeoModel<GearDummyEntity>
{

    @Override
    public Identifier getModelResource(GearDummyEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/gear_dummy.geo.json");
    }

    @Override
    public Identifier getTextureResource(GearDummyEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/gear_dummy.png");
    }

    @Override
    public Identifier getAnimationResource(GearDummyEntity animatable)
	{

        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/gear_dummy.animation.json");
    }
}
