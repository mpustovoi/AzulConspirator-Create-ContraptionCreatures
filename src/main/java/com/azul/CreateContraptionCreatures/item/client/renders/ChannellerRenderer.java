package com.azul.CreateContraptionCreatures.item.client.renders;

import com.azul.CreateContraptionCreatures.item.client.models.*;
import com.azul.CreateContraptionCreatures.item.weapon.ChannellerItem;

import mod.azure.azurelib.renderer.GeoItemRenderer;

public class ChannellerRenderer extends GeoItemRenderer<ChannellerItem> {

	public ChannellerRenderer() {
		super(new ChannellerModel());
	}
}
