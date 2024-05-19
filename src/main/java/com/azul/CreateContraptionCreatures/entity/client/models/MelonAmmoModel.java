package com.azul.CreateContraptionCreatures.entity.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.MelonAmmoEntity;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class MelonAmmoModel extends GeoModel<MelonAmmoEntity>
{
    @Override
    public Identifier getModelResource(MelonAmmoEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/projectile/melonammo.geo.json");
    }

    @Override
    public Identifier getTextureResource(MelonAmmoEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/item/melonammo.png");
    }

    @Override
    public Identifier getAnimationResource(MelonAmmoEntity animatable)
	{

        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/projectile/melonammo.animation.json");
    }
}
