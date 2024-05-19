package com.azul.CreateContraptionCreatures;

import com.azul.CreateContraptionCreatures.block.ModBlocks;
import com.azul.CreateContraptionCreatures.entity.ModEntity;
import com.azul.CreateContraptionCreatures.entity.client.renders.*;
import com.azul.CreateContraptionCreatures.item.ModItem;
import com.azul.CreateContraptionCreatures.util.handler.ContraptionGunRenderHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;


@Environment(EnvType.CLIENT)
public class CreateContraptionCreaturesClient implements ClientModInitializer
{

	public static final ContraptionGunRenderHandler CONTRAPTION_GUN_RENDER_HANDLER = new ContraptionGunRenderHandler();

	@Override
    public void onInitializeClient()
	{
		CONTRAPTION_GUN_RENDER_HANDLER.registerListeners();
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
		EntityRendererRegistry.register(ModEntity.GEAR_MARROW, GearMarrowRenderer::new);

		//Projectiles
		EntityRendererRegistry.register(ModItem.SEED_BULLETS, (ctx) -> new SeedAmmoRenderer(ctx));
		EntityRendererRegistry.register(ModItem.MELON_BULLETS, (ctx) -> new MelonAmmoRenderer(ctx));
    }
}
