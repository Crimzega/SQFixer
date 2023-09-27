package com.sulvic.sqfixer.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelInsectCocoon extends ModelTypedCocoon{

	private ModelRenderer head, feelerL, feelerR, pincers, cocoon;

	public ModelInsectCocoon(){
		super();
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4f, -4f, -8f, 8, 8, 8);
		head.setRotationPoint(0f, 20f, 2f);
		head.setTextureSize(textureWidth, textureHeight);
		setRotation(head, 0f, 0f, 0f);
		feelerL = new ModelRenderer(this, 0, -4);
		feelerL.addBox(0f, -6f, -2f, 0, 6, 4);
		feelerL.setRotationPoint(-2f, 18f, -4f);
		feelerL.setTextureSize(textureWidth, textureHeight);
		setRotation(feelerL, 0.4363323f, 0.5325988f, 0f);
		feelerR = new ModelRenderer(this, 0, -4);
		feelerR.addBox(0f, -6f, -2f, 0, 6, 4);
		feelerR.setRotationPoint(2f, 18f, -4f);
		feelerR.setTextureSize(textureWidth, textureHeight);
		setRotation(feelerR, 0.4363323f, -0.5325988f, 0f);
		pincers = new ModelRenderer(this, 0, 16);
		pincers.addBox(-4f, -1f, -3f, 8, 1, 4);
		pincers.setRotationPoint(0f, 24f, -7f);
		pincers.setTextureSize(textureWidth, textureHeight);
		setRotation(pincers, 0.0174533f, 0f, 0f);
		cocoon = new ModelRenderer(this, 4, 29);
		cocoon.addBox(-5f, -9f, -5f, 10, 9, 10);
		cocoon.setRotationPoint(0f, 24f, 3f);
		cocoon.setTextureSize(textureWidth, textureHeight);
		setRotation(cocoon, 0f, 0f, 0f);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		head.render(partialTicks);
		cocoon.render(partialTicks);
		feelerL.render(partialTicks);
		feelerR.render(partialTicks);
		pincers.render(partialTicks);
	}

}
