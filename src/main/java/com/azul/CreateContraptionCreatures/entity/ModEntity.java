package com.azul.CreateContraptionCreatures.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;

import com.azul.CreateContraptionCreatures.entity.custom.Gatherers.*;

public class ModEntity
{

	public static final EntityType<AutoDrillerEntity> AUTO_DRILLER = Registry.register(Registries.ENTITY_TYPE, new Identifier(CreateContraptionCreatures.MOD_ID, "auto_driller"), FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, AutoDrillerEntity::new).dimensions(EntityDimensions.fixed(1f, 1f)).build());

}
