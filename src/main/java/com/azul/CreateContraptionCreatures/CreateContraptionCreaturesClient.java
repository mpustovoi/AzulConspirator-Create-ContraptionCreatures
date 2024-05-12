package com.azul.CreateContraptionCreatures;

import com.azul.CreateContraptionCreatures.block.ModBlocks;
import com.azul.CreateContraptionCreatures.entity.ModEntity;
import com.azul.CreateContraptionCreatures.entity.client.renders.*;

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
		//Villager Table
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.MECHANIC_TABLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.PLUMBER_TABLE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.LOCOMOTIVE_TABLE, RenderLayer.getCutout());
		//Gatherers
		EntityRendererRegistry.register(ModEntity.AUTO_DRILLER, AutoDrillerRenderer::new);
		EntityRendererRegistry.register(ModEntity.AUTO_LUMBERER, AutoLumbererRenderer::new);
		EntityRendererRegistry.register(ModEntity.AUTO_PORTER, AutoPorterRenderer::new);
		//Combatants
		EntityRendererRegistry.register(ModEntity.GEAR_BUG, GearBugRenderer::new);
		EntityRendererRegistry.register(ModEntity.GEAR_DUMMY, GearDummyRenderer::new);
		EntityRendererRegistry.register(ModEntity.GEAR_DIVER, GearDiverRenderer::new);
		EntityRendererRegistry.register(ModEntity.GEAR_KNIGHT, GearKnightRenderer::new);
    }
}
