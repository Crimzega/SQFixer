package com.sulvic.sqfixer.client;

import java.util.Map;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.sulvic.sqfixer.SpiderQueenFixer;
import com.sulvic.sqfixer.client.HumanCurrentSkinner.HumanInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import radixcore.util.RadixMath;
import sq.core.SpiderCore;
import sq.entity.creature.EntityHuman;

@SuppressWarnings({"rawtypes", "unused"})
public class RenderHumanReskin extends RenderBiped{
	
	private static final Map<String, HumanInfo> NAME_TO_INFO = Maps.newHashMap();
	private final ModelBiped modelArmorPlate, modelArmor;
	
	public RenderHumanReskin(){
		super(new ModelBiped(0f), 0.5f);
		modelArmorPlate = new ModelBiped(1f);
		modelArmor = new ModelBiped(0.5f);
	}
	
	public static void populateData(String origName, HumanInfo info){ NAME_TO_INFO.put(origName, info); }
	
	private void renderLabel(EntityHuman human, double posX, double posY, double posZ, String label){
		if(label == "LuvTrumpetStyle") label = "SheWolfDeadly";
		renderLivingLabel(human, NAME_TO_INFO.containsKey(label)? NAME_TO_INFO.get(label).getGameProfile().getName(): label, posX, posY, posZ, 64);
	}
	
	protected ResourceLocation getEntityTexture(EntityHuman human){ return AbstractClientPlayer.locationStevePng; }
	
	protected ResourceLocation getEntityTexture(Entity entity){ return getEntityTexture((EntityHuman)entity); }
	
	protected void passSpecialRender(EntityLivingBase livingBase, double posX, double posY, double posZ){
		if(Minecraft.isGuiEnabled()){
			EntityHuman human = (EntityHuman)livingBase;
			if(RadixMath.getDistanceToEntity(human, Minecraft.getMinecraft().thePlayer) < 6d){
				if(SpiderCore.getConfig().showHumanName) renderLabel(human, posX, posY - 0.05000000074505806d, posZ, human.getUsername());
				if(SpiderCore.getConfig().showHumanType) renderLabel(human, posX, posY - 0.20000000298023224d, posZ, human.getFortuneString());
			}
			
		}
	}
	
	protected void renderLivingLabel(EntityHuman human, String text, double posX, double posY, double posZ, int visibleDistance){
		double distanceSq = human.getDistanceSqToEntity(renderManager.livingPlayer);
		if(distanceSq <= (visibleDistance * visibleDistance)){
			FontRenderer fontRenderer = getFontRendererFromRenderManager();
			float labelScale = 0.0268F;
			GL11.glPushMatrix();
			GL11.glTranslatef((float)posX + 0f, (float)posY + human.height + 0.5f, (float)posZ);
			GL11.glNormal3f(0f, 1f, 0f);
			GL11.glRotatef(-renderManager.playerViewY, 0f, 1f, 0f);
			GL11.glRotatef(renderManager.playerViewX, 1f, 0f, 0f);
			GL11.glScalef(-0.0268f, -0.0268f, 0.0268f);
			GL11.glDisable(2896);
			GL11.glDepthMask(false);
			GL11.glDisable(2929);
			GL11.glEnable(3042);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glDisable(3553);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(0f, 0f, 0f, 0.25f);
			int halfStringWidth = fontRenderer.getStringWidth(text) / 2;
			tessellator.addVertex((-halfStringWidth - 1), -1d, 0d);
			tessellator.addVertex((-halfStringWidth - 1), 8d, 0d);
			tessellator.addVertex((halfStringWidth + 1), 8d, 0d);
			tessellator.addVertex((halfStringWidth + 1), -1d, 0d);
			tessellator.draw();
			GL11.glEnable(3553);
			fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, 0, 553648127);
			GL11.glEnable(2929);
			GL11.glDepthMask(true);
			if(human.getIsUsernameContributor()) fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, 0, -1);
			else fontRenderer.drawString(text, -fontRenderer.getStringWidth(text) / 2, 0, -1);
			GL11.glEnable(2896);
			GL11.glDisable(3042);
			GL11.glColor4f(1f, 1f, 1f, 1f);
			GL11.glPopMatrix();
		}
	}
	
	protected void preRenderCallback(EntityLivingBase livingBase, float partialTicks){ GL11.glScalef(0.9375f, 0.9375f, 0.9375f); }
	
	public void doRender(Entity entity, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){ doRender((EntityHuman)entity, posX, posY, posZ, rotationYaw, rotationPitch); }
	
	public void doRender(EntityHuman human, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){
		HumanInfo info = NAME_TO_INFO.get(human.getUsername());
		if(info != null){
			SkinManager manager = Minecraft.getMinecraft().func_152342_ad();
			Map map = manager.func_152788_a(info.getGameProfile());
			if(map.containsKey(Type.SKIN)) bindTexture(manager.func_152792_a((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN));
		}
		double posYCorrection = posY - human.yOffset;
		shadowOpaque = 1f;
		ItemStack heldItem = human.getHeldItem();
		modelBipedMain.heldItemRight = (heldItem == null)? 0: 1;
		modelBipedMain.isSneak = human.isSneaking();
		if(heldItem != null){
			EnumAction useAction = heldItem.getItemUseAction();
			if(useAction == EnumAction.bow) modelBipedMain.aimedBow = true;
		}
		if(human.isSneaking()) posYCorrection -= 0.125D;
		super.doRender((EntityLiving)human, posX, posYCorrection, posZ, rotationYaw, rotationPitch);
		modelBipedMain.aimedBow = modelBipedMain.isSneak = false;
		modelBipedMain.heldItemRight = 0;
	}
	
	public void doRender(EntityLiving living, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){ doRender((EntityHuman)living, posX, posY, posZ, rotationYaw, rotationPitch); }
	
}
