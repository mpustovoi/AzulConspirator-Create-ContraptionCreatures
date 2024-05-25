package com.azul.CreateContraptionCreatures.entity.client.renders;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;

public class EmptyRenderer extends ProjectileEntityRenderer<PersistentProjectileEntity>
{
    private static final Identifier BASE_TEX = new Identifier(CreateContraptionCreatures.MOD_ID, "textures/item/empty.png");

    public EmptyRenderer(Context dispatcher)
	{
        super(dispatcher);
    }

    @Override
    public Identifier getTexture(PersistentProjectileEntity entity)
	{
        return BASE_TEX;
    }

    @Override
    public void render(PersistentProjectileEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
		matrices.push();
        matrices.scale(0, 0, 0);
        matrices.pop();
    }
}
