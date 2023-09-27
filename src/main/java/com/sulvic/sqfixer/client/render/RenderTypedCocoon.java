package com.sulvic.sqfixer.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sulvic.sqfixer.client.model.*;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.*;
import sq.entity.creature.EntityCocoon;
import sq.enums.EnumCocoonType;

public class RenderTypedCocoon extends Render{

	private static final Map<EnumCocoonType, ModelTypedCocoon> COCOON_TYPES = Maps.newHashMap();

	public RenderTypedCocoon(){ shadowSize = 0.5f; }

	static{
		addCocoonType(EnumCocoonType.BLACK_ANT, new ModelInsectCocoon());
		addCocoonType(EnumCocoonType.COW, new ModelCowCocoon());
		addCocoonType(EnumCocoonType.CREEPER, new ModelCreeperCocoon());
		addCocoonType(EnumCocoonType.GATHERER_BEE, new ModelBeeCocoon());
		addCocoonType(EnumCocoonType.HUMAN, new ModelBipedCocoon());
		addCocoonType(EnumCocoonType.PIG, new ModelPigCocoon());
		addCocoonType(EnumCocoonType.QUEEN_BEE, new ModelBeeCocoon());
		addCocoonType(EnumCocoonType.SHEEP, new ModelSheepCocoon());
		addCocoonType(EnumCocoonType.SKELETON, new ModelBipedCocoon());
		addCocoonType(EnumCocoonType.WARRIOR_BEE, new ModelBeeCocoon());
		addCocoonType(EnumCocoonType.WASP, new ModelInsectCocoon());
		addCocoonType(EnumCocoonType.WOLF, new ModelWolfCocoon());
		addCocoonType(EnumCocoonType.ZOMBIE, new ModelBipedCocoon());
		addCocoonType(EnumCocoonType.ENDERMAN, new ModelEndermanCocoon());
		addCocoonType(EnumCocoonType.CHICKEN, new ModelChickenCocoon());
		addCocoonType(EnumCocoonType.VILLAGER, new ModelVillagerCocoon());
		addCocoonType(EnumCocoonType.HORSE, new ModelHorseCocoon());
		addCocoonType(EnumCocoonType.RED_ANT, new ModelInsectCocoon());
	}

	public static void addCocoonType(EnumCocoonType type, ModelTypedCocoon modelCocoon){ COCOON_TYPES.put(type, modelCocoon); }

	private ResourceLocation getEntityTexture(EntityCocoon cocoon){
		EnumCocoonType type = cocoon.getCocoonType();
		String name = type.toString().toLowerCase();
		return new ResourceLocation("sqfixer", "textures/entities/cocooned_" + name + (cocoon.isEaten()? "_dead": "") + ".png");
	}

	private void doRender(EntityCocoon cocoon, double x, double y, double z, float rotationYaw, float rotationPitch){
		glPushMatrix();
		glTranslated(x, y, z);
		bindTexture(getEntityTexture(cocoon));
		float interpRenderYawOffset = interpolateRotation(cocoon.prevRenderYawOffset, cocoon.renderYawOffset, rotationPitch);
		glRotatef(interpRenderYawOffset, 0f, 1f, 0f);
		float hitAdjust = cocoon.getTimeSinceHit() - rotationPitch / 2f;
		float dmgAdjust = cocoon.getCurrentDamage() - rotationPitch / 2f;
		if(dmgAdjust < 0f) dmgAdjust = 0f;
		if(hitAdjust > 0f) glRotatef(MathHelper.sin(dmgAdjust) * hitAdjust * dmgAdjust / 10f, 1f, 0f, 0f);
		glScalef(-1f, -1f, 1f);
		cocoon.setHitboxSize();
		glTranslatef(0f, -1.5f, 0f);
		ModelTypedCocoon model = COCOON_TYPES.get(cocoon.getCocoonType());
		if(model != null) model.render(cocoon, 0f, 0f, 0f, 0f, 0f, 0.0625f);
		glPopMatrix();
	}

	private float interpolateRotation(float min, float max, float range){
		float finalMax = max - min;
		while(finalMax < -180f) finalMax += 360f;
		while(finalMax >= 180f) finalMax -= 360f;
		return min + range * finalMax;
	}

	protected ResourceLocation getEntityTexture(Entity entity){ return getEntityTexture((EntityCocoon)entity); }

	public void doRender(Entity entity, double x, double y, double z, float rotationYaw, float rotationPitch){ doRender((EntityCocoon)entity, x, y, z, rotationYaw, rotationPitch); }

}
