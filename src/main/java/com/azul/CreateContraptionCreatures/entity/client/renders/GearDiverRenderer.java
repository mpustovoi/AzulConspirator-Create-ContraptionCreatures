package com.azul.CreateContraptionCreatures.entity.client.renders;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.GearDiverEntity;
import com.azul.CreateContraptionCreatures.entity.client.models.*;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class GearDiverRenderer extends GeoEntityRenderer<GearDiverEntity>
{
		private static final Identifier BASE_TEX = new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/gear_diver.png");

		public GearDiverRenderer(EntityRendererFactory.Context renderManagerIn) {
			super(renderManagerIn, new GearDiverModel());
		}

		@Override
		protected float getDeathMaxRotation(GearDiverEntity entityLivingBaseIn) {
			return 0.0F;
		}

		@Override
		public Identifier getTextureLocation(GearDiverEntity object)
		{
/* 			if (object.hasAngerTime())
			{
				return ANGRY_TEX;
			} */
			return BASE_TEX;
		}

		@Override
		public void render(GearDiverEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,VertexConsumerProvider bufferSource, int packedLight) {
			super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
		}

}
