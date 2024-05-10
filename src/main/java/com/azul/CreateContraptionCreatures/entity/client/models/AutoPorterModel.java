package com.azul.CreateContraptionCreatures.entity.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Gatherers.AutoPorterEntity;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class AutoPorterModel extends GeoModel<AutoPorterEntity>
{

    @Override
    public Identifier getModelResource(AutoPorterEntity object)
	 {
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/auto_porter.geo.json");
    }

    @Override
    public Identifier getTextureResource(AutoPorterEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/auto_porter.png");
    }

    @Override
    public Identifier getAnimationResource(AutoPorterEntity animatable)
	 {
        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/auto_porter.animation.json");
    }
}
