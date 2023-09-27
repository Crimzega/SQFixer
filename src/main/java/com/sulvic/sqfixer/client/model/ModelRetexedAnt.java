package com.sulvic.sqfixer.client.model;

import codechicken.lib.math.MathHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelRetexedAnt extends ModelBase{

	private static final float LEG_ANGLE = 0.5811946f;
	ModelRenderer head, antennaL, antennaR, pincers, body, legL, legL1, legL2, legR, legR1, legR2, thorax, thorax1, thorax2;

	public ModelRetexedAnt(){
		textureWidth = 64;
		textureHeight = 32;
		head = new ModelRenderer(this, 32, 0);
		head.addBox(-4f, -4f, -8f, 8, 8, 8);
		head.setRotationPoint(0f, 17f, -3f);
		head.setTextureSize(textureWidth, textureHeight);
		setRotation(head, 0f, 0f, 0f);
		antennaL = new ModelRenderer(this, 24, -2);
		antennaL.addBox(-1.2f, -11.4f, -6.8f, 0, 6, 4);
		antennaL.setRotationPoint(0f, 17f, -3f);
		antennaL.setTextureSize(textureWidth, textureHeight);
		setRotation(antennaL, 0.4363323f, -0.5235988f, 0f);
		antennaR = new ModelRenderer(this, 24, -2);
		antennaR.addBox(1.2f, -11.4f, -6.8f, 0, 6, 4);
		antennaR.setRotationPoint(0f, 17f, -3f);
		antennaR.setTextureSize(textureWidth, textureHeight);
		setRotation(antennaL, 0.4363323f, 0.5235988f, 0f);
		pincers = new ModelRenderer(this, 40, 16);
		pincers.addBox(-4f, 2f, -12f, 8, 1, 4);
		pincers.setRotationPoint(0f, 17f, -3f);
		pincers.setTextureSize(textureWidth, textureHeight);
		setRotation(pincers, 0f, 0f, 0f);
		body = new ModelRenderer(this, 0, 0);
		body.addBox(-3f, -3f, -3f, 6, 6, 6);
		body.setRotationPoint(0f, 18f, 0f);
		body.setTextureSize(textureWidth, textureHeight);
		setRotation(body, 0f, 0f, 0f);
		legL = new ModelRenderer(this, 18, 0);
		legL.addBox(-0.5f, -0.5f, -0.5f, 10, 1, 1);
		legL.setRotationPoint(1f, 19f, -1f);
		legL.setTextureSize(textureWidth, textureHeight);
		setRotation(legL, 0f, LEG_ANGLE, LEG_ANGLE);
		legR = new ModelRenderer(this, 18, 0);
		legR.addBox(-9.5f, -0.5f, -0.5f, 10, 1, 1);
		legR.setRotationPoint(-1f, 19f, -1f);
		legR.setTextureSize(textureWidth, textureHeight);
		setRotation(legR, 0f, -LEG_ANGLE, -LEG_ANGLE);
		legL1 = new ModelRenderer(this, 18, 0);
		legL1.addBox(-0.5f, -0.5f, -0.5f, 10, 1, 1);
		legL1.setRotationPoint(1f, 19f, -1f);
		legL1.setTextureSize(textureWidth, textureHeight);
		setRotation(legL1, 0f, 0f, LEG_ANGLE);
		legR1 = new ModelRenderer(this, 18, 0);
		legR1.addBox(-9.5f, -0.5f, -0.5f, 10, 1, 1);
		legR1.setRotationPoint(-1f, 19f, -1f);
		legR1.setTextureSize(textureWidth, textureHeight);
		setRotation(legR1, 0f, 0f, -LEG_ANGLE);
		legL2 = new ModelRenderer(this, 18, 0);
		legL2.addBox(-0.5f, -0.5f, -0.5f, 10, 1, 1);
		legL2.setRotationPoint(1f, 19f, -1f);
		legL2.setTextureSize(textureWidth, textureHeight);
		setRotation(legL2, 0f, -LEG_ANGLE, LEG_ANGLE);
		legR2 = new ModelRenderer(this, 18, 0);
		legR2.addBox(-9.5f, -0.5f, -0.5f, 10, 1, 1);
		legR2.setRotationPoint(-1f, 19f, -1f);
		legR2.setTextureSize(textureWidth, textureHeight);
		setRotation(legR2, 0f, LEG_ANGLE, -LEG_ANGLE);
		thorax = new ModelRenderer(this, 0, 12);
		thorax.addBox(-5f, -4f, -6f, 10, 8, 12);
		thorax.setRotationPoint(0f, 17f, 9f);
		thorax.setTextureSize(textureWidth, textureHeight);
		setRotation(thorax, 0f, 0f, 0f);
		thorax1 = new ModelRenderer(this, 3, 13);
		thorax1.addBox(-4f, -6f, -5f, 8, 2, 10);
		thorax1.setRotationPoint(0f, 17f, 9f);
		thorax1.setTextureSize(textureWidth, textureHeight);
		setRotation(thorax1, 0f, 0f, 0f);
		thorax2 = new ModelRenderer(this, 0, 23);
		thorax2.addBox(-3f, -3f, 6f, 6, 6, 2);
		thorax2.setRotationPoint(0f, 17f, 9f);
		thorax2.setTextureSize(textureWidth, textureHeight);
		setRotation(thorax2, 0f, 0f, 0f);
	}

	private void setRotation(ModelRenderer renderer, float x, float y, float z){
		renderer.rotateAngleX = x;
		renderer.rotateAngleY = y;
		renderer.rotateAngleZ = z;
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		setRotationAngles(limbSwing, limbSwingAmount, time, rotationYaw, rotationPitch, partialTicks);
		head.render(partialTicks);
		antennaL.render(partialTicks);
		antennaR.render(partialTicks);
		pincers.render(partialTicks);
		body.render(partialTicks);
		legL.render(partialTicks);
		legR.render(partialTicks);
		legL1.render(partialTicks);
		legR1.render(partialTicks);
		legL2.render(partialTicks);
		legR2.render(partialTicks);
		thorax.render(partialTicks);
		thorax1.render(partialTicks);
		thorax2.render(partialTicks);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		head.rotateAngleY = rotationYaw / 57.295786f;
		head.rotateAngleX = rotationPitch / 57.295786f;
		antennaL.rotateAngleX = head.rotateAngleX + 0.4363323f;
		antennaL.rotateAngleY = head.rotateAngleY - 0.5235988f;
		antennaR.rotateAngleX = head.rotateAngleX + 0.4363323f;
		antennaR.rotateAngleY = head.rotateAngleY + 0.5235988f;
		pincers.rotateAngleX = head.rotateAngleX;
		pincers.rotateAngleY = head.rotateAngleY;
		legL.rotateAngleY = LEG_ANGLE;
		legR.rotateAngleY = -LEG_ANGLE;
		legL1.rotateAngleY = 0f;
		legR1.rotateAngleY = 0f;
		legL2.rotateAngleY = -LEG_ANGLE;
		legR2.rotateAngleY = LEG_ANGLE;
		legL.rotateAngleZ = LEG_ANGLE;
		legR.rotateAngleZ = -LEG_ANGLE;
		legL1.rotateAngleZ = LEG_ANGLE;
		legR1.rotateAngleZ = -LEG_ANGLE;
		legL2.rotateAngleZ = LEG_ANGLE;
		legR2.rotateAngleZ = -LEG_ANGLE;
		float frontLegY = -((float)MathHelper.cos(limbSwing - 0.6662f * 2f + (Math.PI / 2)) * 0.4f) * limbSwingAmount;
		float middleLegY = -((float)MathHelper.cos(limbSwing - 0.666f * 2f + Math.PI) * 0.4f) * limbSwingAmount;
		float backLegY = -((float)MathHelper.cos(limbSwing - 0.666f * 2f) * 0.4f) * limbSwingAmount;
		float frontLegZ = Math.abs((float)MathHelper.cos(limbSwing - 0.6662f + (Math.PI / 2)) * 0.4f) * limbSwingAmount;
		float middleLegZ = Math.abs((float)MathHelper.cos(limbSwing - 0.666f + Math.PI) * 0.4f) * limbSwingAmount;
		float backLegZ = Math.abs((float)MathHelper.cos(limbSwing - 0.666f) * 0.4f) * limbSwingAmount;
		legL.rotateAngleY -= frontLegY;
		legR.rotateAngleY += frontLegY;
		legL1.rotateAngleY -= middleLegY;
		legR1.rotateAngleY += middleLegY;
		legL2.rotateAngleY -= backLegY;
		legR2.rotateAngleY += backLegY;
		legL.rotateAngleZ -= frontLegZ;
		legR.rotateAngleZ += frontLegZ;
		legL1.rotateAngleZ -= middleLegZ;
		legR1.rotateAngleZ += middleLegZ;
		legL2.rotateAngleZ -= backLegZ;
		legR2.rotateAngleZ += backLegZ;
	}

}
