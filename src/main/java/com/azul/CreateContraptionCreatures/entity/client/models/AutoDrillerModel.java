package com.azul.CreateContraptionCreatures.entity.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Gatherers.AutoDrillerEntity;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class AutoDrillerModel extends GeoModel<AutoDrillerEntity>
{
	private static final Identifier BASE_TEX = new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/auto_driller.png");
	@SuppressWarnings("unused")
	private static final Identifier ANGRY_TEX = new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/auto_driller_angry.png");

    @Override
    public Identifier getModelResource(AutoDrillerEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/auto_driller.geo.json");
    }

    @Override
    public Identifier getTextureResource(AutoDrillerEntity object)
	{
/*         if (object.hasAngerTime())
		{
			return ANGRY_TEX;
		}
		else */
			return BASE_TEX;
    }

    @Override
    public Identifier getAnimationResource(AutoDrillerEntity animatable)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/auto_driller.animation.json");
    }
}
