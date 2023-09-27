package com.sulvic.sqfixer.client.render;

import static net.minecraft.client.renderer.OpenGlHelper.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.Map;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.sulvic.sqfixer.SpiderQueenFixer;
import com.sulvic.sqfixer.common.HumanInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import sq.core.SpiderCore;
import sq.entity.creature.EntityHuman;

@SuppressWarnings("rawtypes")
public class RenderHumanFix extends RenderBiped{

	private final ModelBiped modelArmor, modelArmorPlate;

	public RenderHumanFix(){
		super(new ModelBiped(0f), 0.5f);
		modelArmor = new ModelBiped(0.5f);
		modelArmorPlate = new ModelBiped(1f);
	}

	private ResourceLocation getEntityTexture(EntityHuman human){
		ResourceLocation resLoc = AbstractClientPlayer.locationStevePng;
		if(SpiderCore.getConfig().showHumanSkin){
			Minecraft mc = Minecraft.getMinecraft();
			SkinManager manager = mc.func_152342_ad();
			GameProfile profile = HumanInfo.getProfileFromOldName(human.getUsername());
			if(profile != null){
				Type type = Type.SKIN;
				Map map = manager.func_152788_a(profile);
				if(map.containsKey(type)) resLoc = manager.func_152792_a((MinecraftProfileTexture)map.get(type), type);
			}
			if(resLoc == null) resLoc = AbstractClientPlayer.locationStevePng;
		}
		return resLoc;
	}

	private String getCheckedContributorLabel(EntityHuman human, String label){
		StringBuilder builder = new StringBuilder();
		if(human.getIsUsernameContributor()) builder.append(EnumChatFormatting.YELLOW).append(EnumChatFormatting.ITALIC);
		builder.append(label);
		return builder.toString();
	}

	private void doRender(EntityHuman human, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){
		double correctPosY = posY - human.yOffset;
		shadowOpaque = 1f;
		ItemStack stack = human.getHeldItem();
		modelArmorPlate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = stack == null? 0: 1;
		modelArmorPlate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = human.isSneaking();
		if(stack != null){
			EnumAction action = stack.getItemUseAction();
			if(action == EnumAction.bow) modelArmorPlate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = true;
		}
		if(human.isSneaking()) correctPosY -= 0.125d;
		super.doRender((EntityLiving)human, posX, correctPosY, posZ, rotationYaw, rotationPitch);
		modelArmorPlate.aimedBow = modelArmor.aimedBow = modelBipedMain.aimedBow = false;
		modelArmorPlate.isSneak = modelArmor.isSneak = modelBipedMain.isSneak = false;
		modelArmorPlate.heldItemRight = modelArmor.heldItemRight = modelBipedMain.heldItemRight = 0;
	}

	private void renderLabel(EntityHuman human, double posX, double posY, double posZ, String label){
		if(human.getDistanceSqToEntity(renderManager.livingPlayer) <= 4096d){
			FontRenderer fontRenderer = getFontRendererFromRenderManager();
			float labelScale = 0.0268F;
			glPushMatrix();
			glTranslatef((float)posX + 0f, (float)posY + human.height + 0.5f, (float)posZ);
			glNormal3f(0f, 1f, 0f);
			glRotatef(-renderManager.playerViewY, 0f, 1f, 0f);
			glRotatef(renderManager.playerViewX, 1f, 0f, 0f);
			glScalef(-labelScale, -labelScale, labelScale);
			glDisable(GL_LIGHTING);
			glDepthMask(false);
			glDisable(GL_DEPTH_TEST);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
			glDisable(GL_TEXTURE_2D);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(0f, 0f, 0f, 0.25f);
			int halfStrWidth = fontRenderer.getStringWidth(label) / 2;
			tessellator.addVertex((-halfStrWidth - 1), -1d, 0.0d);
			tessellator.addVertex((-halfStrWidth - 1), 8d, 0d);
			tessellator.addVertex((halfStrWidth + 1), 8d, 0d);
			tessellator.addVertex((halfStrWidth + 1), -1d, 0d);
			tessellator.draw();
			glEnable(GL_TEXTURE_2D);
			glEnable(GL_DEPTH_TEST);
			glDepthMask(true);
			fontRenderer.drawString(getCheckedContributorLabel(human, label), -fontRenderer.getStringWidth(label) / 2, 0, 0xFFFFFFFF);
			glEnable(GL_LIGHTING);
			glDisable(GL_BLEND);
			glColor4f(1f, 1f, 1f, 1f);
			glPopMatrix();
		}
	}

	protected void preRenderCallback(EntityLivingBase livingBase, float partialTicks){
		float scale = 0.9375f;
		glScalef(scale, scale, scale);
	}

	private void passSpecialRender(EntityHuman human, double posX, double posY, double posZ){
		if(Minecraft.isGuiEnabled()){
			double distSq = human.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer);
			if(distSq <= Math.pow(SpiderQueenFixer.getConfig().getMaxLabelDistance(), 2d)){
				if(SpiderCore.getConfig().showHumanName){
					String name = human.getUsername();
					GameProfile profile = HumanInfo.getProfileFromOldName(name);
					if(profile != null) name = profile.getName();
					renderLabel(human, posX, posY + 0.05d, posZ, name);
				}
				if(SpiderCore.getConfig().showHumanType) renderLabel(human, posX, posY - 0.2d, posZ, human.getFortuneString());
			}
		}
	}

	protected ResourceLocation getEntityTexture(Entity entity){ return getEntityTexture((EntityHuman)entity); }

	protected void passSpecialRender(EntityLivingBase livingBase, double posX, double posY, double posZ){ passSpecialRender((EntityHuman)livingBase, posX, posY, posZ); }

	public void doRender(Entity entity, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){
		doRender((EntityHuman)entity, posX, posY, posZ, rotationYaw, rotationPitch);
	}

	public void doRender(EntityLiving living, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){
		doRender((EntityHuman)living, posX, posY, posZ, rotationYaw, rotationPitch);
	}

}
