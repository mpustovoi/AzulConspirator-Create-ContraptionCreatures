package com.azul.CreateContraptionCreatures;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.azul.CreateContraptionCreatures.block.ModBlocks;
import com.azul.CreateContraptionCreatures.util.LocomotiveWorkerTrades;
import com.azul.CreateContraptionCreatures.util.MechanicTrades;
import com.azul.CreateContraptionCreatures.util.PlumberTrades;
import com.azul.CreateContraptionCreatures.villager.ModVillagers;

public class CreateContraptionCreatures implements ModInitializer
{
	public static final String MOD_ID = "createcontraptioncreatures";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Hello from create : Contraption Creatures");
		ModBlocks.registerModBlocks();

		MechanicTrades.registerCustomTrades();
		PlumberTrades.registerCustomTrades();
		LocomotiveWorkerTrades.registerCustomTrades();

		ModVillagers.registerVillagers();
	}
}
