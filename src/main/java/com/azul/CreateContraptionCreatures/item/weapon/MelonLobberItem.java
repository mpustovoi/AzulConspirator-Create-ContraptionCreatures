package com.azul.CreateContraptionCreatures.item.weapon;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.MelonAmmoEntity;
import com.azul.CreateContraptionCreatures.item.client.renders.MelonLobberRenderer;

import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.animatable.SingletonGeoAnimatable;
import mod.azure.azurelib.animatable.client.RenderProvider;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.util.AzureLibUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
/*
 *  Gun Concept : uses Pumpkins and Melons, Low Damage but High Ammo Capacity
 */
public class MelonLobberItem extends ContraptionBaseGunItem
{
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

	public MelonLobberItem(float damage,int mag,int cool, Item Ammo, boolean firemode)
	{
		super(new Item.Settings().maxCount(1).maxDamage(mag + 1));
		bulletDamage = damage;
		magsize = mag;
		cooldown = cool;
		AmmoItem = Ammo;
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController[]{(new AnimationController(this,"shoot_controller", (event) ->
		{
			MinecraftClient client = MinecraftClient.getInstance();
        	ClientPlayerEntity player = client.player;
			if (player != null)
			{
				ItemStack mainHandStack = player.getStackInHand(Hand.MAIN_HAND);
				ItemStack offHandStack = player.getStackInHand(Hand.OFF_HAND);

				if (mainHandStack.getItem() == this || offHandStack.getItem() == this) {
					return PlayState.CONTINUE;
				}
			}
			return PlayState.STOP;
		}))
			.triggerableAnim("firing", RawAnimation.begin().then("animation.melon_lobber.firing", LoopType.PLAY_ONCE))
			.triggerableAnim("reload", RawAnimation.begin().then("animation.melon_lobber.reload", LoopType.PLAY_ONCE))});
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}


	@Override
	public void ContraptionShoot(PlayerEntity Player, World worldIn, ItemStack stack, LivingEntity shooter, float bulletDamage)
	{
		ProjectileEntity projectile = MelonLobberItem.createBullet(worldIn, stack, Player, bulletDamage);
		projectile.setVelocity(Player, Player.getPitch(), Player.getYaw(), 0.0f, 1.5f, 1.0f);
		worldIn.spawnEntity(projectile);
	}

	public static MelonAmmoEntity createBullet(World worldIn, ItemStack stack, LivingEntity shooter, float bulletDamage)
	{
        var enchantlevel = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, shooter);
		var effectlevel = EnchantmentHelper.getEquipmentLevel(Enchantments.PUNCH, shooter);
    	var bullet = new MelonAmmoEntity(worldIn, shooter, enchantlevel > 0 ? (bulletDamage + (enchantlevel * 1.5F + 0.5F)) : bulletDamage, effectlevel);
        return bullet;
    }

	// Creates the render
	@Override
	public void createRenderer(Consumer<Object> consumer)
	{
		// Accepts a consumer to create a new renderer
		consumer.accept(new RenderProvider()
		{
			// Your render class made above
			private MelonLobberRenderer renderer = null;

			@Override
			public BuiltinModelItemRenderer getCustomRenderer() {
				// Check if renderer is null, create a new instance if so
				if (renderer == null)
					return new MelonLobberRenderer();
				// Return the existing renderer if it's not null
				return this.renderer;
			}
		});
	}

	@Override
	public Supplier<Object> getRenderProvider() {
		return this.renderProvider;
	}



}

