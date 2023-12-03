package com.sulvic.sqfixer.client.model;

import static org.lwjgl.opengl.GL11.*;

import com.sulvic.sqfixer.common.SpiderQueenData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import sq.core.SpiderCore;
import sq.entity.creature.EntitySpiderEx;

public class ModelRetexedSpiderQueen extends ModelBase{

	public boolean aimedBow, sneaking;
	public int heldItemLeft, heldItemRight;
	ModelRenderer playerHead, playerHeadwear, playerTorso, playerArmL, playerArmR, sqHead, sqTorso, sqChest, sqArmL, sqArmR, sqBody, sqLegL, sqLegL1, sqLegL2, sqLegL3, sqLegR, sqLegR1,
		sqLegR2, sqLegR3, sqThorax, sqSpinnerL, sqSpinnerR;

	public ModelRetexedSpiderQueen(float scale){
		textureWidth = 70;
		textureHeight = 36;
		playerHead = new ModelRenderer(this, 0, 0);
		playerHead.addBox(-4f, -8f, -4f, 8, 8, 8, scale);
		playerHead.setRotationPoint(0f, 3f, -3f);
		playerHead.setTextureSize(64, 32);
		setRotation(playerHead, 0f, 0f, 0f);
		playerHeadwear = new ModelRenderer(this, 32, 0);
		playerHeadwear.addBox(-4f, -8f, -4f, 8, 8, 8, scale + 0.5f);
		playerHeadwear.setRotationPoint(0f, 3f, -3f);
		playerHeadwear.setTextureSize(64, 32);
		setRotation(playerHeadwear, 0f, 0f, 0f);
		playerTorso = new ModelRenderer(this, 16, 16);
		playerTorso.addBox(-4f, 0f, -2f, 8, 10, 4, scale);
		playerTorso.setRotationPoint(0f, 3f, -3f);
		playerTorso.setTextureSize(64, 32);
		setRotation(playerTorso, 0f, 0f, 0f);
		playerArmL = new ModelRenderer(this, 40, 16);
		playerArmL.addBox(-3f, -3f, -3f, 4, 12, 4, scale);
		playerArmL.setRotationPoint(5f, 5f, -3f);
		playerArmL.setTextureSize(64, 32);
		playerArmL.mirror = true;
		setRotation(playerArmL, 0f, 0f, 0f);
		playerArmR = new ModelRenderer(this, 40, 16);
		playerArmR.addBox(-1f, -2f, -2f, 4, 12, 4, scale);
		playerArmR.setRotationPoint(-5f, 5f, -3f);
		playerArmR.setTextureSize(64, 32);
		setRotation(playerArmR, 0f, 0f, 0f);
		sqHead = new ModelRenderer(this, 0, 0);
		sqHead.addBox(-4f, -8f, -4f, 8, 8, 8);
		sqHead.setRotationPoint(0f, 3f, -3f);
		sqHead.setTextureSize(textureWidth, textureHeight);
		setRotation(sqHead, 0f, 0f, 0f);
		sqTorso = new ModelRenderer(this, 8, 16);
		sqTorso.addBox(-3f, -10f, -2f, 6, 10, 4);
		sqTorso.setRotationPoint(0f, 13f, -3f);
		sqTorso.setTextureSize(textureWidth, textureHeight);
		setRotation(sqTorso, 0f, 0f, 0f);
		sqChest = new ModelRenderer(this, 0, 30);
		sqChest.addBox(-3f, -6.9f, -6.9f, 6, 3, 2);
		sqChest.setRotationPoint(0f, 13f, -3f);
		sqChest.setTextureSize(textureWidth, textureHeight);
		setRotation(sqChest, -0.5846853f, 0f, 0f);
		sqArmL = new ModelRenderer(this, 0, 16);
		sqArmL.addBox(-1f, -1f, -1f, 2, 10, 2);
		sqArmL.setRotationPoint(4f, 4f, -3f);
		sqArmL.setTextureSize(textureWidth, textureHeight);
		sqArmL.mirror = true;
		setRotation(sqArmL, 0f, 0f, 0f);
		sqArmR = new ModelRenderer(this, 0, 16);
		sqArmR.addBox(-1f, -1f, -1f, 2, 10, 2);
		sqArmR.setRotationPoint(-4f, 4f, -3f);
		sqArmR.setTextureSize(textureWidth, textureHeight);
		setRotation(sqArmR, 0f, 0f, 0f);
		sqBody = new ModelRenderer(this, 32, 4);
		sqBody.addBox(-3f, -3f, -3f, 6, 6, 5);
		sqBody.setRotationPoint(0f, 15f, 0f);
		sqBody.setTextureSize(textureWidth, textureHeight);
		setRotation(sqBody, 0f, 0f, 0f);
		sqLegL = new ModelRenderer(this, 24, 0);
		sqLegL.addBox(-1f, -1f, -1f, 16, 2, 2);
		sqLegL.setRotationPoint(4f, 15f, -1f);
		sqLegL.setTextureSize(textureWidth, textureHeight);
		sqLegL.mirror = true;
		setRotation(sqLegL, 0f, 0.7859382f, 0.7859382f);
		sqLegL1 = new ModelRenderer(this, 24, 0);
		sqLegL1.addBox(-1f, -1f, -1f, 16, 2, 2);
		sqLegL1.setRotationPoint(4f, 15f, 0f);
		sqLegL1.setTextureSize(textureWidth, textureHeight);
		sqLegL1.mirror = true;
		setRotation(sqLegL1, 0f, 0.3926991f, 0.5811946f);
		sqLegL2 = new ModelRenderer(this, 24, 0);
		sqLegL2.setRotationPoint(4f, 15f, 1f);
		sqLegL2.addBox(-1f, -1f, -1f, 16, 2, 2);
		sqLegL2.setTextureSize(textureWidth, textureHeight);
		sqLegL2.mirror = true;
		setRotation(sqLegL2, 0f, -0.3926991f, 0.5811946f);
		sqLegL3 = new ModelRenderer(this, 24, 0);
		sqLegL3.addBox(-1f, -1f, -1f, 16, 2, 2);
		sqLegL3.setRotationPoint(4f, 15f, 2f);
		sqLegL3.setTextureSize(textureWidth, textureHeight);
		sqLegL3.mirror = true;
		setRotation(sqLegL3, 0f, -0.7859382f, 0.7859382f);
		sqLegR = new ModelRenderer(this, 24, 0);
		sqLegR.addBox(-15f, -1f, -1f, 16, 2, 2);
		sqLegR.setRotationPoint(-4f, 15f, -1f);
		sqLegR.setTextureSize(textureWidth, textureHeight);
		setRotation(sqLegR, 0f, -0.7859382f, -0.7859382f);
		sqLegR1 = new ModelRenderer(this, 24, 0);
		sqLegR1.addBox(-15f, -1f, -1f, 16, 2, 2);
		sqLegR1.setRotationPoint(-4f, 15f, 0f);
		sqLegR1.setTextureSize(textureWidth, textureHeight);
		setRotation(sqLegR1, 0f, -0.3926991f, -0.5811946f);
		sqLegR2 = new ModelRenderer(this, 24, 0);
		sqLegR2.addBox(-15f, -1f, -1f, 16, 2, 2);
		sqLegR2.setRotationPoint(-4f, 15f, 1f);
		sqLegR2.setTextureSize(textureWidth, textureHeight);
		setRotation(sqLegR2, 0f, 0.3926991f, -0.5811946f);
		sqLegR3 = new ModelRenderer(this, 24, 0);
		sqLegR3 = new ModelRenderer(this, 24, 0);
		sqLegR3.addBox(-15f, -1f, -1f, 16, 2, 2);
		sqLegR3.setRotationPoint(-4f, 15f, 2f);
		sqLegR3.setTextureSize(textureWidth, textureHeight);
		setRotation(sqLegR3, 0f, 0.7859382f, -0.7859382f);
		sqThorax = new ModelRenderer(this, 28, 15);
		sqThorax.addBox(-5f, -4f, -6f, 10, 10, 11);
		sqThorax.setRotationPoint(0f, 11f, 7f);
		sqThorax.setTextureSize(textureWidth, textureHeight);
		setRotation(sqThorax, 0f, 0f, 0f);
		sqSpinnerL = new ModelRenderer(this, 0, 0);
		sqSpinnerL.addBox(-1f, -3f, 0f, 2, 3, 1);
		sqSpinnerL.setRotationPoint(2f, 15f, 11f);
		sqSpinnerL.setTextureSize(textureWidth, textureHeight);
		sqSpinnerL.mirror = true;
		setRotation(sqSpinnerL, -0.3328859f, 0f, 0f);
		sqSpinnerR = new ModelRenderer(this, 0, 0);
		sqSpinnerR.addBox(-1f, -3f, 0f, 2, 3, 1);
		sqSpinnerR.setRotationPoint(-2f, 15f, 11f);
		sqSpinnerR.setTextureSize(textureWidth, textureHeight);
		setRotation(sqSpinnerR, -0.3328859f, 0f, 0f);
	}

	private void setRotation(ModelRenderer renderer, float x, float y, float z){
		renderer.rotateAngleX = x;
		renderer.rotateAngleY = y;
		renderer.rotateAngleZ = z;
	}

	public ModelRenderer getPlayerHead(){ return playerHead; }

	public ModelRenderer getTorso(){ return SpiderCore.getConfig().useSpiderQueenModel? sqTorso: playerTorso; }
	
	public ModelRenderer getRightArm(){ return SpiderCore.getConfig().useSpiderQueenModel? sqArmR: playerArmR; }

	public void setLivingAnimations(EntityLivingBase livingBase, float limbSwing, float limbSwingAmount, float partialTicks){}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		sqHead.rotateAngleX = playerHead.rotateAngleX = playerHeadwear.rotateAngleX = rotationPitch / (180f / (float)Math.PI);
		sqHead.rotateAngleY = playerHead.rotateAngleY = playerHeadwear.rotateAngleY = rotationYaw / (180f / (float)Math.PI);
		float legAngleY = (float)Math.PI / 8, legAngleZ = (float)Math.PI / 4;
		sqLegL.rotateAngleY = legAngleY * 2f;
		sqLegR.rotateAngleY = -legAngleY * 2f;
		sqLegL1.rotateAngleY = legAngleY;
		sqLegR1.rotateAngleY = -legAngleY;
		sqLegL2.rotateAngleY = -legAngleY;
		sqLegR2.rotateAngleY = legAngleY;
		sqLegL3.rotateAngleY = -legAngleY * 2f;
		sqLegR3.rotateAngleY = legAngleY * 2f;
		sqLegL.rotateAngleZ = legAngleZ;
		sqLegR.rotateAngleZ = -legAngleZ;
		sqLegL1.rotateAngleZ = legAngleZ * 0.74f;
		sqLegR1.rotateAngleZ = -legAngleZ * 0.74f;
		sqLegL2.rotateAngleZ = legAngleZ * 0.74f;
		sqLegR2.rotateAngleZ = -legAngleZ * 0.74f;
		sqLegL3.rotateAngleZ = legAngleZ;
		sqLegR3.rotateAngleZ = -legAngleZ;
		sqArmR.rotateAngleX = playerArmR.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f + (float)Math.PI) * 2f * limbSwingAmount * 0.5f;
		sqArmL.rotateAngleX = playerArmL.rotateAngleX = MathHelper.cos(limbSwing * 0.6662f) * 2f * limbSwingAmount * 0.5f;
		sqArmL.rotateAngleZ = playerArmL.rotateAngleZ = 0f;
		sqArmR.rotateAngleZ = playerArmR.rotateAngleZ = 0f;
		if(isRiding){
			float ridingOffset = -(float)Math.PI / 5f;
			sqArmL.rotateAngleX = playerArmL.rotateAngleX += ridingOffset;
			sqArmR.rotateAngleX = playerArmR.rotateAngleX += ridingOffset;
		}
		if(heldItemLeft != 0) sqArmL.rotateAngleX = playerArmL.rotateAngleX = sqArmL.rotateAngleX * 0.5f - ((float)Math.PI / 10f) * (float)heldItemRight;
		if(heldItemRight != 0) sqArmR.rotateAngleX = playerArmR.rotateAngleX = sqArmR.rotateAngleX * 0.5f - ((float)Math.PI / 10f) * (float)heldItemRight;
		sqArmL.rotateAngleY = playerArmL.rotateAngleY = 0f;
		sqArmR.rotateAngleY = playerArmR.rotateAngleY = 0f;
		if(onGround > -9990f){
			float ground = onGround;
			sqTorso.rotateAngleY = playerTorso.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(ground) * 3.141593F * 2.0F) * 0.2F;
			sqArmR.rotationPointZ = playerArmR.rotationPointZ = MathHelper.sin(getTorso().rotateAngleY) * 4F - 3F;
			sqArmR.rotationPointX = playerArmR.rotationPointX = -MathHelper.cos(getTorso().rotateAngleY) * 4F;
			sqArmL.rotationPointZ = playerArmL.rotationPointZ = -MathHelper.sin(getTorso().rotateAngleY) * 4F - 3F;
			sqArmL.rotationPointX = playerArmL.rotationPointX = MathHelper.cos(getTorso().rotateAngleY) * 4F;
			playerArmR.rotateAngleY = sqArmR.rotateAngleY += getTorso().rotateAngleY;
			playerArmL.rotateAngleY = sqArmL.rotateAngleY += getTorso().rotateAngleY;
			playerArmL.rotateAngleX = sqArmL.rotateAngleX += getTorso().rotateAngleY;
			ground = 1.0F - onGround;
			ground *= ground;
			ground *= ground;
			ground = 1.0F - ground;
			final float ff7 = MathHelper.sin(ground * (float)Math.PI);
			final float ff8 = MathHelper.sin(onGround * (float)Math.PI) * -(sqHead.rotateAngleX - 0.7f) * 0.75f;
			playerArmR.rotateAngleX = sqArmR.rotateAngleX -= ff7 * 1.2d + ff8;
			playerArmR.rotateAngleY = sqArmR.rotateAngleY += getTorso().rotateAngleY * 2.0f;
			playerArmR.rotateAngleZ = sqArmR.rotateAngleZ = MathHelper.sin(onGround * (float)Math.PI) * -0.4f;
		}
		float armAngleOffsetX = MathHelper.sin(limbSwingAmount * 0.067f) * 0.05f, armAngleOffsetZ = MathHelper.cos(limbSwingAmount * 0.09f) * 0.05f + 0.05f;
		sqArmL.rotateAngleX = playerArmL.rotateAngleX -= armAngleOffsetX;
		sqArmR.rotateAngleX = playerArmR.rotateAngleX += armAngleOffsetX;
		sqArmL.rotateAngleZ = playerArmL.rotateAngleZ -= armAngleOffsetZ;
		sqArmR.rotateAngleZ = playerArmR.rotateAngleZ += armAngleOffsetZ;
		if(aimedBow){
			sqArmL.rotateAngleX = playerArmL.rotateAngleX = -((float)Math.PI * 0.5f) + sqHead.rotateAngleX;
			sqArmR.rotateAngleX = playerArmR.rotateAngleX = -((float)Math.PI * 0.5f) + sqHead.rotateAngleX;
			sqArmL.rotateAngleY = playerArmL.rotateAngleY = 0.1f * 0.6f + sqHead.rotateAngleY + 0.4f;
			sqArmR.rotateAngleY = playerArmR.rotateAngleY = -(0.1f * 0.6f) + sqHead.rotateAngleY;
			sqArmL.rotateAngleZ = playerArmL.rotateAngleZ = 0f;
			sqArmR.rotateAngleZ = playerArmR.rotateAngleZ = 0f;
			sqArmL.rotateAngleX = playerArmL.rotateAngleX -= 0f * 1.2f * 0f * 0.4f;
			sqArmR.rotateAngleX = playerArmR.rotateAngleX -= 0f * 1.2f * 0f * 0.4f;
			sqArmL.rotateAngleZ = playerArmL.rotateAngleZ -= MathHelper.cos(limbSwingAmount * 0.09f) * 0.05f + 0.05f;
			sqArmR.rotateAngleZ = playerArmR.rotateAngleZ += MathHelper.cos(limbSwingAmount * 0.09f) * 0.05f + 0.05f;
			sqArmL.rotateAngleX = playerArmL.rotateAngleX -= MathHelper.sin(limbSwingAmount * 0.067f) * 0.05f;
			sqArmR.rotateAngleX = playerArmR.rotateAngleX += MathHelper.sin(limbSwingAmount * 0.067f) * 0.05f;
		}
		float backLegsY = -(MathHelper.cos(limbSwing * 0.6662f * 2f) * 0.4f) * limbSwingAmount;
		float midBackLegsY = -(MathHelper.cos(limbSwing * 0.6662f * 2f + (float)Math.PI) * 0.4f) * limbSwingAmount;
		float midFrontLegsY = -(MathHelper.cos(limbSwing * 0.6662f * 2f + ((float)Math.PI * 0.5f)) * 0.4f) * limbSwingAmount;
		float frontLegsY = -(MathHelper.cos(limbSwing * 0.6662f * 2f + ((float)Math.PI * 1.5f)) * 0.4f) * limbSwingAmount;
		float backLegsZ = Math.abs(MathHelper.sin(limbSwing * 0.6662f) * 0.4f) * limbSwingAmount;
		float midBackLegsZ = Math.abs(MathHelper.sin(limbSwing * 0.6662f + (float)Math.PI) * 0.4f) * limbSwingAmount;
		float midFrontLegsZ = Math.abs(MathHelper.sin(limbSwing * 0.6662f + ((float)Math.PI * 0.5f)) * 0.4f) * limbSwingAmount;
		float frontLegsZ = Math.abs(MathHelper.sin(limbSwing * 0.6662f + ((float)Math.PI * 1.5f)) * 0.4f) * limbSwingAmount;
		sqLegL.rotateAngleY += -frontLegsY;
		sqLegR.rotateAngleY += frontLegsY;
		sqLegL1.rotateAngleY += -midFrontLegsY;
		sqLegR1.rotateAngleY += midFrontLegsY;
		sqLegL2.rotateAngleY += -midBackLegsY;
		sqLegR2.rotateAngleY += midBackLegsY;
		sqLegL3.rotateAngleY += -backLegsY;
		sqLegR3.rotateAngleY += backLegsY;
		sqLegL.rotateAngleZ += -frontLegsZ;
		sqLegR.rotateAngleZ += frontLegsZ;
		sqLegL1.rotateAngleZ += -midFrontLegsZ;
		sqLegR1.rotateAngleZ += midFrontLegsZ;
		sqLegL2.rotateAngleZ += -midBackLegsZ;
		sqLegR2.rotateAngleZ += midBackLegsZ;
		sqLegL3.rotateAngleZ += -backLegsZ;
		sqLegR3.rotateAngleZ += backLegsZ;
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		setRotationAngles(limbSwing, limbSwingAmount, time, rotationYaw, rotationPitch, partialTicks);
		if(entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)entity;
			if(player.ridingEntity instanceof EntitySpiderEx){
				EntitySpiderEx spiderEx = (EntitySpiderEx)player.ridingEntity;
				if(spiderEx.isOnLadder()){
					Vec3 vec = spiderEx.getLookVec();
					if(vec.xCoord <= -0.9d || vec.zCoord <= -0.9d){
						glRotatef(270f, 1f, 0f, 0f);
						glTranslated(0d, 1.8d, 0d);
					}
					if(vec.xCoord >= 0.9d || vec.zCoord >= 0.9d){
						glRotatef(-90f, 1f, 0f, 0f);
						glTranslated(0d, 1.8d, 0d);
					}
				}
			}
		}
		if(SpiderCore.getConfig().usePlayerSkin && entity instanceof EntityPlayer){
			AbstractClientPlayer player = (AbstractClientPlayer)entity;
			SpiderQueenData playerData = SpiderQueenData.getPlayerData(player);
			glPushMatrix();
			glScaled(0.9d, 0.9d, 0.9d);
			playerHead.render(partialTicks);
			playerTorso.render(partialTicks);
			glPushMatrix();
			glTranslated(-0.25d, 0d, 0d);
			playerArmL.render(partialTicks);
			glPopMatrix();
			glPushMatrix();
			glTranslated(0.25d, 0d, 0d);
			playerArmR.render(partialTicks);
			glPopMatrix();
			glPushMatrix();
			glTranslated(0d, 0.25d, -0.25d);
			playerHeadwear.render(partialTicks);
			glPopMatrix();
			glPopMatrix();
			Minecraft.getMinecraft().renderEngine.bindTexture(playerData.getMainTexture());
		}
		else{
			sqHead.render(partialTicks);
			sqTorso.render(partialTicks);
			sqChest.render(partialTicks);
			sqArmL.render(partialTicks);
			sqArmR.render(partialTicks);
		}
		sqBody.render(partialTicks);
		sqLegL.render(partialTicks);
		sqLegR.render(partialTicks);
		sqLegL1.render(partialTicks);
		sqLegR1.render(partialTicks);
		sqLegL2.render(partialTicks);
		sqLegR2.render(partialTicks);
		sqLegL3.render(partialTicks);
		sqLegR3.render(partialTicks);
		sqThorax.render(partialTicks);
		sqSpinnerL.render(partialTicks);
		sqSpinnerR.render(partialTicks);
	}

}
