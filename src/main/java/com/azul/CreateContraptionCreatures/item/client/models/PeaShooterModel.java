package com.azul.CreateContraptionCreatures.item.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.item.weapon.PeaShooterItem;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class PeaShooterModel extends GeoModel<PeaShooterItem>
{
	@Override
	public Identifier getModelResource(PeaShooterItem object) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/weapon/pea_shooter.geo.json");
	}

	@Override
	public Identifier getTextureResource(PeaShooterItem object) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/item/pea_shooter.png");
	}

	@Override
	public Identifier getAnimationResource(PeaShooterItem animatable) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/weapon/pea_shooter.animation.json");
	}
}
