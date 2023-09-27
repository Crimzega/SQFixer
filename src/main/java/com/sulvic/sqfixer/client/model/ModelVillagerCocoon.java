package com.sulvic.sqfixer.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelVillagerCocoon extends ModelTypedCocoon{

	ModelRenderer head, nose, cocoonHead, cocoonBody;

	public ModelVillagerCocoon(){
		super();
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4f, -10f, -4f, 8, 10, 8);
		head.setRotationPoint(0f, 7f, 0f);
		head.setTextureSize(textureWidth, textureHeight);
		setRotation(head, 0f, 0f, 0f);
		nose = new ModelRenderer(this, 0, 0);
		nose.addBox(-1f, -1f, -2f, 2, 4, 2);
		nose.setRotationPoint(0f, 5f, -3.8f);
		nose.setTextureSize(textureWidth, textureHeight);
		setRotation(nose, 0f, 0f, 0f);
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
		head.render(partialTicks);
		nose.render(partialTicks);
		cocoonHead.render(partialTicks);
		cocoonBody.render(partialTicks);
	}

}
