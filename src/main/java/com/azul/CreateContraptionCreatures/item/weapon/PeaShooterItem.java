package com.azul.CreateContraptionCreatures.item.weapon;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.SeedAmmoEntity;
import com.azul.CreateContraptionCreatures.item.client.renders.PeaShooterRenderer;

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
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
/*
 *  Gun Concept : uses Wheat Seeds, Low Damage but High Ammo Capacity
 */
public class PeaShooterItem extends ContraptionBaseGunItem
{
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

	public PeaShooterItem(float damage,int mag,int cool, Item Ammo, boolean firemode)
	{
		super(new Item.Settings().maxCount(1).maxDamage(mag + 1));
		bulletDamage = damage;
		magsize = mag;
		cooldown = cool;
		AmmoItem = Ammo;
		SetFireMode(0);
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController[]{(new AnimationController(this,"shoot_controller", (event) ->
		{
			return PlayState.CONTINUE;
		}))
			.triggerableAnim("firing", RawAnimation.begin().then("animation.pea_shooter.firing", LoopType.PLAY_ONCE))
			.triggerableAnim("reload", RawAnimation.begin().then("animation.pea_shooter.reload", LoopType.PLAY_ONCE))});
	}
	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}


	@Override
	public void ContraptionShoot(PlayerEntity Player, World worldIn, ItemStack stack, LivingEntity shooter, float bulletDamage)
	{
		ProjectileEntity projectile = PeaShooterItem.createBullet(worldIn, stack, Player, bulletDamage);
		projectile.setVelocity(Player,  Player.getPitch(),Player.getYaw(), 0.0F, 20.0F, 1.0F);
		if (EnchantmentHelper.getEquipmentLevel(Enchantments.FIRE_ASPECT, shooter) > 0)
			projectile.setOnFireFor(100);
		worldIn.spawnEntity(projectile);
	}

	public static SeedAmmoEntity createBullet(World worldIn, ItemStack stack, LivingEntity shooter, float bulletDamage)
	{
        var enchantlevel = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, shooter);
    	var bullet = new SeedAmmoEntity(worldIn, shooter, enchantlevel > 0 ? (bulletDamage + (enchantlevel * 1.5F + 0.5F)) : bulletDamage);
        return bullet;
    }

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		var enchantlevel = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
		super.appendTooltip(stack, world, tooltip, context);
		tooltip.add(Text.translatable("Damage: " + (enchantlevel > 0 ? (bulletDamage + (enchantlevel * 1.5F + 0.5F)) : bulletDamage)).formatted(Formatting.ITALIC));
	}

	// Creates the render
	@Override
	public void createRenderer(Consumer<Object> consumer)
	{
		// Accepts a consumer to create a new renderer
		consumer.accept(new RenderProvider()
		{
			// Your render class made above
			private PeaShooterRenderer renderer = null;

			@Override
			public BuiltinModelItemRenderer getCustomRenderer() {
				// Check if renderer is null, create a new instance if so
				if (renderer == null)
					return new PeaShooterRenderer();
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
