package com.azul.CreateContraptionCreatures.entity.client.renders;


import com.azul.CreateContraptionCreatures.entity.client.models.*;
import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.MelonAmmoEntity;

import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.azurelib.util.RenderUtils;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class MelonAmmoRenderer extends GeoEntityRenderer<MelonAmmoEntity>
{

	public MelonAmmoRenderer(EntityRendererFactory.Context renderManagerIn) {
		super(renderManagerIn, new MelonAmmoModel());
	}

	@Override
	public void preRender(MatrixStack poseStack, MelonAmmoEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		RenderUtils.faceRotation(poseStack, animatable, partialTick);
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

}
