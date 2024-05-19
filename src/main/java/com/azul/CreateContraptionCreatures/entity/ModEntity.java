package com.azul.CreateContraptionCreatures.entity;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Combatants.*;
import com.azul.CreateContraptionCreatures.entity.custom.Gatherers.*;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntity
{


	public static final EntityType<AutoDrillerEntity> AUTO_DRILLER = register("auto_driller", 1250067, 3092271,
	FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, AutoDrillerEntity::new).dimensions(EntityDimensions.fixed(1f, 1.3f)).build());
	public static final EntityType<AutoLumbererEntity> AUTO_LUMBERER = register("auto_lumberer", 1250067, 3092271,
	FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, AutoLumbererEntity::new).dimensions(EntityDimensions.fixed(1f, 1.3f)).build());
	public static final EntityType<AutoPorterEntity> AUTO_PORTER = register("auto_porter", 1250067, 3092271,
	FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, AutoPorterEntity::new).dimensions(EntityDimensions.fixed(1f, 1.3f)).build());
	//
	public static final EntityType<GearBugEntity> GEAR_BUG = register("gear_bug", 10065300, 3092271,
	FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GearBugEntity::new).dimensions(EntityDimensions.fixed(0.3f, 0.5f)).build());
	public static final EntityType<GearDummyEntity> GEAR_DUMMY = register("gear_dummy", 10065300, 3092271,
	FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GearDummyEntity::new).dimensions(EntityDimensions.fixed(1f, 2f)).build());
	public static final EntityType<GearKnightEntity> GEAR_KNIGHT = register("gear_knight", 10065300, 3092271,
	FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GearKnightEntity::new).dimensions(EntityDimensions.fixed(1f, 2f)).build());
	public static final EntityType<GearDiverEntity> GEAR_DIVER = register("gear_diver", 10065300, 3092271,
	FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GearDiverEntity::new).dimensions(EntityDimensions.fixed(1f, 2f)).build());

	public static final EntityType<GearMarrowEntity> GEAR_MARROW = register("gear_marrow", 10065300, 3092271,
	FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GearMarrowEntity::new).dimensions(EntityDimensions.fixed(1f, 2.7f)).build());


	// Credit to AdventureZ, it compresses the mob registry code by a lot
	private static <T extends Entity> EntityType<T> register(String id, int primaryColor, int secondaryColor, EntityType<T> entityType)
	{
		if (primaryColor != 0)
		{
			@SuppressWarnings("unchecked")
			Item item = Registry.register(Registries.ITEM, new Identifier(CreateContraptionCreatures.MOD_ID,id),
					new SpawnEggItem((EntityType<? extends MobEntity>) entityType, primaryColor, secondaryColor, new Item.Settings()));
			ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(entries -> entries.add(item));
		}
		return Registry.register(Registries.ENTITY_TYPE, new Identifier(CreateContraptionCreatures.MOD_ID, id), entityType);
	}

	public static void init()
	{
		FabricDefaultAttributeRegistry.register(ModEntity.AUTO_DRILLER, AutoDrillerEntity.createGathererDrillerAttributes());
		FabricDefaultAttributeRegistry.register(ModEntity.AUTO_LUMBERER, AutoLumbererEntity.createGathererLumbererAttributes());
		FabricDefaultAttributeRegistry.register(ModEntity.AUTO_PORTER, AutoPorterEntity.createGathererPorterAttributes());

		FabricDefaultAttributeRegistry.register(ModEntity.GEAR_BUG, GearBugEntity.createCombatantGearBugAttributes());

		FabricDefaultAttributeRegistry.register(ModEntity.GEAR_DUMMY, GearDummyEntity.createCombatantGearDummyAttributes());
		FabricDefaultAttributeRegistry.register(ModEntity.GEAR_KNIGHT, GearKnightEntity.createCombatantGearKnightAttributes());
		FabricDefaultAttributeRegistry.register(ModEntity.GEAR_DIVER, GearDiverEntity.createCombatantGearDiverAttributes());

		FabricDefaultAttributeRegistry.register(ModEntity.GEAR_MARROW, GearMarrowEntity.createCombatantGearMarrowAttributes());
	}
}
