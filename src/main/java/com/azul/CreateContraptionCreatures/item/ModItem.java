package com.azul.CreateContraptionCreatures.item;

import java.util.LinkedList;
import java.util.List;

import com.azul.CreateContraptionCreatures.CreateContraptionCreatures;
import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.*;
import com.azul.CreateContraptionCreatures.item.ammo.LensItem;
import com.azul.CreateContraptionCreatures.item.weapon.*;

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
	public static final Identifier FIRE_MODE = new Identifier(CreateContraptionCreatures.MOD_ID, "fire_mode");

	public static final EntityType<SeedAmmoEntity> SEED_BULLETS = projectile(SeedAmmoEntity::new, "sharpen_seeds");
	public static final EntityType<MelonAmmoEntity> MELON_BULLETS = projectile(MelonAmmoEntity::new, "thrown_melon");
	public static final EntityType<FlameBlastEntity> FLAME_BLAST = projectile(FlameBlastEntity::new, "flame_blast");
	public static final EntityType<EchoBlastEntity> ECHO_BLAST = projectile(EchoBlastEntity::new, "echo_blast");
	public static final EntityType<TeleBulletEntity> TELE_BULLET = projectile(TeleBulletEntity::new, "tele_bullet");

	public static final PeaShooterItem PEA_SHOOTER = item(new PeaShooterItem(2,32,7, Items.WHEAT_SEEDS,false), "pea_shooter");
	public static final MelonLobberItem MELON_LOBBER = item(new MelonLobberItem(10,1,25, Items.MELON,false), "melon_lobber");
	public static final FlamerItem FLAMER = item(new FlamerItem(4,3,22, Items.BLAZE_ROD,false), "flamer");
	public static final ChannellerItem CHANNELLER = item(new ChannellerItem(4,1,22, Items.GLOW_BERRIES,false), "channeller");

	public static final Item FLAME_LENS = item(new LensItem(),"flame_lens");
	public static final Item ECHO_LENS = item(new LensItem(),"echo_lens");
	public static final Item TELEPORT_LENS = item(new LensItem(),"teleport_lens");

	public static void init()
	{
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItem::addItemsToIngredientItemGroup);
	}

	private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries)
	{
		entries.add(PEA_SHOOTER);
		entries.add(MELON_LOBBER);
		entries.add(FLAMER);
		entries.add(CHANNELLER);
		entries.add(FLAME_LENS);
		entries.add(ECHO_LENS);
		entries.add(TELEPORT_LENS);
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


