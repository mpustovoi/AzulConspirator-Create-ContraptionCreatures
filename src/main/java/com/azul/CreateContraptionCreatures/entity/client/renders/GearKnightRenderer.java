package com.azul.CreateContraptionCreatures.entity.client.renders;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.GearKnightEntity;
import com.azul.CreateContraptionCreatures.entity.client.models.*;
import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class GearKnightRenderer extends GeoEntityRenderer<GearKnightEntity>
{
		private static final Identifier BASE_TEX = new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/gear_knight.png");

		public GearKnightRenderer(EntityRendererFactory.Context renderManagerIn) {
			super(renderManagerIn, new GearKnightModel());
		}

		@Override
		protected float getDeathMaxRotation(GearKnightEntity entityLivingBaseIn) {
			return 0.0F;
		}

		@Override
		public Identifier getTextureLocation(GearKnightEntity object)
		{
/* 			if (object.hasAngerTime())
			{
				return ANGRY_TEX;
			} */
			return BASE_TEX;
		}

		@Override
		public void render(GearKnightEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,VertexConsumerProvider bufferSource, int packedLight) {
			super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
		}

}
