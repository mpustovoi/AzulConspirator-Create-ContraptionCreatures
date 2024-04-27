package com.azul.CreateContraptionCreatures.villager;

import com.google.common.collect.ImmutableSet;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

public class ModVillagers 
{
    public static final RegistryKey<PointOfInterestType> MECHANIC_POI_KEY = poiKey("ccc_mechanicpoi");
    public static final PointOfInterestType MECHANIC_POI = registerPoi("ccc_mechanicpoi", ModBlocks.MECHANIC_TABLE);
    public static final VillagerProfession MECHANIC = registerProfession("ccc_mechanic", MECHANIC_POI_KEY);


    private static VillagerProfession registerProfession(String name, RegistryKey<PointOfInterestType> type) 
    {
        return Registry.register(Registries.VILLAGER_PROFESSION, new Identifier(CreateContraptionCreatures.MOD_ID, name),
                new VillagerProfession(name, entry -> entry.matchesKey(type), entry -> entry.matchesKey(type),
                        ImmutableSet.of(), ImmutableSet.of(), SoundEvents.ENTITY_VILLAGER_WORK_ARMORER));
    }

    private static PointOfInterestType registerPoi(String name, Block block) {
        return PointOfInterestHelper.register(new Identifier(CreateContraptionCreatures.MOD_ID, name), 1, 1, block);
    }

    private static RegistryKey<PointOfInterestType> poiKey(String name) {
        return RegistryKey.of(RegistryKeys.POINT_OF_INTEREST_TYPE, new Identifier(CreateContraptionCreatures.MOD_ID, name));
    }

    public static void registerVillagers() {
        CreateContraptionCreatures.LOGGER.info("Registering Villagers " + CreateContraptionCreatures.MOD_ID);
    }
}
