package com.azul.CreateContraptionCreatures.item.weapon;


import java.util.function.Consumer;
import java.util.function.Supplier;

import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.FlameBlastEntity;
import com.azul.CreateContraptionCreatures.item.client.renders.*;

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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
/*
 *  Gun Concept : uses Wheat Seeds, Low Damage but High Ammo Capacity
 */
public class FlamerItem extends ContraptionBaseGunItem
{
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
	private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

	public FlamerItem(float damage,int mag,int cool, Item Ammo, boolean firemode)
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
			.triggerableAnim("firing", RawAnimation.begin().then("animation.flamer.firing", LoopType.HOLD_ON_LAST_FRAME))
			.triggerableAnim("stop_firing", RawAnimation.begin().then("animation.flamer.stop_firing", LoopType.HOLD_ON_LAST_FRAME))
			.triggerableAnim("reload", RawAnimation.begin().then("animation.flamer.reload", LoopType.PLAY_ONCE))});
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return cache;
	}

	public void fireGun(World worldIn, PlayerEntity player, ItemStack stack) {
		if (stack.getDamage() < (stack.getMaxDamage() - 1) && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
			if (!worldIn.isClient())
			{
				triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerWorld) worldIn), "shoot_controller", "firing");


				int remainingAmmo = (stack.getMaxDamage()- 1) - stack.getDamage();
				for (int i = 0; i < remainingAmmo; i++)
				{
					// Method to simulate the shooting action
					for (int a = 0; a < 6; a++)
					{
						if(worldIn.getRandom().nextBoolean())
						{
							ContraptionShoot(player, worldIn, stack, player, bulletDamage);
						}
					}
					stack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
				}
				player.getItemCooldownManager().set(stack.getItem(), cooldown);
				triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerWorld) worldIn), "shoot_controller", "stop_firing");
				player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.PLAYERS, 1.0F, 1.0F);
			}
		}
	}

	@Override
	public void ContraptionShoot(PlayerEntity Player, World worldIn, ItemStack stack, LivingEntity shooter, float bulletDamage)
	{
		var rod = FlamerItem.createBullet(worldIn,stack, shooter,bulletDamage);
		rod.setVelocity(Player, Player.getPitch(), Player.getYaw(), 0.0F, 0.5F, 1.0F);
		rod.updatePositionAndAngles(Player.getX(), Player.getY()+0.85, Player.getZ(), 0, 0);
		rod.hasNoGravity();
		rod.isOnFire();
		worldIn.spawnEntity(rod);
	}

	public static FlameBlastEntity createBullet(World worldIn, ItemStack stack, LivingEntity shooter, float bulletDamage)
	{
        return new FlameBlastEntity(worldIn, shooter,bulletDamage);
    }

	// Creates the render
	@Override
	public void createRenderer(Consumer<Object> consumer)
	{
		// Accepts a consumer to create a new renderer
		consumer.accept(new RenderProvider()
		{
			// Your render class made above
			private FlamerRenderer renderer = null;

			@Override
			public BuiltinModelItemRenderer getCustomRenderer() {
				// Check if renderer is null, create a new instance if so
				if (renderer == null)
					return new FlamerRenderer();
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

