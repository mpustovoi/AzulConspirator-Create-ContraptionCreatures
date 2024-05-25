package com.azul.CreateContraptionCreatures.item.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.item.weapon.ChannellerItem;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class ChannellerModel extends GeoModel<ChannellerItem>
{
	@Override
	public Identifier getModelResource(ChannellerItem object) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/weapon/channeller.geo.json");
	}

	@Override
	public Identifier getTextureResource(ChannellerItem object) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/item/channeller.png");
	}

	@Override
	public Identifier getAnimationResource(ChannellerItem animatable) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/weapon/channeller.animation.json");
	}
}
