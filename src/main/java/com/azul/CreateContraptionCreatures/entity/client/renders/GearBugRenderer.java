package com.azul.CreateContraptionCreatures.entity.client.renders;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.client.models.GearBugModel;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.GearBugEntity;

import mod.azure.azurelib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class GearBugRenderer extends GeoEntityRenderer<GearBugEntity>
{
		private static final Identifier BASE_TEX = new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/gear_bug.png");

		public GearBugRenderer(EntityRendererFactory.Context renderManagerIn) {
			super(renderManagerIn, new GearBugModel());
		}

		@Override
		protected float getDeathMaxRotation(GearBugEntity entityLivingBaseIn) {
			return 0.0F;
		}

		@Override
		public Identifier getTextureLocation(GearBugEntity object)
		{
/* 			if (object.hasAngerTime())
			{
				return ANGRY_TEX;
			} */
			return BASE_TEX;
		}

		@Override
		public void render(GearBugEntity entity, float entityYaw, float partialTick, MatrixStack poseStack,VertexConsumerProvider bufferSource, int packedLight) {
			super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
		}

}
