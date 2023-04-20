package com.sulvic.sqfixer.client.render;

import static net.minecraft.client.renderer.OpenGlHelper.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.Map;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.sulvic.sqfixer.SpiderQueenFixer;
import com.sulvic.sqfixer.client.PlayerInfoStorage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import radixcore.util.RadixMath;
import sq.core.SpiderCore;
import sq.entity.creature.EntityHuman;

@SuppressWarnings({"rawtypes", "unused"})
public class RenderHumanFix extends RenderBiped{

	private final ModelBiped modelArmorPlate, modelArmor;
	private final double labelDistSq;

	public RenderHumanFix(){
		super(new ModelBiped(0f), 0.5f);
		modelArmorPlate = new ModelBiped(1f);
		modelArmor = new ModelBiped(0.5f);
		double labelDist = SpiderQueenFixer.getConfig().renderHumanLabelDistance();
		labelDistSq = labelDist * labelDist;
	}

	private ResourceLocation getEntityTexture(EntityHuman human){
		ResourceLocation resLoc = AbstractClientPlayer.locationStevePng;
		if(SpiderCore.getConfig().showHumanSkin){
			Minecraft mc = Minecraft.getMinecraft();
			SkinManager manager = mc.func_152342_ad();
			GameProfile profile = PlayerInfoStorage.getProfileFromOldName(human.getUsername());
			if(profile != null){
				Map map = manager.func_152788_a(PlayerInfoStorage.getProfileFromOldName(human.getUsername()));
				if(map.containsKey(Type.SKIN)) resLoc = manager.func_152792_a((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
			}
			if(resLoc == null) resLoc = AbstractClientPlayer.locationStevePng;
		}
		return resLoc;
	}

	private void renderHuman(EntityHuman entity, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){
		double correctionPosY = posY - entity.yOffset;
		shadowOpaque = 1f;
		ItemStack stack = entity.getHeldItem();
		modelBipedMain.heldItemRight = (stack == null)? 0: 1;
		modelBipedMain.isSneak = entity.isSneaking();
		if(stack != null){
			EnumAction useAction = stack.getItemUseAction();
			if(useAction == EnumAction.bow) modelBipedMain.aimedBow = true;
		}
		if(entity.isSneaking()) correctionPosY -= 0.125d;
		super.doRender((EntityLiving)entity, posX, correctionPosY, posZ, rotationYaw, rotationPitch);
		modelBipedMain.aimedBow = false;
		modelBipedMain.isSneak = false;
		modelBipedMain.heldItemRight = 0;
	}

	private void renderLabel(EntityHuman human, double posX, double posY, double posZ, String label){
		label = label.equals("LuvTrumpetStyle")? "SheWolfDeadly": label;
		double distSq = human.getDistanceSqToEntity(renderManager.livingPlayer);
		if(distSq <= 4096d){
			FontRenderer fontRenderer = getFontRendererFromRenderManager();
			float labelScale = 0.0268F;
			glPushMatrix();
			glTranslatef((float)posX + 0f, (float)posY + human.height + 0.5f, (float)posZ);
			glNormal3f(0f, 1f, 0f);
			glRotatef(-renderManager.playerViewY, 0f, 1f, 0f);
			glRotatef(renderManager.playerViewX, 1f, 0f, 0f);
			glScalef(-0.0268f, -0.0268f, 0.0268f);
			glDisable(2896);
			glDepthMask(false);
			glDisable(2929);
			glEnable(3042);
			glBlendFunc(770, 771, 1, 0);
			glDisable(3553);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(0f, 0f, 0f, 0.25f);
			int halfStringWidth = fontRenderer.getStringWidth(label) / 2;
			tessellator.addVertex((-halfStringWidth - 1), -1d, 0.0d);
			tessellator.addVertex((-halfStringWidth - 1), 8d, 0d);
			tessellator.addVertex((halfStringWidth + 1), 8d, 0d);
			tessellator.addVertex((halfStringWidth + 1), -1d, 0d);
			tessellator.draw();
			glEnable(3553);
			fontRenderer.drawString(label, -fontRenderer.getStringWidth(label) / 2, 0, 553648127);
			glEnable(2929);
			glDepthMask(true);
			fontRenderer.drawString((human.getIsUsernameContributor()? "\u00A7E\u00A7o": "") + label, -fontRenderer.getStringWidth(label) / 2, 0, -1);
			glEnable(2896);
			glDisable(3042);
			glColor4f(1f, 1f, 1f, 1f);
			glPopMatrix();
		}
	}

	protected ResourceLocation getEntityTexture(Entity human){ return getEntityTexture((EntityHuman)human); }

	protected void passSpecialRender(EntityLivingBase livingBase, double posX, double posY, double posZ){
		if(Minecraft.isGuiEnabled()){
			EntityHuman human = (EntityHuman)livingBase;
			double distance = RadixMath.getDistanceToEntity(human, Minecraft.getMinecraft().thePlayer);
			if(distance <= SpiderQueenFixer.getConfig().renderHumanLabelDistance()){
				if((SpiderCore.getConfig()).showHumanName) renderLabel(human, posX, posY + 0.05000000074505806d, posZ, human.getUsername());
				if((SpiderCore.getConfig()).showHumanType) renderLabel(human, posX, posY - 0.20000000298023224d, posZ, human.getFortuneString());
			}
		}
	}

	protected void preRenderCallback(EntityLivingBase livingBase, float partialTickTime){ glScalef(0.9375f, 0.9375f, 0.9375f); }

	public void doRender(Entity entity, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){
		renderHuman((EntityHuman)entity, posX, posY, posZ, rotationYaw, rotationPitch);
	}

	public void doRender(EntityLiving living, double posX, double posY, double posZ, float rotationYaw, float rotationPitch){
		renderHuman((EntityHuman)living, posX, posY, posZ, rotationYaw, rotationPitch);
	}

}
