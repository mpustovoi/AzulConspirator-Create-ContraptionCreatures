package com.azul.CreateContraptionCreatures.entity.client.renders;

import com.azul.CreateContraptionCreatures.entity.client.models.SeedAmmoModel;
import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.SeedAmmoEntity;

import mod.azure.azurelib.cache.object.BakedGeoModel;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import mod.azure.azurelib.util.RenderUtils;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class SeedAmmoRenderer extends GeoEntityRenderer<SeedAmmoEntity> {

	public SeedAmmoRenderer(EntityRendererFactory.Context renderManagerIn) {
		super(renderManagerIn, new SeedAmmoModel());
	}

	@Override
	public void preRender(MatrixStack poseStack, SeedAmmoEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		poseStack.scale(0.0F, 0.0F, 0.0F);
		RenderUtils.faceRotation(poseStack, animatable, partialTick);
		super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
	}

}
