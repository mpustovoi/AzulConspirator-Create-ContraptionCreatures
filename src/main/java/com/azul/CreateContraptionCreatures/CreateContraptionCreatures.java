package com.azul.CreateContraptionCreatures;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azul.CreateContraptionCreatures.block.ModBlocks;
import com.azul.CreateContraptionCreatures.entity.ModEntity;
import com.azul.CreateContraptionCreatures.util.LocomotiveWorkerTrades;
import com.azul.CreateContraptionCreatures.util.MechanicTrades;
import com.azul.CreateContraptionCreatures.util.PlumberTrades;
import com.azul.CreateContraptionCreatures.villager.ModVillagers;

import mod.azure.azurelib.AzureLib;

public class CreateContraptionCreatures implements ModInitializer
{
	public static final String MOD_ID = "createcontraptioncreatures";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Hello from create : Contraption Creatures");
		// Register blocks
		AzureLib.initialize();
		// Register Mobs
		ModEntity.init();
		ModBlocks.registerModBlocks();
		// Register Villagers
		ModVillagers.registerVillagers();
		MechanicTrades.registerCustomTrades();
		PlumberTrades.registerCustomTrades();
		LocomotiveWorkerTrades.registerCustomTrades();

	}
}
