package com.sulvic.sqfixer.client.render;

import static com.sulvic.sqfixer.SpiderFixerReference.*;

import com.sulvic.sqfixer.client.model.ModelRetexedAnt;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import sq.entity.creature.EntityAnt;

public class RenderRetexedAnt extends RenderLiving{

	private static final ResourceLocation BLACK_ANT_TEXTURE = new ResourceLocation(MODID, "textures/entities/black_ant.png");
	private static final ResourceLocation RED_ANT_TEXTURE = new ResourceLocation(MODID, "textures/entities/red_ant.png");

	public RenderRetexedAnt(){
		super(new ModelRetexedAnt(), 1f);
		setRenderPassModel((ModelRetexedAnt)mainModel);
	}

	protected ResourceLocation getEntityTexture(EntityAnt ant){
		switch(ant.getAntType()){
			case RED:
				return RED_ANT_TEXTURE;
			default:
				return BLACK_ANT_TEXTURE;
		}
	}

	protected ResourceLocation getEntityTexture(Entity entity){ return getEntityTexture((EntityAnt)entity); }

	public void doRender(EntityLivingBase livingBase, double posX, double posY, double posZ, float rotationYaw, float partialTicks){
		
		super.doRender(livingBase, posX, posY, posZ, rotationYaw, partialTicks);
	}

}
