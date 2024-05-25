package com.azul.CreateContraptionCreatures;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Hand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azul.CreateContraptionCreatures.block.ModBlocks;
import com.azul.CreateContraptionCreatures.entity.ModEntity;
import com.azul.CreateContraptionCreatures.item.ModItem;
import com.azul.CreateContraptionCreatures.item.weapon.ContraptionBaseGunItem;
import com.azul.CreateContraptionCreatures.util.LocomotiveWorkerTrades;
import com.azul.CreateContraptionCreatures.util.MechanicTrades;
import com.azul.CreateContraptionCreatures.util.PlumberTrades;
import com.azul.CreateContraptionCreatures.villager.ModVillagers;
import com.azul.CreateContraptionCreatures.world.gen.ModEntityGeneration;

import mod.azure.azurelib.AzureLib;

public class CreateContraptionCreatures implements ModInitializer
{
	public static final String MOD_ID = "createcontraptioncreatures";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Hello from create : Contraption Creatures");
		AzureLib.initialize();


		// Register Mobs
		ModEntity.init();
		// register Block

		ModBlocks.registerModBlocks();
		//Register Item
		ModItem.init();

		// Register Villagers
		ModVillagers.registerVillagers();
		MechanicTrades.registerCustomTrades();
		PlumberTrades.registerCustomTrades();
		LocomotiveWorkerTrades.registerCustomTrades();

		//Register Spawn
		ModEntityGeneration.addSpawns();

		ServerPlayNetworking.registerGlobalReceiver(ModItem.RELOAD_BULLETS, (server, player, serverPlayNetworkHandler, inputPacket, packetSender) ->
		{
            if (player.getMainHandStack().getItem() instanceof ContraptionBaseGunItem)
                ((ContraptionBaseGunItem) player.getMainHandStack().getItem()).reloadBullets(player, Hand.MAIN_HAND);
        });

		ServerPlayNetworking.registerGlobalReceiver(ModItem.FIRE_MODE, (server, player, serverPlayNetworkHandler, inputPacket, packetSender) ->
		{
            if (player.getMainHandStack().getItem() instanceof ContraptionBaseGunItem)
                ((ContraptionBaseGunItem) player.getMainHandStack().getItem()).SetFireMode(player, player.getMainHandStack());
        });

	}
}
