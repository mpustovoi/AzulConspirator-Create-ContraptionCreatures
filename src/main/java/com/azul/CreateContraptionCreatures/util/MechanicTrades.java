package com.azul.CreateContraptionCreatures.util;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import com.azul.CreateContraptionCreatures.villager.ModVillagers;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;

public class MechanicTrades 
{
    public static void registerCustomTrades() 
    {
        TradeOfferHelper.registerVillagerOffers(ModVillagers.MECHANIC, 1,
                factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 2),
                                new ItemStack(AllItems.ANDESITE_ALLOY.get(),8),
                                8,8,0.02F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.ANDESITE, 20),
                                new ItemStack(Items.EMERALD, 1),
                                8,8,0.01F));
                                
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 1),
                                new ItemStack(AllItems.IRON_SHEET.get(),4),
                                10,8,0.02F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.ANDESITE, 20),
                                new ItemStack(Items.EMERALD, 1),
                                8,8,0.01F));

                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(AllItems.RAW_ZINC.get(), 6),
                                new ItemStack(Items.EMERALD, 1),
                                10,8,0.02F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 5),
                                new ItemStack(AllItems.WRENCH.get(),1),
                                3,24,0.04F));

                });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.MECHANIC, 2,
                factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 3),
                                new ItemStack(AllBlocks.MECHANICAL_BEARING.get(),1),
                                8,8,0.02F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 6),
                                new ItemStack(AllItems.GOGGLES.get(),1),
                                3,32,0.1F));
                                
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(AllBlocks.COGWHEEL.get(), 14),
                                new ItemStack(Items.EMERALD, 1),
                                10,8,0.02F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 3),
                                new ItemStack(AllBlocks.WATER_WHEEL.get(),4),
                                10,12,0.1F));

                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 2),
                                new ItemStack(AllBlocks.BASIN.get(),1),
                                10,12,0.1F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 2),
                                new ItemStack(AllBlocks.DEPOT.get(),1),
                                10,10,0.1F));

        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.MECHANIC, 3,
                factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(AllBlocks.BELT.get(), 10),
                                new ItemStack(Items.EMERALD, 3),
                                10,10,0.01F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 6),
                                new ItemStack(AllBlocks.STRESSOMETER.get(), 1),
                                3,16,0.01F));

        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.MECHANIC, 4,
        factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 8),
                                new ItemStack(AllBlocks.SPEEDOMETER.get(), 1),
                                4,38,0.01F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(AllBlocks.LARGE_WATER_WHEEL.get(), 4),
                                new ItemStack(Items.EMERALD, 1),
                                6,10,0.01F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 5),
                                new ItemStack(AllBlocks.CRUSHING_WHEEL.get(), 1),
                                8,28,0.01F));

        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.MECHANIC, 5,
        factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 9),
                                new ItemStack(AllBlocks.MECHANICAL_ROLLER.get(), 2),
                                2,24,0.01F));
                        
                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 10),
                                new ItemStack(AllBlocks.MECHANICAL_ARM.get(), 1),
                                3,40,0.01F));

                        factories.add((entity, random) -> new TradeOffer(
                                new ItemStack(Items.EMERALD, 3),
                                new ItemStack(AllBlocks.MECHANICAL_CRAFTER.get(), 2),
                                8,12,0.01F));
        });
    }
}
