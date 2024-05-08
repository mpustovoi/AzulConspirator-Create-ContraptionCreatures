package com.azul.CreateContraptionCreatures;

import com.azul.CreateContraptionCreatures.block.ModBlocks;
import com.azul.CreateContraptionCreatures.entity.ModEntity;
import com.azul.CreateContraptionCreatures.entity.client.renders.AutoDrillerRenderer;
import com.azul.CreateContraptionCreatures.entity.client.renders.AutoLumbererRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;


@Environment(EnvType.CLIENT)
public class CreateContraptionCreaturesClient implements ClientModInitializer
{
	@Override
    public void onInitializeClient()
	{
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MECHANIC_TABLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PLUMBER_TABLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCOMOTIVE_TABLE, RenderLayer.getCutout());

		EntityRendererRegistry.register(ModEntity.AUTO_DRILLER, AutoDrillerRenderer::new);
		EntityRendererRegistry.register(ModEntity.AUTO_LUMBERER, AutoLumbererRenderer::new);
    }
}
