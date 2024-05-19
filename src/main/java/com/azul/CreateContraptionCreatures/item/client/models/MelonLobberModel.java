package com.azul.CreateContraptionCreatures.item.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.item.weapon.MelonLobberItem;

import mod.azure.azurelib.model.GeoModel;
import net.minecraft.util.Identifier;

public class MelonLobberModel extends GeoModel<MelonLobberItem>
{
	@Override
	public Identifier getModelResource(MelonLobberItem object) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/weapon/melon_lobber.geo.json");
	}

	@Override
	public Identifier getTextureResource(MelonLobberItem object) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/item/melon_lobber.png");
	}

	@Override
	public Identifier getAnimationResource(MelonLobberItem animatable) {
		return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/weapon/melon_lobber.animation.json");
	}
}
