package com.sulvic.sqfixer.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelWolfCocoon extends ModelTypedCocoon{

	ModelRenderer head, earL, earR, muzzle, cocoonHead, cocoonBody;

	public ModelWolfCocoon(){
		super();
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3f, -3f, -4f, 6, 6, 4);
		head.setRotationPoint(0f, 8.5f, -5f);
		head.setTextureSize(textureWidth, textureHeight);
		setRotation(head, 0f, 0f, 0f);
		earL = new ModelRenderer(this, 16, 14);
		earL.addBox(1f, -5f, -2F, 2, 2, 1);
		earL.setRotationPoint(0f, 8.5f, -5f);
		earL.setTextureSize(textureWidth, textureHeight);
		setRotation(earL, 0f, 0f, 0f);
		earR = new ModelRenderer(this, 16, 14);
		earR.addBox(-3f, -5f, -2F, 2, 2, 1);
		earR.setRotationPoint(0f, 8.5f, -5f);
		earR.setTextureSize(textureWidth, textureHeight);
		setRotation(earR, 0f, 0f, 0f);
		muzzle = new ModelRenderer(this, 0, 10);
		muzzle.addBox(-2f, 0f, -7f, 3, 3, 4);
		muzzle.setRotationPoint(0f, 8.5f, -5f);
		muzzle.setTextureSize(textureWidth, textureHeight);
		setRotation(muzzle, 0f, 0f, 0f);
		cocoonHead = new ModelRenderer(this, 4, 29);
		cocoonHead.addBox(-5f, -9f, -5f, 10, 9, 10);
		cocoonHead.setRotationPoint(0f, 12f, 0f);
		cocoonHead.setTextureSize(textureWidth, textureHeight);
		setRotation(cocoonHead, 0f, 0f, 0f);
		cocoonBody = new ModelRenderer(this, 0, 24);
		cocoonBody.addBox(-6f, -12f, -6f, 12, 12, 12);
		cocoonBody.setRotationPoint(0f, 24f, 0f);
		setRotation(cocoonBody, 0f, 0f, 0f);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		head.render(partialTicks);
		earL.render(partialTicks);
		earR.render(partialTicks);
		muzzle.render(partialTicks);
		cocoonHead.render(partialTicks);
		cocoonBody.render(partialTicks);
	}

}
