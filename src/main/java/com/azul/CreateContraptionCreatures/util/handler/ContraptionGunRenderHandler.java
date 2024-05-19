package com.azul.CreateContraptionCreatures.util.handler;

import com.azul.CreateContraptionCreatures.item.weapon.ContraptionBaseGunItem;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetRenderHandler;

import io.github.fabricators_of_create.porting_lib.event.client.RenderHandCallback.RenderHandEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.util.math.RotationAxis;

public class ContraptionGunRenderHandler extends ShootableGadgetRenderHandler
{
	@Override
	protected void playSound(Hand hand, Vec3d position) {}

	@Override
	protected boolean appliesTo(ItemStack stack)
	{
		return stack.getItem() instanceof ContraptionBaseGunItem;
	}

	protected void onRenderPlayerHand(RenderHandEvent event)
	{
		ItemStack heldItem = event.getItemStack();
		if (!appliesTo(heldItem))
			return;

		MinecraftClient mc = MinecraftClient.getInstance();
		@SuppressWarnings("unused")
		AbstractClientPlayerEntity player = mc.player;
		HeldItemRenderer firstPersonRenderer = mc.getEntityRenderDispatcher().getHeldItemRenderer();

		MatrixStack ms = event.getPoseStack();
		VertexConsumerProvider buffer = event.getMultiBufferSource();
		int light = event.getPackedLight();
		float pt = event.getPartialTicks();

		boolean rightHand = event.getHand() == Hand.MAIN_HAND ^ mc.player.getMainArm() == Arm.LEFT;
		float recoil = rightHand ? MathHelper.lerp(pt, lastRightHandAnimation, rightHandAnimation)
				: MathHelper.lerp(pt, lastLeftHandAnimation, leftHandAnimation);
		float equipProgress = event.getEquipProgress();

		if (rightHand && (rightHandAnimation > .01f || dontReequipRight))
			equipProgress = 0;
		if (!rightHand && (leftHandAnimation > .01f || dontReequipLeft))
			equipProgress = 0;

		// Render gadget without arm and offset
		ms.push();
		float flip = rightHand ? 1.0F : -1.0F;
		float f1 = MathHelper.sqrt(event.getSwingProgress());
		float f2 = -0.3F * MathHelper.sin(f1 * (float) Math.PI);
		float f3 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
		float f4 = -0.4F * MathHelper.sin(event.getSwingProgress() * (float) Math.PI);
		float f5 = MathHelper.sin(event.getSwingProgress() * event.getSwingProgress() * (float) Math.PI);
		float f6 = MathHelper.sin(f1 * (float) Math.PI);

		ms.translate(flip * (f2 + 0.64F - .1f), f3 + -0.4F + equipProgress * -0.6F, f4 + -0.72F - 0.1f + recoil);
		ms.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(flip * f6 * 70.0F));
		ms.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(flip * f5 * -20.0F));
		transformTool(ms, flip, equipProgress, recoil, pt);
		firstPersonRenderer.renderItem(mc.player, heldItem,
				rightHand ? ModelTransformationMode.FIRST_PERSON_RIGHT_HAND
						: ModelTransformationMode.FIRST_PERSON_LEFT_HAND,
				!rightHand, ms, buffer, light);
		ms.pop();

		event.setCanceled(true);
	}

	@Override
	protected void transformTool(MatrixStack ms, float flip, float equipProgress, float recoil, float pt) {}

	@Override
	protected void transformHand(MatrixStack ms, float flip, float equipProgress, float recoil, float pt) {}
}
