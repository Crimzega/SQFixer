package com.sulvic.sqfixer.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelChickenCocoon extends ModelTypedCocoon{

	ModelRenderer head, beak, chin, cocoon;

	public ModelChickenCocoon(){
		super();
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-2f, -6f, -3f, 4, 6, 3);
		head.setRotationPoint(0f, 20f, -6f);
		head.setTextureSize(textureWidth, textureHeight);
		setRotation(head, 0f, 0f, 0f);
		beak = new ModelRenderer(this, 14, 0);
		beak.addBox(-2f, -4f, -5f, 4, 2, 2);
		beak.setRotationPoint(0f, 20f, -6f);
		beak.setTextureSize(textureWidth, textureHeight);
		setRotation(beak, 0f, 0f, 0f);
		chin = new ModelRenderer(this, 14, 4);
		chin.addBox(-1f, -2f, -4f, 2, 2, 2);
		chin.setRotationPoint(0f, 20f, -6f);
		chin.setTextureSize(textureWidth, textureHeight);
		setRotation(chin, 0f, 0f, 0f);
		cocoon = new ModelRenderer(this, 0, 24);
		cocoon.addBox(-6f, -12f, -6f, 12, 12, 12);
		cocoon.setRotationPoint(0f, 24f, 0f);
		cocoon.setTextureSize(textureWidth, textureHeight);
		setRotation(cocoon, 0f, 0f, 0f);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		head.render(partialTicks);
		beak.render(partialTicks);
		chin.render(partialTicks);
		cocoon.render(partialTicks);
	}

}
