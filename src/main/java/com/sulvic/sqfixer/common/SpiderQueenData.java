package com.sulvic.sqfixer.common;

import static com.sulvic.sqfixer.SpiderFixerReference.*;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import sq.core.SpiderCore;

public class SpiderQueenData{

	private static final Map<UUID, SpiderQueenData> PLAYER_SKINS = Maps.newHashMap();
	private EnumSkin currentSkin;

	private SpiderQueenData(){ currentSkin = EnumSkin.QUEEN1; }

	private static SpiderQueenData makePlayerData(UUID id){
		SpiderQueenData result = new SpiderQueenData();
		PLAYER_SKINS.put(id, result);
		return result;
	}

	public static boolean hasPlayerData(EntityPlayer player){
		if(player == null) throw new NullPointerException("The player cannot be null");
		UUID id = null;
		GameProfile profile = player.getGameProfile();
		if(profile != null) id = profile.getId();
		else id = player.getUniqueID();
		return PLAYER_SKINS.containsKey(id);
	}

	public static SpiderQueenData getPlayerData(EntityPlayer player){
		if(player == null) throw new NullPointerException("The player cannot be null");
		UUID id = null;
		GameProfile profile = player.getGameProfile();
		if(profile != null) id = profile.getId();
		else id = player.getUniqueID();
		return hasPlayerData(player)? PLAYER_SKINS.get(id): makePlayerData(id);
	}

	public void changeSkin(EntityPlayer player){
		byte next = (byte)(currentSkin.ordinal() + 1);
		if(next == 5) next = 0;
		currentSkin = EnumSkin.byId(next);
		SpiderCore.getPlayerData(player).isMale.setValue(currentSkin == EnumSkin.KING);
	}

	public EnumSkin getCurrentSkin(){ return currentSkin; }

	public ResourceLocation getMainTexture(){ return currentSkin.getMainTexture(); }
	
	public ResourceLocation getArmTexture(){ return currentSkin.getArmTexture(); }

	public void readFromNBT(NBTTagCompound nbtCompound){ currentSkin = EnumSkin.byId(nbtCompound.getByte("SpiderQueenSkinID")); }

	public void writeToNBT(NBTTagCompound nbtCompound){ nbtCompound.setByte("SpiderQueenSkinID", currentSkin.getId()); }

	public static enum EnumSkin{

		QUEEN1,
		QUEEN2,
		QUEEN3,
		QUEEN4,
		KING;

		public static EnumSkin byId(byte id){
			switch(id){
				case 0:
					return QUEEN1;
				case 1:
					return QUEEN2;
				case 2:
					return QUEEN3;
				case 3:
					return QUEEN4;
				case 4:
					return KING;
				default:
					return QUEEN1;
			}
		}

		public byte getId(){ return (byte)ordinal(); }

		public String getTextureName(){
			String name = this.name();
			return "Spider" + name.charAt(0) + name.substring(1).toLowerCase();
		}

		public ResourceLocation getMainTexture(){ return new ResourceLocation(MODID, "textures/entities/spider" + name().toLowerCase() + ".png"); }

		public ResourceLocation getArmTexture(){ return new ResourceLocation(MODID, "textures/entities/arms/spider" + name().toLowerCase() + ".png"); }

	}

}
