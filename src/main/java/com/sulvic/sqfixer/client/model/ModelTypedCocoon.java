package com.sulvic.sqfixer.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTypedCocoon extends ModelBase{

	protected ModelTypedCocoon(){
		textureWidth = 48;
		textureHeight = 48;
	}

	protected void setRotation(ModelRenderer renderer, float x, float y, float z){
		renderer.rotateAngleX = x;
		renderer.rotateAngleY = y;
		renderer.rotateAngleZ = z;
	}

}
