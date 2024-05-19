package com.azul.CreateContraptionCreatures.entity.client.models;
import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.SeedAmmoEntity;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class SeedAmmoModel extends GeoModel<SeedAmmoEntity>
{
    @Override
    public Identifier getModelResource(SeedAmmoEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/projectile/seedammo.geo.json");
    }

    @Override
    public Identifier getTextureResource(SeedAmmoEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/item/seedammo.png");
    }

    @Override
    public Identifier getAnimationResource(SeedAmmoEntity animatable)
	{

        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/projectile/seedammo.animation.json");
    }
}
