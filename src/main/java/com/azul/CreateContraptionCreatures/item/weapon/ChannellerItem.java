package com.azul.CreateContraptionCreatures.item.weapon;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.EchoBlastEntity;
import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.FlameBlastEntity;
import com.azul.CreateContraptionCreatures.entity.custom.Projectiles.TeleBulletEntity;
import com.azul.CreateContraptionCreatures.item.ModItem;
import com.azul.CreateContraptionCreatures.item.client.renders.ChannellerRenderer;
import com.simibubi.create.foundation.utility.Components;

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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.entity.model.BipedEntityModel.ArmPose;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ChannellerItem extends ContraptionBaseGunItem {
    private final AnimatableInstanceCache cache = AzureLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    private static final int MAX_FIRE_MODES = 3;

    public ChannellerItem(float damage, int mag, int cool, Item Ammo, boolean firemode) {
        super(new Item.Settings().maxCount(1).maxDamage(mag + 1));
        bulletDamage = damage;
        magsize = mag;
        cooldown = cool;
        AmmoItem = Ammo;
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void SetFireMode(PlayerEntity player, ItemStack stack) {
        super.SetFireMode(player, stack);
        int currentMode = getCurrentLensMode(stack);
        int nextMode = (currentMode + 1) % MAX_FIRE_MODES;

        ItemStack currentLens = getCurrentLens(stack);
        ItemStack newLens = getNextAvailableLens(player, nextMode);

        if (newLens.isEmpty() && currentLens.isEmpty()) {
            player.sendMessage(Text.translatable("No lens equipped and no alternate lens found"));
        } else if (newLens.isEmpty()) {
            player.giveItemStack(currentLens);
            setCurrentLens(stack, ItemStack.EMPTY);
            player.sendMessage(Text.translatable("No alternate lens found"));
        } else {
            if (!currentLens.isEmpty()) {
                player.giveItemStack(currentLens);
            }
            setCurrentLens(stack, newLens);
            setCurrentLensMode(stack, nextMode);
            player.getInventory().removeStack(player.getInventory().indexOf(newLens), 1);

            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerWorld) player.getWorld()), "shoot_controller", "change");
            player.getWorld().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.PLAYERS, 0.25F, 1.3F);
            player.sendMessage(Text.translatable("Lens Swapped"));
        }
    }

    private void setCurrentLens(ItemStack stack, ItemStack newLens) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (newLens.isEmpty()) {
            nbt.remove("currentLens");
        } else {
            NbtCompound lensNbt = new NbtCompound();
            newLens.writeNbt(lensNbt);
            nbt.put("currentLens", lensNbt);
        }
        stack.setNbt(nbt);
    }

    private ItemStack getNextAvailableLens(PlayerEntity player, int startMode) {
        for (int i = 0; i < MAX_FIRE_MODES; i++) {
            int mode = (startMode + i) % MAX_FIRE_MODES;
            Item lensItem = getLensItemByMode(mode);
            for (int j = 0; j < player.getInventory().size(); j++) {
                ItemStack stack = player.getInventory().getStack(j);
                if (stack.isOf(lensItem)) {
                    return stack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private Item getLensItemByMode(int mode) {
        return switch (mode) {
            case 0 -> ModItem.FLAME_LENS;
            case 1 -> ModItem.ECHO_LENS;
            case 2 -> ModItem.TELEPORT_LENS;
            // Add more cases for additional lens items
            default -> null;
        };
    }

    private int getCurrentLensMode(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        return nbt.getInt("currentLensMode");
    }

    private void setCurrentLensMode(ItemStack stack, int mode) {
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt("currentLensMode", mode);
        stack.setNbt(nbt);
    }

    private ItemStack getCurrentLens(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        if (nbt.contains("currentLens")) {
            return ItemStack.fromNbt(nbt.getCompound("currentLens"));
        }
        return ItemStack.EMPTY;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController[]{(new AnimationController(this, "shoot_controller", (event) -> {
            MinecraftClient client = MinecraftClient.getInstance();
            ClientPlayerEntity player = client.player;
            if (player != null) {
                ItemStack mainHandStack = player.getStackInHand(Hand.MAIN_HAND);
                ItemStack offHandStack = player.getStackInHand(Hand.OFF_HAND);

                if (mainHandStack.getItem() == this || offHandStack.getItem() == this) {
                    return PlayState.CONTINUE;
                }
            }
            return PlayState.STOP;
        }))
                .triggerableAnim("firing", RawAnimation.begin().then("animation.channeller.firing", LoopType.PLAY_ONCE))
                .triggerableAnim("change", RawAnimation.begin().then("animation.channeller.change_mode", LoopType.PLAY_ONCE))
                .triggerableAnim("reload", RawAnimation.begin().then("animation.channeller.reload", LoopType.PLAY_ONCE))});
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

	@Override
	public boolean extraFireCheck(ItemStack stack)
	{
		ItemStack currentLens = getCurrentLens(stack);
		return !currentLens.isOf(ItemStack.EMPTY.getItem());
	}

    @Override
    public void ContraptionShoot(PlayerEntity player, World worldIn, ItemStack stack, LivingEntity shooter, float bulletDamage) {
        ProjectileEntity projectile = null;
        var enchantLevel = EnchantmentHelper.getEquipmentLevel(Enchantments.POWER, shooter);
        ItemStack currentLens = getCurrentLens(stack);

        if (currentLens.isOf(ModItem.TELEPORT_LENS)) {
            projectile = new TeleBulletEntity(worldIn, shooter, bulletDamage);
        } else if (currentLens.isOf(ModItem.ECHO_LENS)) {
            projectile = new EchoBlastEntity(worldIn, shooter, enchantLevel > 0 ? (bulletDamage + (enchantLevel * 1.5F + 0.5F)) : bulletDamage);
        } else if (currentLens.isOf(ModItem.FLAME_LENS)) {
            projectile = new FlameBlastEntity(worldIn, shooter, enchantLevel > 0 ? (bulletDamage + (enchantLevel * 1.5F + 0.5F)) : bulletDamage);
        }
        // Add more cases for other lens types

        if (projectile != null) {
            projectile.setVelocity(player, player.getPitch(), player.getYaw(), 0.0F, 20.0F, 1.0F);
            worldIn.spawnEntity(projectile);
        }
    }

    @Override
    @Nullable
    public ArmPose getArmPose(ItemStack stack, AbstractClientPlayerEntity player, Hand hand) {
        if (!player.handSwinging) {
            return ArmPose.TOOT_HORN;
        }
        return null;
    }

    // Creates the render
    @Override
    public void createRenderer(Consumer<Object> consumer) {
        // Accepts a consumer to create a new renderer
        consumer.accept(new RenderProvider() {
            // Your render class made above
            private ChannellerRenderer renderer = null;

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                // Check if renderer is null, create a new instance if so
                if (renderer == null)
                    return new ChannellerRenderer();
                // Return the existing renderer if it's not null
                return this.renderer;
            }
        });
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext flag) {
        int power = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
        int AmmoCount = stack.getMaxDamage() - stack.getDamage() - 1;

        tooltip.add(Components.immutableEmpty());
        tooltip.add(Text.translatable("[ Ammo: " + AmmoCount + " / " + (stack.getMaxDamage() - 1) +" ]").formatted(Formatting.GOLD));
        tooltip.add(Components.translatable(""+ AmmoItem.getTranslationKey()).append(Components.literal(":")).formatted(Formatting.GRAY));
        MutableText spacing = Components.literal(" ");
        tooltip.add(spacing.copyContentOnly().append(Text.translatable("Equipped Lens: ").append(getCurrentLens(stack).getName()).formatted(Formatting.GREEN)));
        tooltip.add(spacing.copyContentOnly().append(Text.translatable("" + Float.toString(power > 0 ? (bulletDamage + (power * 1.5F + 0.5F)) : bulletDamage) + " Attack Damage").formatted(Formatting.DARK_GREEN)));
        tooltip.add(spacing.copyContentOnly().append(Text.translatable("" + cooldown + " Cooldown Rate").formatted(Formatting.DARK_GREEN)));
        //super.appendTooltip(stack, world, tooltip, flag);
    }
}
