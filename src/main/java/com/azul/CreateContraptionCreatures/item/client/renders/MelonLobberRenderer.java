package com.azul.CreateContraptionCreatures.item.client.renders;

import com.azul.CreateContraptionCreatures.item.client.models.*;
import com.azul.CreateContraptionCreatures.item.weapon.MelonLobberItem;

import mod.azure.azurelib.renderer.GeoItemRenderer;

public class MelonLobberRenderer extends GeoItemRenderer<MelonLobberItem> {

	public MelonLobberRenderer() {
		super(new MelonLobberModel());
	}
}
