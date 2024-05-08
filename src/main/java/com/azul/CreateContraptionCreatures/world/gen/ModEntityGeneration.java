package com.azul.CreateContraptionCreatures.world.gen;

import com.azul.CreateContraptionCreatures.entity.ModEntity;
import com.azul.CreateContraptionCreatures.entity.custom.AbstractCogBotEntity;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.world.Heightmap;

public class ModEntityGeneration
{
    public static void addSpawns()
	{
        BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.CREATURE, ModEntity.AUTO_DRILLER, 60, 1, 2);
		BiomeModifications.addSpawn(BiomeSelectors.all(), SpawnGroup.CREATURE, ModEntity.AUTO_LUMBERER, 60, 1, 2);

        SpawnRestriction.register(ModEntity.AUTO_DRILLER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractCogBotEntity::canSpawnCog);
		SpawnRestriction.register(ModEntity.AUTO_LUMBERER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractCogBotEntity::canSpawnCog);
    }
}
