package com.sulvic.sqfixer.client.render;

import static com.sulvic.sqfixer.SpiderQueenFixer.getLogger;
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
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.util.*;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.*;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;

public class RenderRetexedPlayerQueen extends RenderPlayer{

	private ModelRetexedSpiderQueen modelSpiderQueen;
	private ModelBiped armFirstPerson;

	public RenderRetexedPlayerQueen(){
		super();
		modelSpiderQueen = new ModelRetexedSpiderQueen(0f);
		armFirstPerson = new ModelBiped();
	}

	private float interpolateRotation(float prevRotation, float rotation, float partialTicks){
		float mult = rotation - prevRotation;
		while(mult < -180f) mult += 360f;
		while(mult >= 180f) mult -= 360f;
		return prevRotation + partialTicks * mult;
	}

	private void renderLabel(EntityPlayer player, double posX, double posY, double posZ, String label){
		if(player.getDistanceSqToEntity(renderManager.livingPlayer) <= 4096d){
			FontRenderer fontRenderer = getFontRendererFromRenderManager();
			float labelScale = 0.0268F;
			glPushMatrix();
			glTranslatef((float)posX + 0f, (float)posY + player.height + 0.5f, (float)posZ);
			glNormal3f(0f, 1f, 0f);
			glRotatef(-renderManager.playerViewY, 0f, 1f, 0f);
			glRotatef(renderManager.playerViewX, 1f, 0f, 0f);
			glScalef(-labelScale, -labelScale, labelScale);
			glDisable(GL_LIGHTING);
			glDepthMask(false);
			glDisable(GL_DEPTH_TEST);
			glEnable(GL_BLEND);
			OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
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
			fontRenderer.drawString(label, -fontRenderer.getStringWidth(label) / 2, 0, 0xFFFFFFFF);
			glEnable(GL_LIGHTING);
			glDisable(GL_BLEND);
			glColor4f(1f, 1f, 1f, 1f);
			glPopMatrix();
		}
	}

	protected int shouldRenderPass(AbstractClientPlayer player, int renderPass, float partialTicks){ return -1; }

	protected ResourceLocation getEntityTexture(AbstractClientPlayer player){ return player.getLocationSkin(); }

	protected void func_82408_c(AbstractClientPlayer player, int renderPass, float partialTicks){}

	protected void func_96449_a(AbstractClientPlayer player, double posX, double posY, double posZ, String playerName, float par, double playerDist){}

	public void renderFirstPersonArm(EntityPlayer player){
		glColor3f(1f, 1f, 1f);
		modelSpiderQueen.onGround = 0f;
		modelSpiderQueen.setRotationAngles(0f, 0f, 0f, 0f, 0f, 0.0625f, player);
		glPushMatrix();
		glScalef(0.7f, 0.9f, 0.7f);
		glTranslated(-0.3d, 0.1d, 0d);
		try{
			SpiderQueenData playerData = SpiderQueenData.getPlayerData(player);
			if(playerData != null){
				bindTexture(playerData.getArmTexture());
				armFirstPerson.bipedRightArm.render(0.0625f);
			}
		}
		catch(NullPointerException ex){
			getLogger().error("The player must have been rendered while joining a world.");
			getLogger().catching(ex);
		}
		armFirstPerson.bipedRightLeg.render(0.0625f);
		glPopMatrix();
	}

	protected void passSpecialRender(AbstractClientPlayer player, double posX, double posY, double posZ){
		if(Minecraft.isGuiEnabled() && Minecraft.getMinecraft().currentScreen == null){
			double dist = player.getDistanceToEntity(Minecraft.getMinecraft().thePlayer);
			if(dist < (player.isSneaking()? NAME_TAG_RANGE_SNEAK: NAME_TAG_RANGE) && Minecraft.getMinecraft().thePlayer != player)
				renderLabel(player, posX, posY, posZ, player.getCommandSenderName());
		}
	}

	protected void preRenderCallback(AbstractClientPlayer player, float partialTicks){}

	protected void renderEquippedItems(AbstractClientPlayer player, float partialTicks){
		RenderPlayerEvent.Specials.Pre event = new RenderPlayerEvent.Specials.Pre(player, this, partialTicks);
		if(MinecraftForge.EVENT_BUS.post(event)) return;
		glColor3f(1f, 1f, 1f);
		glScaled(-1, -1, -1);
		glTranslated(-0.05d, 0.3d, -0.15d);
		super.renderArrowsStuckInEntity(player, partialTicks);
		ItemStack stack = player.inventory.armorItemInSlot(3);
		if(stack != null && event.renderHelmet){
			Item item = stack.getItem();
			glPushMatrix();
			modelSpiderQueen.getPlayerHead().postRender(0.0625F);
			if(item instanceof ItemBlock){
				IItemRenderer renderer = MinecraftForgeClient.getItemRenderer(stack, ItemRenderType.EQUIPPED);
				boolean is3D = (renderer != null && renderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, stack, ItemRendererHelper.BLOCK_3D));
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
					else if(nbtCompound.hasKey("SkullOwner", 8) && !StringUtils.isNullOrEmpty(nbtCompound.getString("SkullOwner")))
						profile = new GameProfile((UUID)null, nbtCompound.getString("SkullOwner"));
				}
				TileEntitySkullRenderer.field_147536_b.func_152674_a(-0.5f, 0f, -0.5f, 1, 180f, stack.getItemDamage(), profile);
			}
			glPopMatrix();
		}
		if(player.getCommandSenderName().equals("deadmau5") && player.func_152123_o()){
			bindTexture(player.getLocationSkin());
			for(int j = 0; j < 2; ++j){
				float rotationYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * partialTicks -
					(player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks);
				float rotationPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks;
				glPushMatrix();
				glRotatef(rotationYaw, 0f, 1f, 0f);
				glRotatef(rotationPitch, 1f, 0f, 0f);
				glTranslatef(0.375f * (j * 2 - 1), 0f, 0f);
				glTranslatef(0f, -0.375f, 0f);
				glRotatef(-rotationPitch, 1f, 0f, 0f);
				glRotatef(-rotationYaw, 0f, 1f, 0f);
				float scale = 1.3333334f;
				glScalef(scale, scale, scale);
				glPopMatrix();
			}
		}
		boolean flag = player.func_152122_n();
		flag = event.renderCape && flag;
		if(flag && !player.isInvisible() && !player.getHideCape()){
			bindTexture(player.getLocationCape());
			glPushMatrix();
			glTranslatef(0.0F, 0.0F, 0.125F);
			double capeX = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * partialTicks - (player.prevPosX + (player.posX - player.prevPosX) * partialTicks);
			double capeY = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * partialTicks - (player.prevPosY + (player.posY - player.prevPosY) * partialTicks);
			double capeZ = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * partialTicks - (player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks);
			float renderYaw = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks;
			double renderYawSin = MathHelper.sin(renderYaw * (float)Math.PI / 180f);
			double renderYawCos = (-MathHelper.cos(renderYaw * (float)Math.PI / 180f));
			float par = (float)capeY * 10.0F;
			if(par < -6f) par = -6f;
			if(par > 32f) par = 32f;
			float par1 = (float)(capeX * renderYawSin + capeZ * renderYawCos) * 100f;
			float par2 = (float)(capeX * renderYawCos - capeZ * renderYawSin) * 100f;
			if(par1 < 0f) par1 = 0f;
			float f9 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * partialTicks;
			par += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * partialTicks) * 6f) * 32f * f9;
			if(player.isSneaking()) par += 25f;
			glRotatef(6f + par1 / 2f + par, 1f, 0f, 0f);
			glRotatef(par2 / 2f, 0f, 0f, 1f);
			glRotatef(-par2 / 2f, 0f, 1f, 0f);
			glRotatef(180f, 0f, 1f, 0f);
			glPopMatrix();
		}
		ItemStack stack1 = player.inventory.getCurrentItem();
		if(stack1 != null && event.renderItem){
			glPushMatrix();
			modelSpiderQueen.getRightArm().postRender(0.0625f);
			glTranslatef(-0.0625f, 0.4375f, 0.0625f);
			if(player.fishEntity != null) stack1 = new ItemStack(Items.stick);
			EnumAction enumaction = null;
			if(player.getItemInUseCount() > 0) enumaction = stack1.getItemUseAction();
			IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(stack1, ItemRenderType.EQUIPPED);
			boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(ItemRenderType.EQUIPPED, stack1, ItemRendererHelper.BLOCK_3D));
			if(is3D || stack1.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(stack1.getItem()).getRenderType())){
				float scale = 0.5f;
				glTranslatef(0f, 0.1875f, -0.3125f);
				scale *= 0.75f;
				glRotatef(20f, 1f, 0F, 0f);
				glRotatef(45f, 0f, 1f, 0f);
				glScalef(-scale, -scale, scale);
			}
			else if(stack1.getItem() == Items.bow){
				float scale = 0.625F;
				glTranslatef(0f, 0.125f, 0.3125f);
				glRotatef(-20f, 0f, 1f, 0f);
				glScalef(scale, -scale, scale);
				glRotatef(-100f, 1f, 0f, 0f);
				glRotatef(45f, 0f, 1f, 0f);
			}
			else if(stack1.getItem().isFull3D()){
				float scale = 0.625f;
				if(stack1.getItem().shouldRotateAroundWhenRendering()){
					glRotatef(180f, 0f, 0f, 1f);
					glTranslatef(0f, -0.125f, 0.0f);
				}

				if(player.getItemInUseCount() > 0 && enumaction == EnumAction.block){
					glTranslatef(0.05f, 0F, -0.1f);
					glRotatef(-50f, 0f, 1f, 0f);
					glRotatef(-10f, 1f, 0f, 0f);
					glRotatef(-60f, 0f, 0f, 1f);
				}
				glTranslatef(0f, 0.1875f, 0F);
				glScalef(scale, -scale, scale);
				glRotatef(-100f, 1f, 0f, 0f);
				glRotatef(45f, 0f, 1f, 0f);
			}
			else{
				float scale = 0.375f;
				glTranslatef(0.25f, 0.1875f, -0.1875f);
				glScalef(scale, scale, scale);
				glRotatef(60f, 0f, 0f, 1f);
				glRotatef(-90f, 1f, 0f, 0f);
				glRotatef(20f, 0f, 0f, 1f);
			}
			if(stack1.getItem().requiresMultipleRenderPasses()) for(int i = 0; i <= stack1.getItem().getRenderPasses(stack1.getItemDamage()); i++){
				int colorMult = stack1.getItem().getColorFromItemStack(stack1, i);
				float red = (colorMult >> 16 & 255) / 255f;
				float green = (colorMult >> 8 & 255) / 255f;
				float blue = (colorMult & 255) / 255f;
				glColor4f(red, green, blue, 1f);
				renderManager.itemRenderer.renderItem(player, stack1, i);
			}
			else{
				int colorMult = stack1.getItem().getColorFromItemStack(stack1, 0);
				float red = (colorMult >> 16 & 255) / 255f;
				float green = (colorMult >> 8 & 255) / 255f;
				float blue = (colorMult & 255) / 255f;
				glColor4f(red, green, blue, 1f);
				renderManager.itemRenderer.renderItem(player, stack1, 0);
			}
			glPopMatrix();
		}
		MinecraftForge.EVENT_BUS.post(new RenderPlayerEvent.Specials.Post(player, this, partialTicks));
	}

	protected void renderLivingAt(AbstractClientPlayer player, double posX, double posY, double posZ){
		if(player.isEntityAlive() && player.isPlayerSleeping()) super.renderLivingAt(player, posX + player.field_71079_bU, posY + player.field_71082_cx, posZ + player.field_71089_bV);
		else super.renderLivingAt(player, posX, posY, posZ);
	}

	protected void renderModel(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		SpiderQueenData playerData = SpiderQueenData.getPlayerData(player);
		bindTexture(playerData.getMainTexture());
		if(!player.isInvisible()) modelSpiderQueen.render(player, limbSwing, limbSwingAmount, time, rotationYaw, rotationPitch, partialTicks);
		else if(!player.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer)){
			glPushMatrix();
			glColor4f(1f, 1f, 1f, 0.15f);
			glDepthMask(false);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glAlphaFunc(GL_GREATER, 0.003921569f);
			modelSpiderQueen.render(player, limbSwing, limbSwingAmount, time, rotationYaw, rotationPitch, partialTicks);
			glDisable(GL_BLEND);
			glAlphaFunc(GL_GREATER, 0.1f);
			glPopMatrix();
			glDepthMask(true);
		}
		else modelSpiderQueen.setRotationAngles(limbSwing, limbSwingAmount, time, rotationYaw, rotationPitch, partialTicks, player);
	}

	protected void rotateCorpse(AbstractClientPlayer player, float yawHead, float renderYaw, float partialTicks){
		if(player.isEntityAlive() && player.isPlayerSleeping()){
			glRotatef(player.getBedOrientationInDegrees(), 0f, 1f, 0f);
			glRotatef(getDeathMaxRotation(player), 0f, 0f, 1f);
			glRotatef(270f, 0f, 1f, 0f);
		}
		else super.rotateCorpse(player, yawHead, renderYaw, partialTicks);
	}

	protected int shouldRenderPass(EntityLivingBase livingBase, int renderPass, float partialTicks){ return -1; }

	protected void func_82408_c(EntityLivingBase livingBase, int renderPass, float partialTicks){}

	protected void func_96449_a(EntityLivingBase livingBase, double posX, double posY, double posZ, String playerName, float par, double playerDist){}

	protected void preRenderCallback(EntityLivingBase livinBase, float partialTicks){}

	protected void rotateCorpse(EntityLivingBase livingBase, float yawHead, float renderYaw, float partialTicks){
		rotateCorpse((AbstractClientPlayer)livingBase, yawHead, renderYaw, partialTicks);
	}

	protected void renderEquippedItems(EntityLivingBase livingBase, float partialTicks){ renderEquippedItems((AbstractClientPlayer)livingBase, partialTicks); }

	protected void renderLivingAt(EntityLivingBase livingBase, double posX, double posY, double posZ){ renderLivingAt((AbstractClientPlayer)livingBase, posX, posY, posZ); }

	protected void renderModel(EntityLivingBase livingBase, float limbSwing, float limbSwingAmount, float time, float rotationYaw, float rotationPitch, float partialTicks){
		renderModel((AbstractClientPlayer)livingBase, limbSwing, limbSwingAmount, time, rotationYaw, rotationPitch, partialTicks);
	}

	public void doRender(AbstractClientPlayer player, double posX, double posY, double posZ, float rotationYaw, float partialTicks){
		glColor3f(1f, 1f, 1f);
		if(player.isSneaking() && !(player instanceof EntityPlayerSP));
		glPushMatrix();
		EntityLivingBase livingBase = player;
		glDisable(GL_CULL_FACE);
		modelSpiderQueen.onGround = renderSwingProgress(livingBase, partialTicks);
		modelSpiderQueen.isRiding = livingBase.isRiding();
		if(renderPassModel != null){
			renderPassModel.onGround = mainModel.onGround;
			renderPassModel.isRiding = mainModel.isRiding;
		}
		if(player != Minecraft.getMinecraft().thePlayer) glTranslatef(0f, 1.7f, 0f);
		try{
			float truePartialTicks = -0.0925F;
			float rotationPitch = livingBase.prevRotationPitch + (livingBase.rotationPitch - livingBase.prevRotationPitch) * partialTicks;
			float renderYaw = interpolateRotation(livingBase.prevRenderYawOffset, livingBase.renderYawOffset, partialTicks);
			float rotationYawHead = interpolateRotation(livingBase.prevRotationYawHead, livingBase.rotationYawHead, partialTicks);
			float rotation = handleRotationFloat(livingBase, partialTicks);
			if(livingBase.isRiding() && livingBase.ridingEntity instanceof EntityLivingBase){
				EntityLivingBase entityRiding = (EntityLivingBase)livingBase.ridingEntity;
				renderYaw = interpolateRotation(entityRiding.prevRenderYawOffset, entityRiding.renderYawOffset, partialTicks);
				rotation = MathHelper.wrapAngleTo180_float(rotationYawHead - renderYaw);
				if(rotation < -85f) rotation = -85f;
				if(rotation >= 85f) rotation = 85f;
				renderYaw = rotationYawHead - rotation;
				if(rotation * rotation > 2500f) renderYaw += rotation * 0.2f;
			}
			renderLivingAt(livingBase, posX, posY, posZ);
			rotateCorpse(livingBase, rotation, renderYaw, partialTicks);
			glTranslatef(0.0F, -0.10F, 0.0F);
			glScalef(0.7F, 0.7F, -0.7F);
			float limbSwingAmount = livingBase.prevLimbSwingAmount + (livingBase.limbSwingAmount - livingBase.prevLimbSwingAmount) * partialTicks;
			final float limbSwing = livingBase.limbSwing - livingBase.limbSwingAmount * (1.0F - partialTicks);
			if(limbSwingAmount > 1f) limbSwingAmount = 1f;
			mainModel.setLivingAnimations(livingBase, limbSwing, limbSwingAmount, partialTicks);
			renderModel(livingBase, limbSwing, limbSwingAmount, rotation, rotationYawHead - renderYaw, rotationPitch, truePartialTicks);
			try{
				renderEquippedItems(livingBase, limbSwingAmount);
			}
			catch(Throwable ex){
				getLogger().catching(ex);
			}
			final float brightness = livingBase.getBrightness(partialTicks);
			final int colorMult = getColorMultiplier(livingBase, brightness, partialTicks);
			if((colorMult >> 24 & 255) > 0 || livingBase.hurtTime > 0 || livingBase.deathTime > 0){
				glDisable(GL_TEXTURE_2D);
				glDisable(GL_ALPHA_TEST);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				glDepthFunc(GL_EQUAL);
				if(livingBase.hurtTime > 0 || livingBase.deathTime > 0){
					glColor4f(brightness, 0f, 0f, 0.4f);
					modelSpiderQueen.render(player, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks);
				}
				if((colorMult >> 24 & 255) > 0){
					float red = (colorMult >> 16 & 255) / 255f;
					float green = (colorMult >> 8 & 255) / 255f;
					float blue = (colorMult & 255) / 255f;
					float alpha = (colorMult >> 24 & 255) / 255f;
					glColor4f(red, green, blue, alpha);
					modelSpiderQueen.render(player, limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks);
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
		glPopMatrix();
	}

	public void doRender(EntityLivingBase livingBase, double posX, double posY, double posZ, float rotationYaw, float partialTicks){
		doRender((AbstractClientPlayer)livingBase, posX, posY, posZ, rotationYaw, partialTicks);
		passSpecialRender((AbstractClientPlayer)livingBase, posX, posY, posZ);
	}

	protected void passSpecialRender(EntityLivingBase livingBase, double posX, double posY, double posZ){ passSpecialRender((AbstractClientPlayer)livingBase, posX, posY, posZ); }

}
