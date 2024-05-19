package com.azul.CreateContraptionCreatures.item.client.renders;

import com.azul.CreateContraptionCreatures.item.client.models.PeaShooterModel;
import com.azul.CreateContraptionCreatures.item.weapon.PeaShooterItem;

import mod.azure.azurelib.renderer.GeoItemRenderer;

public class PeaShooterRenderer extends GeoItemRenderer<PeaShooterItem> {

	public PeaShooterRenderer() {
		super(new PeaShooterModel());
	}
}
