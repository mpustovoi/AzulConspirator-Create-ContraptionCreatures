package com.azul.CreateContraptionCreatures.item.client.renders;

import com.azul.CreateContraptionCreatures.item.client.models.*;
import com.azul.CreateContraptionCreatures.item.weapon.FlamerItem;

import mod.azure.azurelib.renderer.GeoItemRenderer;

public class FlamerRenderer extends GeoItemRenderer<FlamerItem> {

	public FlamerRenderer() {
		super(new FlamerModel());
	}
}
