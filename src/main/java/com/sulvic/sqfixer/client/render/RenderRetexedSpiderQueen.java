package com.sulvic.sqfixer.client.render;

import com.sulvic.sqfixer.client.model.ModelRetexedSpiderQueen;
import com.sulvic.sqfixer.common.SpiderQueenData.EnumSkin;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.*;
import net.minecraft.util.ResourceLocation;
import sq.entity.creature.EntitySpiderQueen;

public class RenderRetexedSpiderQueen extends RenderLiving{

	public RenderRetexedSpiderQueen(){ super(new ModelRetexedSpiderQueen(0f), 0f); }

	protected ResourceLocation getEntityTexture(EntitySpiderQueen spiderQueen){ return EnumSkin.byId((byte)spiderQueen.getTextureId()).getMainTexture(); }

	protected ResourceLocation getEntityTexture(Entity entity){ return getEntityTexture((EntitySpiderQueen)entity); }

	protected int shouldRenderPass(EntityLivingBase livingBase, int renderPass, float partialTicks){ return -1; }

	protected void func_82408_c(EntityLivingBase livingBase, int renderPass, float partialTicks){}

	protected void func_96449_a(EntityLivingBase livingBase, double x, double y, double z, String playerName, float par, double playerDist){}

	protected void preRenderCallback(EntityLivingBase livingBase, float partialTicks){}

	protected void renderEquippedItems(EntityLivingBase livingBase, float partialTicks){}

	public void doRender(Entity entity, double posX, double posY, double posZ, float rotationYaw, float partialTicks){
		doRender((EntityLivingBase)entity, posX, posY, posZ, rotationYaw, partialTicks);
	}

	public void doRender(EntityLivingBase livingBase, double posX, double posY, double posZ, float rotationYaw, float partialTicks){
		super.doRender(livingBase, posX, posY, posZ, rotationYaw, partialTicks);
	}

}
