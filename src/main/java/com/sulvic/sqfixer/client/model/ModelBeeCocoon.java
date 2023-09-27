package com.sulvic.sqfixer.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelBeeCocoon extends ModelTypedCocoon{

	protected ModelRenderer head, feelerL, feelerR, cocoon;

	public ModelBeeCocoon(){
		super();
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-3f, -3f, -6f, 6, 6, 6);
		head.setRotationPoint(0f, 19f, -2f);
		head.setTextureSize(textureWidth, textureHeight);
		setRotation(head, 0f, 0f, 0f);
		feelerL = new ModelRenderer(this, 0, -4);
		feelerL.addBox(0f, -6f, -2f, 0, 6, 3);
		feelerL.setRotationPoint(-2f, 17f, -6f);
		feelerL.setTextureSize(textureWidth, textureHeight);
		setRotation(feelerL, 0.4363323f, 0.5325988f, 0f);
		feelerR = new ModelRenderer(this, 0, -4);
		feelerR.addBox(0f, -6f, -2f, 0, 6, 3);
		feelerR.setRotationPoint(2f, 17f, -6f);
		feelerR.setTextureSize(textureWidth, textureHeight);
		setRotation(feelerR, 0.4363323f, -0.5325988f, 0f);
		cocoon = new ModelRenderer(this, 4, 29);
		cocoon.addBox(-5f, -9f, -5f, 10, 9, 10);
		cocoon.setRotationPoint(0f, 24f, 0f);
		cocoon.setTextureSize(textureWidth, textureHeight);
		setRotation(cocoon, 0f, 0f, 0f);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		head.render(partialTicks);
		cocoon.render(partialTicks);
		feelerL.render(partialTicks);
		feelerR.render(partialTicks);
	}

}
