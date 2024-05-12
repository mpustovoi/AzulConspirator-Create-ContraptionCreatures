package com.azul.CreateContraptionCreatures.entity.client.models;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.GearKnightEntity;

import mod.azure.azurelib.constant.DataTickets;
import mod.azure.azurelib.core.animatable.model.CoreGeoBone;
import mod.azure.azurelib.core.animation.AnimationState;
import mod.azure.azurelib.model.GeoModel;
import mod.azure.azurelib.model.data.EntityModelData;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class GearKnightModel extends GeoModel<GearKnightEntity>
{

    @Override
    public Identifier getModelResource(GearKnightEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "geo/gear_knight.geo.json");
    }

    @Override
    public Identifier getTextureResource(GearKnightEntity object)
	{
        return new Identifier(CreateContraptionCreatures.MOD_ID, "textures/entity/gear_knight.png");
    }

    @Override
    public Identifier getAnimationResource(GearKnightEntity animatable)
	{

        return new Identifier(CreateContraptionCreatures.MOD_ID, "animations/gear_knight.animation.json");
    }

	@Override
    public void setCustomAnimations(GearKnightEntity animatable, long instanceId, AnimationState<GearKnightEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
	}
}
