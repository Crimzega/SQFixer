package com.sulvic.sqfixer.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelEndermanCocoon extends ModelTypedCocoon{

	ModelRenderer head, cocoon;

	public ModelEndermanCocoon(){
		super();
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4f, -8f, -4f, 8, 8, 8);
		head.setRotationPoint(0f, 7f, 0f);
		head.setTextureSize(textureWidth, textureHeight);
		setRotation(head, 0f, 0f, 0f);
		cocoon = new ModelRenderer(this, 0, 24);
		cocoon.addBox(-5f, 0f, -3.5f, 10, 17, 7);
		cocoon.setRotationPoint(0f, 7f, 0f);
		cocoon.setTextureSize(textureWidth, textureHeight);
		setRotation(cocoon, 0f, 0f, 0f);
	}

	public void render(Entity entity, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		head.render(partialTicks);
		cocoon.render(partialTicks);
	}

}
