package com.azul.CreateContraptionCreatures.item.weapon;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

import com.azul.CreateContraptionCreatures.CreateContraptionCreaturesClient;
import com.azul.CreateContraptionCreatures.item.ModItem;

import com.simibubi.create.foundation.item.CustomArmPoseItem;
import com.simibubi.create.foundation.utility.Components;

import io.github.fabricators_of_create.porting_lib.enchant.CustomEnchantingBehaviorItem;
import io.github.fabricators_of_create.porting_lib.item.EntitySwingListenerItem;
import io.github.fabricators_of_create.porting_lib.item.ReequipAnimationItem;

import io.netty.buffer.Unpooled;

import mod.azure.azurelib.Keybindings;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import mod.azure.azurelib.core.animation.Animation.LoopType;
import mod.azure.azurelib.core.animation.AnimationController;
import mod.azure.azurelib.core.animation.RawAnimation;
import mod.azure.azurelib.core.object.PlayState;
import mod.azure.azurelib.entities.TickingLightEntity;
import mod.azure.azurelib.platform.Services;
import mod.azure.azurelib.util.AzureLibUtil;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.World;
/*
 * Base Gun For this mod.  Set the basic stuff up, Set up the POV view
 */
public abstract class ContraptionBaseGunItem extends Item implements GeoItem,CustomArmPoseItem, EntitySwingListenerItem, ReequipAnimationItem , CustomEnchantingBehaviorItem
{
	private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    @SuppressWarnings("unused")
	private BlockPos lightBlockPos = null;
	@SuppressWarnings("unused")
	private boolean hasFired = false;
	private boolean wasReloadPressed = false;
	private boolean wasFireWeaponPressed = false;


	public float bulletDamage;
	public static int magsize;
	public int cooldown;
	public Item AmmoItem;
	public boolean isAutomatic;
	public int firemode = 0;

	public ContraptionBaseGunItem(Settings properties)
	{
        super(properties);
    }

	public void SetFireMode(PlayerEntity player,ItemStack stack){}

	//Needed JUST to Disable the Arm Swing Animation. Mainly using Create Mod method of handling this
 	@Override
	public boolean onEntitySwing(ItemStack stack, LivingEntity entity){return true;}
	@Override
	public UseAction getUseAction(ItemStack stack) {return UseAction.NONE;}
	@Override
	public boolean canMine(BlockState state, World world, BlockPos pos, PlayerEntity player) {return false;}
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged)
	{
		return slotChanged || newStack.getItem() != oldStack.getItem();
	}

	@Override
	@Nullable
	public ArmPose getArmPose(ItemStack stack, AbstractClientPlayerEntity player, Hand hand)
	{
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

	//
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		if (enchantment == Enchantments.POWER)
			return true;
		if (enchantment == Enchantments.PUNCH)
			return true;
		if (enchantment == Enchantments.FLAME)
			return true;
		if (enchantment == Enchantments.LOOTING)
			return true;
		return CustomEnchantingBehaviorItem.super.canApplyAtEnchantingTable(stack, enchantment);
	}

	// Button Click Check
    @Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		if (world.isClient() && entity instanceof PlayerEntity player)
		{
			var GunItem = player.getStackInHand(Hand.MAIN_HAND).getItem();
			if (GunItem instanceof ContraptionBaseGunItem) {
				// Check the current state of the keys
				boolean isReloadPressed = Keybindings.RELOAD.isPressed();
				boolean isFireWeaponPressed = Keybindings.FIRE_WEAPON.isPressed();

				// Trigger reload action only if the key was not pressed before but is pressed now
				if (isReloadPressed && !wasReloadPressed && selected) {
					var passedData = new PacketByteBuf(Unpooled.buffer());
					passedData.writeBoolean(true);
					ClientPlayNetworking.send(ModItem.RELOAD_BULLETS, passedData);
				}

				// Trigger fire mode action only if the key was not pressed before but is pressed now
				if (isFireWeaponPressed && !wasFireWeaponPressed && selected) {
					var passedData = new PacketByteBuf(Unpooled.buffer());
					passedData.writeBoolean(true);
					ClientPlayNetworking.send(ModItem.FIRE_MODE, passedData);
				}

				// Update the previous state of the keys
				wasReloadPressed = isReloadPressed;
				wasFireWeaponPressed = isFireWeaponPressed;
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

	public void fireGun(World worldIn, PlayerEntity player, ItemStack stack)
	{
		if (stack.getDamage() < (stack.getMaxDamage() - 1) && !player.getItemCooldownManager().isCoolingDown(stack.getItem()) && extraFireCheck(stack))
		{
			if (!worldIn.isClient())
			{
				int enchantLevel = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, player);
				ContraptionShoot(player, worldIn, stack, player, (enchantLevel > 0 ? (bulletDamage + (enchantLevel * 1.5F + 0.5F)) : bulletDamage));
				player.getItemCooldownManager().set(stack.getItem(), cooldown);
				stack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
				triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerWorld) worldIn), "shoot_controller", "firing");
				player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_CAKE_ADD_CANDLE, SoundCategory.PLAYERS, 1.00F, 1.0F);
			}
		}
	}

	public boolean extraFireCheck(ItemStack stack)
	{
		return true;
	}

	// Method for Spawning bullets
	public abstract void ContraptionShoot(PlayerEntity Shooter_Player,World worldIn, ItemStack stack, LivingEntity shooter, float bulletDamage);
	public void prereload(PlayerEntity user, Hand hand){};

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
				prereload(user,hand);
				user.getItemCooldownManager().set((user.getStackInHand(hand)).getItem(), cooldown * 2);
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

	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext flag)
	{
		int power = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
		int AmmoCount = stack.getMaxDamage() - stack.getDamage() - 1;

		tooltip.add(Components.immutableEmpty());
		tooltip.add(Text.translatable("[ Ammo: " + AmmoCount + " / " + (stack.getMaxDamage() - 1) +" ]").formatted(Formatting.GOLD));
		tooltip.add(Components.translatable(""+ AmmoItem.getTranslationKey()).append(Components.literal(":")).formatted(Formatting.GRAY));
		MutableText spacing = Components.literal(" ");
		tooltip.add(spacing.copyContentOnly().append(Text.translatable("" + Float.toString(power > 0 ? (bulletDamage + (power * 1.5F + 0.5F)) : bulletDamage) + " Attack Damage").formatted(Formatting.DARK_GREEN)));
		tooltip.add(spacing.copyContentOnly().append(Text.translatable("" + cooldown + " Cooldown Rate").formatted(Formatting.DARK_GREEN)));
		super.appendTooltip(stack, world, tooltip, flag);
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController[]
		{(new AnimationController(this, "shoot_controller", (event) -> {return PlayState.CONTINUE;}))
		.triggerableAnim("firing", RawAnimation.begin().then("firing", LoopType.PLAY_ONCE))
		.triggerableAnim("reload", RawAnimation.begin().then("reload", LoopType.PLAY_ONCE))});
	}

	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}

	@SuppressWarnings("rawtypes")
	public void removeAmmo(Item ammo, PlayerEntity playerEntity)
	{
		if (!playerEntity.isCreative())
		{
			Iterator var3 = playerEntity.getInventory().offHand.iterator();

			while(true)
			{
				while(var3.hasNext()) {
				ItemStack item = (ItemStack)var3.next();
				if (item.getItem() == ammo) {
					item.decrement(1);
					return;
				}

				Iterator var5 = playerEntity.getInventory().main.iterator();

				while(var5.hasNext()) {
					ItemStack item1 = (ItemStack)var5.next();
					if (item1.getItem() == ammo) {
						item1.decrement(1);
						break;
					}
				}
				}

				return;
			}
		}
    }

	@SuppressWarnings("rawtypes")
	public void removeOffHandItem(Item ammo, PlayerEntity playerEntity)
	{
		if (!playerEntity.isCreative()) {
			Iterator var3 = playerEntity.getInventory().offHand.iterator();

			while(var3.hasNext()) {
			ItemStack item = (ItemStack)var3.next();
				if (item.getItem() == ammo)
				{
					item.decrement(1);
					break;
				}
			}
		}
	}

	public boolean hasGlint(ItemStack stack) {
		return false;
	}

	public int getMaxUseTime(ItemStack stack) {
		return 72000;
	}

	public int getEnchantability() {
		return 0;
	}

	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	protected void spawnLightSource(Entity entity, boolean isInWaterBlock)
	{
		if (this.lightBlockPos == null)
		{
			this.lightBlockPos = this.findFreeSpace(entity.getWorld(), entity.getBlockPos(), 2);
			if (this.lightBlockPos == null)
			{
				return;
			}
			entity.getWorld().setBlockState(this.lightBlockPos, Services.PLATFORM.getTickingLightBlock().getDefaultState());
		}
		else if (this.checkDistance(this.lightBlockPos, entity.getBlockPos(), 2))
		{
			BlockEntity blockEntity = entity.getWorld().getBlockEntity(this.lightBlockPos);
			if (blockEntity instanceof TickingLightEntity)
			{
				TickingLightEntity tickingLightEntity = (TickingLightEntity)blockEntity;
				tickingLightEntity.refresh(isInWaterBlock ? 20 : 0);
			}
			else
			{
				this.lightBlockPos = null;
			}
		}
		else
		{
			this.lightBlockPos = null;
		}
	}

	private boolean checkDistance(BlockPos blockPosA, BlockPos blockPosB, int distance) {
		return Math.abs(blockPosA.getX() - blockPosB.getX()) <= distance && Math.abs(blockPosA.getY() - blockPosB.getY()) <= distance && Math.abs(blockPosA.getZ() - blockPosB.getZ()) <= distance;
	}

	private BlockPos findFreeSpace(World world, BlockPos blockPos, int maxDistance) {
		if (blockPos == null) {
			return null;
		} else {
			int[] offsets = new int[maxDistance * 2 + 1];
			offsets[0] = 0;

			for(int i = 2; i <= maxDistance * 2; i += 2) {
			offsets[i - 1] = i / 2;
			offsets[i] = -i / 2;
			}

			int[] var19 = offsets;
			int var6 = offsets.length;

			for(int var7 = 0; var7 < var6; ++var7) {
			int x = var19[var7];
			int[] var9 = offsets;
			int var10 = offsets.length;

			for(int var11 = 0; var11 < var10; ++var11) {
				int y = var9[var11];
				int[] var13 = offsets;
				int var14 = offsets.length;

				for(int var15 = 0; var15 < var14; ++var15) {
					int z = var13[var15];
					BlockPos offsetPos = blockPos.add(x, y, z);
					BlockState state = world.getBlockState(offsetPos);
					if (state.isAir() || state.getBlock().equals(Services.PLATFORM.getTickingLightBlock())) {
						return offsetPos;
					}
				}
			}
			}

			return null;
		}
	}

	@SuppressWarnings("rawtypes")
	public static EntityHitResult hitscanTrace(PlayerEntity player, double range, float ticks)
	{
		Vec3d look = player.getRotationVec(ticks);
		Vec3d start = player.getCameraPosVec(ticks);
		Vec3d end = new Vec3d(player.getX() + look.x * range, player.getEyeY() + look.y * range, player.getZ() + look.z * range);
		double traceDistance = player.getWorld().raycast(new RaycastContext(start, end, ShapeType.COLLIDER, FluidHandling.NONE, player)).getPos().squaredDistanceTo(end);
		Iterator var9 = player.getWorld().getOtherEntities(player, player.getBoundingBox().stretch(look.multiply(traceDistance)).stretch(3.0, 3.0, 3.0), (entity) -> {
			return !entity.isSpectator() && entity.canHit() && entity instanceof LivingEntity;
		}).iterator();

		Entity possible;
		do {
			if (!var9.hasNext()) {
			return null;
			}

			possible = (Entity)var9.next();
		} while(!possible.getBoundingBox().expand(0.3).raycast(start, end).isPresent() || !(start.squaredDistanceTo((Vec3d)possible.getBoundingBox().expand(0.3).raycast(start, end).get()) < traceDistance));

		return ProjectileUtil.getEntityCollision(player.getWorld(), player, start, end, player.getBoundingBox().stretch(look.multiply(traceDistance)).expand(3.0, 3.0, 3.0), (target) -> {
			return !target.isSpectator() && player.isAttackable() && player.canSee(target);
		});
	}
}
