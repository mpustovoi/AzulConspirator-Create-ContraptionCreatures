package com.azul.CreateContraptionCreatures.item.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.item.weapon.FlamerItem;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class FlamerModel extends GeoModel<FlamerItem>
{
	@Override
	public Identifier getModelResource(FlamerItem object) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/weapon/flamer.geo.json");
	}

	@Override
	public Identifier getTextureResource(FlamerItem object) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/item/flamer.png");
	}

	@Override
	public Identifier getAnimationResource(FlamerItem animatable) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/weapon/flamer.animation.json");
	}
}
