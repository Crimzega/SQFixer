package com.sulvic.sqfixer.client.render;

import static com.sulvic.sqfixer.SpiderQueenFixer.getLogger;
import static net.minecraftforge.client.IItemRenderer.ItemRenderType.EQUIPPED;
import static net.minecraftforge.client.IItemRenderer.ItemRendererHelper.BLOCK_3D;
import static org.lwjgl.opengl.GL11.*;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.sulvic.sqfixer.client.model.ModelRetexedSpiderQueen;
import com.sulvic.sqfixer.common.SpiderQueenData;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.*;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.client.*;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

public class RenderRetexedPlayerQueen extends RenderPlayer{

	private ModelRetexedSpiderQueen modelSpiderQueen;
	private ModelBiped armFirstPerson;

	public RenderRetexedPlayerQueen(){
		super();
		modelSpiderQueen = new ModelRetexedSpiderQueen(0f);
		armFirstPerson = new ModelBiped();
		mainModel = modelSpiderQueen;
	}

	private float interpolateRotation(float prevRotation, float rotation, float partialTicks){
		float mult = rotation - prevRotation;
		while(mult < -180f) mult += 360f;
		while(mult >= 180f) mult -= 360f;
		return prevRotation + partialTicks * mult;
	}

	private void renderLabel(Entity entity, double x, double y, double z, String text){
		if(entity.getDistanceSqToEntity(renderManager.livingPlayer) < 4096d){
			FontRenderer renderer = getFontRendererFromRenderManager();
			float scale = 0.0268f;
			glPushMatrix();
			glTranslatef((float)x, (float)y + entity.height + 0.5f, (float)z);
			glNormal3f(0f, 1f, 0f);
			glRotatef(-renderManager.playerViewY, 0f, 1f, 0f);
			glRotatef(renderManager.playerViewX, 1f, 0f, 0f);
			glScalef(-scale, -scale, scale);
			glDisable(GL_LIGHTING);
			glDepthMask(false);
			glDisable(GL_DEPTH_TEST);
			glEnable(GL_BLEND);
			OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 0, 1);
			glDisable(GL_TEXTURE_2D);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA_F(0f, 0f, 0f, 0.25f);
			int strHalfWidth = renderer.getStringWidth(text) / 2;
			tessellator.addVertex(-strHalfWidth - 1, -1, 0d);
			tessellator.addVertex(-strHalfWidth - 1, 8, 0d);
			tessellator.addVertex(strHalfWidth + 1, 8, 0d);
			tessellator.addVertex(strHalfWidth + 1, -1, 0d);
			tessellator.draw();
			glEnable(GL_TEXTURE_2D);
			renderer.drawString(text, -renderer.getStringWidth(text) / 2, 0, 0x20FFFFFF);
			glEnable(GL_DEPTH_TEST);
			glDepthMask(true);
			renderer.drawString(text, -renderer.getStringWidth(text) / 2, 0, 0xFFFFFFFF);
			glEnable(GL_LIGHTING);
			glDisable(GL_BLEND);
			glColor4f(1f, 1f, 1f, 1f);
			glPopMatrix();
		}
	}

	protected int shouldRenderPass(AbstractClientPlayer player, int pass, float partialTicks){ return -1; }

	protected int shouldRenderPass(EntityLivingBase player, int pass, float partialTicks){ return -1; }

	protected ResourceLocation getEntityTexture(AbstractClientPlayer player){ return player.getLocationSkin(); }

	protected void bindEntityTexture(AbstractClientPlayer player){ bindTexture(SpiderQueenData.getPlayerData(player).getMainTexture()); }

	protected void bindEntityTexture(Entity entity){ bindEntityTexture((AbstractClientPlayer)entity); }

	protected void func_82408_c(AbstractClientPlayer player, int pass, float partialTicks){}

	protected void func_82408_c(EntityLivingBase livingBase, int pass, float partialTicks){}

	protected void func_96449_a(AbstractClientPlayer player, double x, double y, double z, String name, float par, double dist){}

	protected void func_96449_a(EntityLivingBase player, double x, double y, double z, String name, float par, double dist){}

	protected void preRenderCallback(AbstractClientPlayer player, float partialTicks){}

	protected void preRenderCallback(EntityLivingBase livingBase, float partialTicks){}

	protected void passSpecialRender(EntityLivingBase livingBase, double x, double y, double z){
		if(Minecraft.isGuiEnabled() && Minecraft.getMinecraft().currentScreen == null){
			EntityPlayer player = (EntityPlayer)livingBase;
			double dist = player.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer);
			if(player != Minecraft.getMinecraft().thePlayer && dist < (player.isSneaking()? NAME_TAG_RANGE_SNEAK: NAME_TAG_RANGE))
				renderLabel(player, x, y + 0.05f, y, player.getCommandSenderName());
		}
	}

	protected void renderEquippedItems(AbstractClientPlayer player, float partialTicks){
		RenderPlayerEvent.Specials.Pre evt = new RenderPlayerEvent.Specials.Pre(player, this, partialTicks);
		if(MinecraftForge.EVENT_BUS.post(evt)) return;
		glColor3f(1f, 1f, 1f);
		glScaled(-1d, -1d, -1d);
		glTranslated(-0.05d, 0.3d, -0.15d);
		super.renderArrowsStuckInEntity(player, partialTicks);
		ItemStack stack = player.inventory.armorItemInSlot(3);
		if(stack != null && evt.renderHelmet){
			glPushMatrix();
			modelSpiderQueen.getPlayerHead().postRender(0.0625f);
			Item item = stack.getItem();
			if(item instanceof ItemBlock){
				IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(stack, EQUIPPED);
				boolean is3D = renderer != null && renderer.shouldUseRenderHelper(EQUIPPED, stack, BLOCK_3D);
				if(is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())){
					float scale = 0.625f;
					glTranslatef(0f, -0.25f, 0f);
					glRotatef(90f, 0f, 1f, 0f);
					glScalef(scale, -scale, -scale);
				}
				renderManager.itemRenderer.renderItem(player, stack, 0);
			}
			else if(item == Items.skull){
				float scale = 1.0625f;
				glScalef(scale, -scale, -scale);
				GameProfile profile = null;
				if(stack.hasTagCompound()){
					NBTTagCompound nbtCompound = stack.getTagCompound();
					if(nbtCompound.hasKey("SkullOwner", 10)) profile = NBTUtil.func_152459_a(nbtCompound.getCompoundTag("SkullOwner"));
					else if(nbtCompound.hasKey("SkullOwner", 8) && StringUtils.isNullOrEmpty(nbtCompound.getString("SkullOwner")))
						profile = new GameProfile((UUID)null, nbtCompound.getString("SkullOwner"));
				}
				TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5f, 0f, -0.5f, 1, 180f, stack.getItemDamage(), profile);
			}
		}
		if(player.getCommandSenderName().equals("deadmau5") && player.func_152123_o()){
			bindTexture(player.getLocationSkin());
			for(int i = 0; i < 2; i++){
				float rotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks;
				rotationYaw -= player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
				float rotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
				glPushMatrix();
				glRotatef(rotationYaw, 0f, 1f, 0f);
				glRotatef(rotationPitch, 1f, 0f, 0f);
				glTranslatef(0.375f * (i * 2 - 1), 0f, 0f);
				glTranslatef(0f, -0.375f, 0f);
				glRotatef(-rotationPitch, 1f, 0f, 0f);
				glRotatef(-rotationYaw, 0f, 1f, 0f);
				float scale = 1.3333334f;
				glScalef(scale, scale, scale);
				glPopMatrix();
			}
		}
		if(evt.renderCape && player.func_152122_n() && !player.isInvisible() && !player.getHideCape()){
			bindTexture(player.getLocationCape());
			glPushMatrix();
			glTranslatef(0f, 0f, 0.125f);
			double unk = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * partialTicks;
			double unk2 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * partialTicks;
			double unk4 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * partialTicks;
			float renderYawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
			double sin = MathHelper.sin(renderYawOffset * (float)Math.PI / 180f);
			double cos = -MathHelper.cos(renderYawOffset * (float)Math.PI / 180f);
			float unk2f = MathHelper.clamp_float((float)unk2 * 10f, -6f, 32f);
			float f = Math.max((float)(unk * sin + unk4 * cos) * 100f, 0f), f1 = (float)(unk * cos + unk4 * cos) * 100f;
			float cameraYaw = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
			unk2f += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6f) * 32f * cameraYaw;
			if(player.isSneaking()) unk2f += 25f;
			glRotatef(6f + f / 2f + unk2f, 1f, 0f, 0f);
			glRotatef(f1 / 2f, 0f, 0f, 1f);
			glRotatef(-f1 / 2f, 0f, 1f, 0f);
			glRotatef(180f, 0f, 1f, 0f);
			glPopMatrix();
		}
		ItemStack stack1 = player.inventory.getCurrentItem();
		if(stack1 != null && evt.renderItem){
			glPushMatrix();
			modelSpiderQueen.getRightArm().postRender(0.0625f);
			glTranslatef(-0.0625f, 0.4375F, 0.0625f);
			if(player.fishEntity != null) stack1 = new ItemStack(Items.stick);
			EnumAction action = null;
			if(player.getItemInUseCount() > 0) action = stack1.getItemUseAction();
			IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(stack1, EQUIPPED);
			boolean is3D = (renderer != null && renderer.shouldUseRenderHelper(EQUIPPED, stack1, BLOCK_3D));
			Item item = stack1.getItem();
			if(is3D || item instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(item).getRenderType())){
				float scale = 0.375F;
				glTranslatef(0f, 0.1875f, -0.3125f);
				glRotatef(20f, 1f, 0.0F, 0f);
				glRotatef(45f, 0f, 1.0F, 0f);
				glScalef(-scale, -scale, scale);
			}
			else if(item == Items.bow){
				float scale = 0.625F;
				glTranslatef(0f, 0.125f, 0.3125f);
				glRotatef(-20f, 0f, 1f, 0f);
				glScalef(scale, -scale, scale);
				glRotatef(-100f, 1f, 0f, 0f);
				glRotatef(45f, 0f, 1f, 0f);
			}
			else if(item.isFull3D()){
				float scale = 0.625F;
				if(stack1.getItem().shouldRotateAroundWhenRendering()){
					glRotatef(180f, 0f, 0F, 1f);
					glTranslatef(0f, -0.125f, 0f);
				}
				if(player.getItemInUseCount() > 0 && action == EnumAction.block){
					glTranslatef(0.05f, 0f, -0.1f);
					glRotatef(-50f, 0f, 1f, 0f);
					glRotatef(-10f, 1f, 0f, 0f);
					glRotatef(-60f, 0f, 0f, 1f);
				}
				glTranslatef(0.0f, 0.1875f, 0f);
				glScalef(scale, -scale, scale);
				glRotatef(-100f, 1f, 0f, 0f);
				glRotatef(45f, 0F, 1f, 0f);
			}
			else{
				float scale = 0.375f;
				glTranslatef(0.25f, 0.1875f, -0.1875f);
				glScalef(scale, scale, scale);
				glRotatef(60f, 0f, 0f, 1f);
				glRotatef(-90f, 1f, 0f, 0f);
				glRotatef(20f, 0f, 0f, 1f);
			}
			if(item.requiresMultipleRenderPasses()){
				for(int i = 0; i < item.getRenderPasses(stack1.getItemDamage()); i++){
					int clr = stack.getItem().getColorFromItemStack(stack1, i);
					float r = (float)(clr >> 16 & 0xFF) / 255f;
					float g = (float)(clr >> 8 & 0xFF) / 255f;
					float b = (float)(clr & 0xFF) / 255f;
					glColor4f(r, g, b, 1f);
					renderManager.itemRenderer.renderItem(player, stack1, i);
				}
			}
			else{
				int clr = item.getColorFromItemStack(stack1, 0);
				float f11 = (float)(clr >> 16 & 0xFF) / 255f;
				float f12 = (float)(clr >> 8 & 0xFF) / 255f;
				float f3 = (float)(clr & 0xFF) / 255f;
				glColor4f(f11, f12, f3, 1f);
				renderManager.itemRenderer.renderItem(player, stack1, 0);
			}
			glPopMatrix();
		}
		MinecraftForge.EVENT_BUS.post(new RenderPlayerEvent.Specials.Post(player, this, partialTicks));
	}

	protected void renderEquippedItems(EntityLivingBase player, float partialTicks){ renderEquippedItems((AbstractClientPlayer)player, partialTicks); }

	protected void renderLivingAt(AbstractClientPlayer player, double x, double y, double z){
		if(player.isEntityAlive() && player.isPlayerSleeping()) super.renderLivingAt(player, x + player.field_71079_bU, y + player.field_71082_cx, z + player.field_71089_bV);
		else super.renderLivingAt(player, x, y, z);
	}

	protected void renderLivingAt(EntityLivingBase player, double x, double y, double z){ renderLivingAt((AbstractClientPlayer)player, x, y, z); }

	protected void rotateCorpse(AbstractClientPlayer player, float x, float y, float z){
		if(player.isEntityAlive() && player.isPlayerSleeping()){
			glRotatef(player.getBedOrientationInDegrees(), 0f, 1f, 0f);
			glRotatef(getDeathMaxRotation(player), 0f, 0f, 1f);
			glRotatef(270f, 0f, 1f, 0f);
		}
		else super.rotateCorpse(player, x, y, z);
	}

	protected void rotateCorpse(EntityLivingBase player, float x, float y, float z){ rotateCorpse((AbstractClientPlayer)player, x, y, z); }

	public void doRender(AbstractClientPlayer player, double x, double y, double z, float rotationYaw, float partialTicks){
		glColor3f(1f, 1f, 1f);
		glPushMatrix();
		EntityLivingBase livingBase = (EntityLivingBase)player;
		glDisable(GL_CULL_FACE);
		mainModel.onGround = renderSwingProgress(livingBase, partialTicks);
		mainModel.isRiding = livingBase.isRiding();
		if(renderPassModel != null){
			renderPassModel.onGround = mainModel.onGround;
			renderPassModel.isRiding = mainModel.isRiding;
		}
		if(player != Minecraft.getMinecraft().thePlayer) glTranslatef(0f, 1.7f, 0f);
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null){
			Item item = stack.getItem();
			if(item != null){
				EnumAction action = null;
				if(player.getItemInUseCount() > 0) action = stack.getItemUseAction();
				if(action == EnumAction.block) modelSpiderQueen.heldItemRight = armFirstPerson.heldItemRight = 3;
				if(action == EnumAction.bow) modelSpiderQueen.aimedBow = armFirstPerson.aimedBow = true;
			}
		}
		try{
			float newPartialTicks = -0.0925f;
			float rotationPitch = livingBase.prevRotationPitch + (livingBase.rotationPitch - livingBase.prevRotationPitch) * partialTicks;
			float renderYaw = interpolateRotation(livingBase.prevRenderYawOffset, livingBase.renderYawOffset, partialTicks);
			float renderYawHead = interpolateRotation(livingBase.prevRotationYawHead, livingBase.rotationYawHead, partialTicks);
			float wrappedRotation = handleRotationFloat(livingBase, partialTicks);
			if(livingBase.isRiding() && livingBase.ridingEntity instanceof EntityLivingBase){
				EntityLivingBase riddingEntity = (EntityLivingBase)livingBase.ridingEntity;
				renderYaw = interpolateRotation(riddingEntity.prevRenderYawOffset, riddingEntity.renderYawOffset, partialTicks);
				wrappedRotation = MathHelper.clamp_float(MathHelper.wrapAngleTo180_float(renderYawHead - renderYaw), -85f, 85f);
				renderYaw = renderYawHead - wrappedRotation;
				if(wrappedRotation * wrappedRotation > 2500f) renderYaw += wrappedRotation * 0.2f;
			}
			renderLivingAt(livingBase, x, y, z);
			rotateCorpse(livingBase, wrappedRotation, renderYaw, partialTicks);
			glTranslatef(0f, -0.1f, 0f);
			glScalef(0.7f, 0.7f, -0.7f);
			float limbSwing = Math.min(livingBase.prevLimbSwingAmount + (livingBase.limbSwingAmount - livingBase.prevLimbSwingAmount) * partialTicks, 1f);
			float limbAngle = livingBase.limbSwing - livingBase.limbSwingAmount * (1f - partialTicks);
			mainModel.setLivingAnimations(livingBase, limbAngle, limbSwing, partialTicks);
			renderModel(player, limbAngle, limbSwing, wrappedRotation, renderYawHead - renderYaw, rotationPitch, newPartialTicks);
			try{
				renderEquippedItems(livingBase, limbSwing);
			}
			catch(Throwable ex){
				getLogger().catching(ex);
			}
			float brightness = player.getBrightness(partialTicks);
			int clrMult = getColorMultiplier(player, brightness, partialTicks);
			if((clrMult >> 24 & 0xFF) > 0 || player.hurtTime > 0 || player.deathTime > 0){
				glDisable(GL_TEXTURE_2D);
				glDisable(GL_ALPHA_TEST);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glDepthFunc(GL_EQUAL);
				if(livingBase.hurtTime > 0 || livingBase.deathTime > 0){
					glColor4f(brightness, 0f, 0f, 0.4f);
					mainModel.render(livingBase, limbAngle, limbSwing, wrappedRotation, renderYawHead - renderYaw, rotationPitch, newPartialTicks);
				}
				if((clrMult >> 24 & 0xFF) > 0){
					float r = (clrMult >> 16 & 0xFF) / 255f, g = (clrMult >> 8 & 0xFF) / 255f, b = (clrMult & 0xFF) / 255f, a = (clrMult >> 24 & 0xFF) / 255f;
					glColor4f(r, g, b, a);
					mainModel.render(livingBase, limbAngle, limbSwing, wrappedRotation, renderYawHead - renderYaw, rotationPitch, newPartialTicks);
				}
				glDepthFunc(GL_LEQUAL);
				glDisable(GL_BLEND);
				glEnable(GL_ALPHA_TEST);
				glEnable(GL_TEXTURE_2D);
			}
		}
		catch(Exception ex){
			getLogger().error("Failed to render entity.");
			getLogger().catching(ex);
		}
		modelSpiderQueen.aimedBow = armFirstPerson.aimedBow = false;
		modelSpiderQueen.heldItemRight = armFirstPerson.heldItemRight = 0;
		glPopMatrix();
	}

	public void doRender(Entity entity, double x, double y, double z, float rotationYaw, float partialTicks){ doRender((AbstractClientPlayer)entity, x, y, z, rotationYaw, partialTicks); }

	public void doRender(EntityLivingBase livingBase, double x, double y, double z, float rotationYaw, float partialTicks){
		doRender((AbstractClientPlayer)livingBase, x, y, z, rotationYaw, partialTicks);
	}

	public void renderFirstPersonArm(EntityPlayer player){
		glColor3f(1f, 1f, 1f);
		float partialTicks = 0.0625f;
		modelSpiderQueen.onGround = 0f;
		modelSpiderQueen.setRotationAngles(0f, 0f, 0f, 0f, 0f, partialTicks, player);
		glPushMatrix();
		glScalef(0.7f, 0.9f, 0.7f);
		glTranslated(-0.3d, 0.1d, 0d);
		try{
			bindTexture(SpiderQueenData.getPlayerData(player).getArmTexture());
		}
		catch(NullPointerException ex){
			getLogger().catching(ex);
		}
		armFirstPerson.bipedRightArm.render(partialTicks);
		glPopMatrix();
	}

}
