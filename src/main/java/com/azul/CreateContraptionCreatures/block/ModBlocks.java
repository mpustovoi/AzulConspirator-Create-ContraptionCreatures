package com.azul.CreateContraptionCreatures.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.simibubi.create.AllBlocks;

import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block MECHANIC_TABLE = registerBlock("mechanic_table", new Block(FabricBlockSettings.copyOf(AllBlocks.SCHEMATIC_TABLE.get()).sounds(BlockSoundGroup.ANVIL)));
	public static final Block PLUMBER_TABLE = registerBlock("plumber_table", new Block(FabricBlockSettings.copyOf(AllBlocks.SCHEMATIC_TABLE.get()).sounds(BlockSoundGroup.ANVIL)));
	public static final Block LOCOMOTIVE_TABLE = registerBlock("locomotive_table", new Block(FabricBlockSettings.copyOf(AllBlocks.SCHEMATIC_TABLE.get()).sounds(BlockSoundGroup.ANVIL)));

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(CreateContraptionCreatures.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(CreateContraptionCreatures.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
    }


    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries)
    {
        entries.add(MECHANIC_TABLE);
		entries.add(PLUMBER_TABLE);
		entries.add(LOCOMOTIVE_TABLE);
    }

    public static void registerModBlocks()
    {
        CreateContraptionCreatures.LOGGER.info("Registering ModBlocks for " + CreateContraptionCreatures.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(ModBlocks::addItemsToIngredientItemGroup);
    }
}
