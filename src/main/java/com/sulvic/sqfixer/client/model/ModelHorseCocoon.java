package com.sulvic.sqfixer.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelHorseCocoon extends ModelTypedCocoon{

	ModelRenderer headStart, muzzleUp, muzzleDown, web, cocoonHead, cocoonBody;

	public ModelHorseCocoon(){
		super();
		headStart = new ModelRenderer(this, 0, 8);
		headStart.addBox(-2f, -14f, -3f, 4, 5, 6);
		headStart.setRotationPoint(0f, 17f, -2f);
		headStart.setTextureSize(textureWidth, textureHeight);
		setRotation(headStart, 0.2617994f, 0f, 0f);
		muzzleUp = new ModelRenderer(this, 20, 0);
		muzzleUp.addBox(-2f, -14f, -9f, 4, 3, 6);
		muzzleUp.setRotationPoint(0f, 17f, -2f);
		muzzleUp.setTextureSize(textureWidth, textureHeight);
		setRotation(muzzleUp, 0.2617994f, 0f, 0f);
		muzzleDown = new ModelRenderer(this, 0, 0);
		muzzleDown.addBox(-2f, -1f, -6.2f, 4, 2, 6);
		muzzleDown.setRotationPoint(0f, 8f, -7f);
		muzzleDown.setTextureSize(textureWidth, textureHeight);
		setRotation(muzzleDown, 0.447677f, -0.0174533f, 0f);
		web = new ModelRenderer(this, 20, 4);
		web.addBox(0f, -3f, -6f, 0, 4, 6);
		web.setRotationPoint(2f, 8f, -7f);
		web.setTextureSize(textureWidth, textureHeight);
		setRotation(web, 0.349066f, -0.0174553f, 0f);
		cocoonHead = new ModelRenderer(this, 4, 29);
		cocoonHead.addBox(-5f, -9f, -5f, 10, 9, 10);
		cocoonHead.setRotationPoint(0f, 12f, 0f);
		cocoonHead.setTextureSize(textureWidth, textureHeight);
		setRotation(cocoonHead, 0f, 0f, 0f);
		cocoonBody = new ModelRenderer(this, 0, 24);
		cocoonBody.addBox(-6f, -12f, -6f, 12, 12, 12);
		cocoonBody.setRotationPoint(0f, 24f, 0f);
		cocoonBody.setTextureSize(textureWidth, textureHeight);
		setRotation(cocoonBody, 0f, 0f, 0f);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		headStart.render(partialTicks);
		muzzleUp.render(partialTicks);
		muzzleDown.render(partialTicks);
		web.render(partialTicks);
		cocoonHead.render(partialTicks);
		cocoonBody.render(partialTicks);
	}

}
