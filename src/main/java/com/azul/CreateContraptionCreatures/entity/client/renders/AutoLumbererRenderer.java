package com.azul.CreateContraptionCreatures.entity.client.renders;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.client.models.AutoLumbererModel;
import com.azul.CreateContraptionCreatures.entity.custom.Gatherers.AutoLumbererEntity;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class AutoLumbererRenderer extends GeoEntityRenderer<AutoLumbererEntity>
{
		private static final Identifier BASE_TEX = new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/auto_lumberer.png");

		public AutoLumbererRenderer(EntityRendererFactory.Context renderManagerIn) {
			super(renderManagerIn, new AutoLumbererModel());
		}

		@Override
		protected float getDeathMaxRotation(AutoLumbererEntity entityLivingBaseIn) {
			return 0.0F;
		}

		@Override
		public Identifier getTextureLocation(AutoLumbererEntity object)
		{
/* 			if (object.hasAngerTime())
			{
				return ANGRY_TEX;
			} */
			return BASE_TEX;
		}

		@Override
		public void render(AutoLumbererEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,VertexConsumerProvider bufferSource, int packedLight) {
			super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
		}

}
