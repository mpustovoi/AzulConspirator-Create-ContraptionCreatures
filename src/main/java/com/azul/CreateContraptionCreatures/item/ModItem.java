package com.azul.CreateContraptionCreatures.item;

import java.util.LinkedList;
import java.util.List;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.*;
import com.azul.CreateContraptionCreatures.item.weapon.MelonLobberItem;
import com.azul.CreateContraptionCreatures.item.weapon.PeaShooterItem;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItem
{
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<EntityType<? extends Entity>> ENTITY_TYPES = new LinkedList();
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<EntityType<? extends Entity>> ENTITY_THAT_USE_ITEM_RENDERS = new LinkedList();

	public static final Identifier RELOAD_BULLETS = new Identifier(CreateContraptionCreatures.MOD_ID, "reload_bullets");
	public static final Identifier FIRE_WEAPON = new Identifier(CreateContraptionCreatures.MOD_ID, "fire_weapon");

	public static final EntityType<SeedAmmoEntity> SEED_BULLETS = projectile(SeedAmmoEntity::new, "sharpen_seeds");
	public static final EntityType<MelonAmmoEntity> MELON_BULLETS = projectile(MelonAmmoEntity::new, "thrown_melon");

	public static final PeaShooterItem PEA_SHOOTER = item(new PeaShooterItem(2,32,7, Items.WHEAT_SEEDS,false), "pea_shooter");
	public static final MelonLobberItem MELON_LOBBER = item(new MelonLobberItem(10,1,25, Items.MELON,false), "melon_lobber");

	public static void init()
	{
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItem::addItemsToIngredientItemGroup);
	}

	private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries)
	{
		entries.add(PEA_SHOOTER);
		entries.add(MELON_LOBBER);
    }

	private static <T extends Entity> EntityType<T> projectile(EntityType.EntityFactory<T> factory, String id)
	{
		EntityType<T> type = FabricEntityTypeBuilder.<T>create(SpawnGroup.MISC, factory).dimensions(new EntityDimensions(0.5F, 0.5F, true)).disableSummon().spawnableFarFromPlayer().trackRangeBlocks(90).trackedUpdateRate(1).build();

		Registry.register(Registries.ENTITY_TYPE, new Identifier(CreateContraptionCreatures.MOD_ID, id), type);

		ENTITY_TYPES.add(type);
		ENTITY_THAT_USE_ITEM_RENDERS.add(type);

		return type;
	}
	static <T extends Item> T item(T c, String id) {
		Registry.register(Registries.ITEM, new Identifier(CreateContraptionCreatures.MOD_ID, id), c);
		return c;
	}
}


