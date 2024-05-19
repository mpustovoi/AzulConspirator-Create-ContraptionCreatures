package com.azul.CreateContraptionCreatures.item.weapon;

import org.jetbrains.annotations.Nullable;

import com.azul.CreateContraptionCreatures.CreateContraptionCreaturesClient;
import com.azul.CreateContraptionCreatures.item.ModItem;
import com.simibubi.create.foundation.item.CustomArmPoseItem;

import io.github.fabricators_of_create.porting_lib.item.EntitySwingListenerItem;
import io.github.fabricators_of_create.porting_lib.item.ReequipAnimationItem;

import io.netty.buffer.Unpooled;
import mod.azure.azurelib.Keybindings;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.items.BaseGunItem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
/*
 * Base Gun For this mod.  Set the basic stuff up, Set up the POV view
 */
public abstract class ContraptionBaseGunItem extends BaseGunItem implements CustomArmPoseItem, EntitySwingListenerItem, ReequipAnimationItem
{
    @SuppressWarnings("unused")
	private BlockPos lightBlockPos = null;
	@SuppressWarnings("unused")
	private boolean hasFired = false;


	public float bulletDamage;
	public static int magsize;
	public int cooldown;
	public Item AmmoItem;
	public boolean isAutomatic;

	public ContraptionBaseGunItem(Settings properties)
	{
        super(properties);
		SetFireMode(0);
    }

	protected void SetFireMode(int i)
	{
		switch (i)
		{
			case 0:
				isAutomatic=false;
				break;
			case 1:
				isAutomatic=true;
				break;
			default:
				isAutomatic=false;
				break;
		}
	}

	//Needed JUST to Disable the Arm Swing Animation. Mainly using Create Mod method of handling this
 	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity){return true;}
	@Override
	public UseAction getUseAction(ItemStack stack) {return UseAction.NONE;}
	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity player) {return false;}
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {return slotChanged || newStack.getItem() != oldStack.getItem();}
	@Override
	@Nullable
	public ArmPose getArmPose(ItemStack stack, AbstractClientPlayerEntity player, Hand hand) {
		if (!player.handSwinging) {
			return ArmPose.CROSSBOW_HOLD;
		}
		return null;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack itemStack = user.getStackInHand(hand);
		user.setCurrentHand(hand);
		if (world.isClient) {
			CreateContraptionCreaturesClient.CONTRAPTION_GUN_RENDER_HANDLER.dontAnimateItem(hand);
			return TypedActionResult.success(itemStack);
		}
		return TypedActionResult.success(itemStack);
	}


	// Button Click Check
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
        if (world.isClient())
		{
			var GunItem = ((PlayerEntity) entity).getStackInHand(Hand.MAIN_HAND).getItem();
            if (GunItem instanceof ContraptionBaseGunItem)
            {
				if (Keybindings.RELOAD.isPressed() && selected)
				{
					var passedData = new PacketByteBuf(Unpooled.buffer());
					passedData.writeBoolean(true);
					ClientPlayNetworking.send(ModItem.RELOAD_BULLETS, passedData);
				}

			}
        }
    }


	//check firing
	@Override
	public void usageTick(World worldIn, LivingEntity shooter, ItemStack stack, int count)
	{
		if (shooter instanceof PlayerEntity)
		{
			PlayerEntity player = (PlayerEntity) shooter;
/* 			if (!isAutomatic)
			{
				if (player.isUsingItem() && !hasFired)
				{ */
					fireGun(worldIn, player, stack);
/* 					hasFired = true;
				}
			}
			else
			{
				if (player.isUsingItem()) {
					fireGun(worldIn, player, stack);
				}
			} */
		}
	}

	private void fireGun(World worldIn, PlayerEntity player, ItemStack stack)
	{
		if (stack.getDamage() < (stack.getMaxDamage() - 1) && !player.getItemCooldownManager().isCoolingDown(stack.getItem())) {
			if (!worldIn.isClient()) {
				EntityHitResult result = BaseGunItem.hitscanTrace(player, 64, 1.0F);
				int enchantLevel = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, player);
				if (result instanceof EntityHitResult) {
					EntityHitResult entityResult = (EntityHitResult) result;
					if (entityResult.getEntity() instanceof LivingEntity) {
						entityResult.getEntity().damage(player.getDamageSources().playerAttack(player), (enchantLevel > 0 ? (bulletDamage + (enchantLevel * 1.5F + 0.5F)) : bulletDamage));
					}
				} else {
					ContraptionShoot(player, worldIn, stack, player, bulletDamage);
				}
				player.getItemCooldownManager().set(stack.getItem(), cooldown);
				stack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
				triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerWorld) worldIn), "shoot_controller", "firing");
				player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_CAKE_ADD_CANDLE, SoundCategory.PLAYERS, 1.00F, 1.0F);
			}
		}
	}
	// Method for Spawning bullets
	public abstract void ContraptionShoot(PlayerEntity Shooter_Player,World worldIn, ItemStack stack, LivingEntity shooter, float bulletDamage);

	//Reloading method, triggered via ClientNetworking thinggi
	public void reloadBullets(PlayerEntity user, Hand hand) {
		if ((user.getStackInHand(hand)).getItem() instanceof ContraptionBaseGunItem) {
			ItemStack gunStack = user.getStackInHand(hand);
			int maxAmmo = gunStack.getMaxDamage();
			int currentAmmo = maxAmmo - gunStack.getDamage();
			int missingAmmo = maxAmmo - currentAmmo;

			// Check how much ammo is available in the inventory
			int availableAmmo = countAmmo(user, AmmoItem);

			// Determine the ammo to reload
			int ammoToReload = Math.min(missingAmmo, availableAmmo);

			if (ammoToReload > 0) {
				// Consume ammo from inventory
				consumeAmmo(user, AmmoItem, ammoToReload);

				// Repair the gun item by reducing the damage value
				gunStack.setDamage(gunStack.getDamage() - ammoToReload);

				// Trigger the reload animation and sound
				triggerAnim(user, GeoItem.getOrAssignId(gunStack, (ServerWorld) user.getWorld()), "shoot_controller", "reload");
				// reload sound
				user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_BONE_BLOCK_BREAK, SoundCategory.PLAYERS, 1.00F, 1.0F);
			}
		}
	}

	// Helper method to count ammo in the player's inventory
	private int countAmmo(PlayerEntity player, Item ammoItem) {
		int count = 0;
		for (ItemStack stack : player.getInventory().main) {
			if (stack.getItem() == ammoItem) {
				count += stack.getCount();
			}
		}
		return count;
	}

	private void consumeAmmo(PlayerEntity player, Item ammoItem, int amount)
	{
		for (ItemStack stack : player.getInventory().main)
		{
			if (stack.getItem() == ammoItem) {
				int stackCount = stack.getCount();
				if (stackCount > amount) {
					stack.decrement(amount);
					return;
				} else {
					amount -= stackCount;
					stack.setCount(0);
				}
			}
		}
	}
}
