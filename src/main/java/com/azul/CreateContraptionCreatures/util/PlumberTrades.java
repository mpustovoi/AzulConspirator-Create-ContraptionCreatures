package com.azul.CreateContraptionCreatures.util;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import com.azul.CreateContraptionCreatures.villager.ModVillagers;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;

public class PlumberTrades
{
    public static void registerCustomTrades()
    {
        TradeOfferHelper.registerVillagerOffers(ModVillagers.PLUMBER, 1,
                factories -> {
                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 2),
							new ItemStack(AllItems.COPPER_SHEET.get(),8),
							8,8,0.02F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.DRIED_KELP, 20),
							new ItemStack(Items.EMERALD, 1),
							8,8,0.01F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 2),
							new ItemStack(AllBlocks.FLUID_PIPE.get(),6),
							10,8,0.02F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.COPPER_INGOT, 6),
							new ItemStack(Items.EMERALD, 1),
							10,8,0.02F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 3),
							new ItemStack(AllItems.COPPER_DIVING_HELMET.get(),1),
							3,24,0.04F));

                });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.PLUMBER, 2,
                factories -> {
                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 2),
							new ItemStack(AllBlocks.COPPER_CASING.get(),1),
							8,8,0.1F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 4),
							new ItemStack(AllBlocks.SPOUT.get(),1),
							10,12,0.1F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 2),
							new ItemStack(AllBlocks.FLUID_TANK.get(),1),
							10,10,0.1F));

						factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 3),
                    		new ItemStack(AllBlocks.MECHANICAL_PUMP.get(),2),
							10,10,0.1F));

        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.PLUMBER, 3,
                factories -> {
                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(AllBlocks.COPPER_CASING.get(), 4),
							new ItemStack(Items.EMERALD, 1),
							10,8,0.02F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 1),
							new ItemStack(AllBlocks.FLUID_TANK.get(), 3),
							10,10,0.01F));

						factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 4),
							new ItemStack(AllBlocks.MECHANICAL_MIXER.get(), 1),
							8,12,0.01F));

        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.PLUMBER, 4,
        factories -> {
                        factories.add((entity, random) -> new TradeOffer(
								new ItemStack(Items.EMERALD, 2),
								new ItemStack(AllBlocks.FLUID_VALVE.get(), 3),
								4,16,0.01F));

                        factories.add((entity, random) -> new TradeOffer(
								new ItemStack(Items.EMERALD, 1),
								new ItemStack(AllBlocks.LARGE_WATER_WHEEL.get(), 3),
								6,10,0.01F));

        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.PLUMBER, 5,
        factories -> {
                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 1),
							new ItemStack(AllItems.HONEYED_APPLE.get(), 8),
							3,16,0.01F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 4),
							new ItemStack(AllItems.COPPER_DIVING_BOOTS.get(),1),
							3,16,0.02F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 7),
							new ItemStack(AllItems.COPPER_BACKTANK.get(),1),
							3,16,0.02F));
        });
    }
}
