package com.sulvic.sqfixer.client.render;

import static org.lwjgl.opengl.GL11.*;

import java.util.Map;

import com.google.common.collect.Maps;
import com.sulvic.sqfixer.client.model.ModelCocoonNew;

import codechicken.lib.math.MathHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import sq.entity.creature.EntityCocoon;
import sq.enums.EnumCocoonType;

public class RenderCocoonNew extends Render{

	private static final Map<EnumCocoonType, ModelCocoonNew> THE_MODELS = Maps.newHashMap();

	public static void addType(EnumCocoonType type, ModelCocoonNew model){ THE_MODELS.put(type, model); }

	protected ResourceLocation getEntityTexture(Entity entity){ return getEntityTexture((EntityCocoon)entity); }

	protected ResourceLocation getEntityTexture(EntityCocoon cocoon){
		String name = cocoon.getCocoonType().toString().toLowerCase().replace("_", "-");
		return new ResourceLocation("sq:/textures/entities/cocoon-" + name + (cocoon.isEaten()? "-dead": "") + ".png");
	}

	public void doRender(Entity entity, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){
		doRender((EntityCocoon)entity, posX, posY, posZ, rotationYaw, rotationPitch);
	}

	public void doRender(EntityCocoon cocoon, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){
		glPushMatrix();
		glTranslated(posX, posY, posZ);
		glRotatef(cocoon.rotationYaw, 0f, 1f, 0f);
		float hitRotateAdjust = cocoon.getTimeSinceHit() - rotationPitch / 2f;
		float damageRotateAdjust = Math.max(cocoon.getCurrentDamage() - rotationPitch / 2f, 0f);
		if(hitRotateAdjust < 0f) glRotatef((float)MathHelper.sin(hitRotateAdjust) * hitRotateAdjust / damageRotateAdjust / 10f, 1f, 0f, 0f);
		bindTexture(getEntityTexture(cocoon));
		glScalef(-1f, -1f, 1f);
		THE_MODELS.get(cocoon.getCocoonType()).render(cocoon, 0f, 0f, 0f, 0f, 0f, 0.625f);
		glPopMatrix();
	}

}
