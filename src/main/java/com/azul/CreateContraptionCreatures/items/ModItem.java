package com.azul.CreateContraptionCreatures.items;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.ModEntity;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItem
{
    public static final Item AUTO_DRILLER_SPAWN_EGG = registerItem("auto_driller_spawn_egg",
            new SpawnEggItem(ModEntity.AUTO_DRILLER, 0xa86518, 0x3b260f, new FabricItemSettings()));
	public static final Item AUTO_LUMBERER_SPAWN_EGG = registerItem("auto_lumberer_spawn_egg",
            new SpawnEggItem(ModEntity.AUTO_LUMBERER, 0xa86518, 0x3b260f, new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(CreateContraptionCreatures.MOD_ID, name), item);
    }

	private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries)
    {
        entries.add(AUTO_DRILLER_SPAWN_EGG);
		entries.add(AUTO_LUMBERER_SPAWN_EGG);
    }
    public static void registerModItems() {
        CreateContraptionCreatures.LOGGER.info("Registering Mod Items for " + CreateContraptionCreatures.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(ModItem::addItemsToIngredientItemGroup);
    }
}
