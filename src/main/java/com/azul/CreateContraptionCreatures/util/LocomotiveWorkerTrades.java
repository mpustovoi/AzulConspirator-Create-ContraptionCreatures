package com.azul.CreateContraptionCreatures.util;

import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import com.azul.CreateContraptionCreatures.villager.ModVillagers;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;

public class LocomotiveWorkerTrades
{
    public static void registerCustomTrades()
    {
        TradeOfferHelper.registerVillagerOffers(ModVillagers.LOCOMOTIVEWORKER, 1,
                factories -> {
                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 3),
							new ItemStack(Items.IRON_INGOT,7),
							8,8,0.02F));

						factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 3),
							new ItemStack(AllBlocks.ANDESITE_ALLOY_BLOCK.get(),6),
							8,8,0.02F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 3),
							new ItemStack(AllBlocks.TRAIN_DOOR.get(),2),
							10,12,0.02F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 4),
							new ItemStack(AllBlocks.STEAM_WHISTLE.get(), 5),
							8,8,0.02F));

                });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.LOCOMOTIVEWORKER, 2,
                factories -> {
                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 3),
							new ItemStack(AllBlocks.TRACK.get(),12),
							8,8,0.02F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 3),
							new ItemStack(AllBlocks.TRACK_STATION.get(),1),
							8,8,0.1F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 3),
							new ItemStack(AllBlocks.DISPLAY_BOARD.get(),8),
							10,12,0.1F));

        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.LOCOMOTIVEWORKER, 3,
                factories -> {
                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(AllBlocks.TRACK.get(), 6),
							new ItemStack(Items.EMERALD, 1),
							8,8,0.01F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 2),
							new ItemStack(AllBlocks.DISPLAY_LINK.get(), 1),
							10,10,0.01F));

						factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 5),
							new ItemStack(AllItems.SCHEDULE.get(), 1),
							4,16,0.01F));


						factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 3),
							new ItemStack(AllBlocks.PLACARD.get(), 4),
							3,16,0.01F));

        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.LOCOMOTIVEWORKER, 4,
        factories -> {
                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 7),
							new ItemStack(AllBlocks.TRAIN_CONTROLS.get(), 1),
							4,12,0.01F));

                        factories.add((entity, random) -> new TradeOffer(
                            new ItemStack(Items.EMERALD, 5),
							new ItemStack(AllBlocks.RAILWAY_CASING.get(), 3),
							8,10,0.01F));

        });
        TradeOfferHelper.registerVillagerOffers(ModVillagers.LOCOMOTIVEWORKER, 5,
        factories -> {
                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 6),
							new ItemStack(AllBlocks.PORTABLE_STORAGE_INTERFACE.get(), 2),
							8,12,0.01F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 9),
							new ItemStack(AllBlocks.ITEM_VAULT.get(), 8),
							2,24,0.01F));

                        factories.add((entity, random) -> new TradeOffer(
							new ItemStack(Items.EMERALD, 4),
							new ItemStack(AllItems.STURDY_SHEET.get(), 3),
							8,12,0.01F));
        });
    }
}
