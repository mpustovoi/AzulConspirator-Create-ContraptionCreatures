package com.azul.CreateContraptionCreatures.entity.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Gatherers.AutoLumbererEntity;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class AutoLumbererModel extends GeoModel<AutoLumbererEntity>
{

    @Override
    public Identifier getModelResource(AutoLumbererEntity object)
	 {
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/auto_lumberer.geo.json");
    }

    @Override
    public Identifier getTextureResource(AutoLumbererEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/auto_lumberer.png");
    }

    @Override
    public Identifier getAnimationResource(AutoLumbererEntity animatable)
	 {

        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/auto_lumberer.animation.json");
    }
}
