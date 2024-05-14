package com.azul.CreateContraptionCreatures.world.gen;

import com.azul.CreateContraptionCreatures.entity.ModEntity;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.*;
import com.azul.CreateContraptionCreatures.entity.custom.Gatherers.*;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.world.Heightmap;

public class ModEntityGeneration
{
    public static void addSpawns()
	{
        BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_OVERWORLD), SpawnGroup.MONSTER, ModEntity.AUTO_DRILLER, 100, 1, 2);
		BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_FOREST), SpawnGroup.MONSTER, ModEntity.AUTO_LUMBERER, 100, 2, 3);
		BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_FOREST), SpawnGroup.MONSTER, ModEntity.AUTO_PORTER, 40, 2, 3);
		BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.IS_OVERWORLD), SpawnGroup.MONSTER, ModEntity.GEAR_BUG, 80, 2, 3);
		BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.NETHER_FORTRESS_HAS_STRUCTURE), SpawnGroup.MONSTER, ModEntity.GEAR_BUG, 80, 2, 3);
		BiomeModifications.addSpawn(BiomeSelectors.tag(BiomeTags.NETHER_FORTRESS_HAS_STRUCTURE), SpawnGroup.MONSTER, ModEntity.GEAR_MARROW, 20, 1, 2);

        SpawnRestriction.register(ModEntity.AUTO_DRILLER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AutoDrillerEntity::canSpawnCog);
		SpawnRestriction.register(ModEntity.AUTO_LUMBERER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AutoLumbererEntity::canSpawnCog);
		SpawnRestriction.register(ModEntity.AUTO_PORTER, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AutoPorterEntity::canSpawnCog);

		SpawnRestriction.register(ModEntity.GEAR_BUG, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GearBugEntity::canSpawnCog);
		SpawnRestriction.register(ModEntity.GEAR_MARROW, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GearMarrowEntity::canSpawnCog);
    }

}
