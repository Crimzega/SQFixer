package com.sulvic.sqfixer;

import static com.sulvic.sqfixer.SpiderFixerReference.*;
import static com.sulvic.sqfixer.SpiderQueenFixer.*;
import static net.minecraft.server.MinecraftServer.*;

import java.io.*;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.sulvic.sqfixer.common.SpiderQueenData;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.world.WorldEvent;
import radixcore.util.RadixMath;
import sq.core.SpiderCore;
import sq.core.minecraft.ModAchievements;
import sq.entity.ai.PlayerExtension;
import sq.packet.PacketSleepC;

public class FixerEvents{

	private static FixerEvents instance = new FixerEvents();
	private static final int SLEEP_CHECK_TIME = 20, STRING_TIME = 3600;
	private int checkSleepingCooldown, stringGenCooldown;

	private FixerEvents(){}

	public static FixerEvents getInstance(){ return instance; }

	public static File getSpiderqueensFolder(World world){
		File result = new File(world.getSaveHandler().getWorldDirectory(), "playerdata-sqfixer");
		if(!result.exists()) result.mkdir();
		return result;
	}

	private void attemptAddNightVision(EntityLivingBase livingBase){ if(SpiderCore.getConfig().enableNightVision) attemptAddPotionEffect(livingBase, Potion.nightVision, 12000, 0, true); }

	private void attemptAddPotionEffect(EntityLivingBase livingBase, Potion potion, int time, int amp, boolean ambient){
		if(!livingBase.isPotionActive(potion)) livingBase.addPotionEffect(new PotionEffect(potion.id, time, amp, ambient));
	}

	private void attemptRemovePotionEffect(EntityLivingBase livingBase, Potion potion){ if(livingBase.isPotionActive(potion)) livingBase.removePotionEffect(potion.id); }

	private File getPlayerData(World world, EntityPlayer player){
		UUID id = player.getGameProfile().getId();
		return new File(getSpiderqueensFolder(world), id.toString() + ".dat");
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent evt){
		if(evt.modID.equals(MODID)){
			getConfig().save();
			getConfig().sync();
		}
	}

	@SubscribeEvent
	public void onPlayerCreated(EntityEvent.EntityConstructing evt){
		Entity entity = evt.entity;
		if(entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)entity;
			if(!SpiderQueenData.hasPlayerData(player)) SpiderQueenData.getPlayerData(player);
		}
	}

	@SubscribeEvent
	public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent evt){
		EntityPlayer player = evt.player;
		GameProfile profile = player.getGameProfile();
		World world = player.worldObj;
		SpiderQueenData skinner = SpiderQueenData.getPlayerData(evt.player);
		try{
			File file = getPlayerData(world, player);
			if(file.exists()) skinner.readFromNBT(CompressedStreamTools.readCompressed(new FileInputStream(file)));
		}
		catch(IOException ex){
			getLogger().error("Unable to read spiderqueen texture info for {}", profile.getName());
			getLogger().catching(ex);
		}
	}

	@SubscribeEvent
	public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent evt){
		EntityPlayer player = evt.player;
		GameProfile profile = player.getGameProfile();
		World world = player.worldObj;
		SpiderQueenData skinner = SpiderQueenData.getPlayerData(player);
		try{
			NBTTagCompound nbtCompound = new NBTTagCompound();
			skinner.writeToNBT(nbtCompound);
			CompressedStreamTools.writeCompressed(nbtCompound, new FileOutputStream(getPlayerData(world, player)));
		}
		catch(IOException ex){
			getLogger().error("Unable to write spiderqueen texture info for {}", profile.getName());
			getLogger().catching(ex);
		}
	}

	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save evt){
		World world = evt.world;
		for(Object obj: world.playerEntities){
			EntityPlayer player = (EntityPlayer)obj;
			GameProfile profile = player.getGameProfile();
			SpiderQueenData skinner = SpiderQueenData.getPlayerData(player);
			try{
				NBTTagCompound nbtCompound = new NBTTagCompound();
				skinner.writeToNBT(nbtCompound);
				CompressedStreamTools.writeCompressed(nbtCompound, new FileOutputStream(getPlayerData(world, player)));
			}
			catch(IOException ex){
				getLogger().error("Unable to write spiderqueen texture info for {}", profile.getName());
				getLogger().catching(ex);
			}
		}
	}

	@SubscribeEvent
	public void onMakeString(TickEvent.ServerTickEvent evt){
		if(stringGenCooldown > 0) stringGenCooldown--;
		else{
			for(WorldServer worldServer: getServer().worldServers) for(Object obj: worldServer.playerEntities){
				EntityPlayer player = (EntityPlayer)obj;
				if(!player.capabilities.isCreativeMode || getConfig().allowCreativeString())
					player.inventory.addItemStackToInventory(new ItemStack(Items.string, RadixMath.getNumberInRange(1, 3)));
			}
			getLogger().info("All possible players should have gotten string.");
			stringGenCooldown = STRING_TIME;
		}
	}

	@SubscribeEvent
	public void onPlayersSleeping(TickEvent.ServerTickEvent evt){
		if(checkSleepingCooldown > 0) checkSleepingCooldown--;
		else{
			int totalPlayers = getServer().getCurrentPlayerCount();
			if(SpiderCore.sleepingPlayers.size() >= totalPlayers && totalPlayers > 0){
				for(String playerName: SpiderCore.sleepingPlayers) for(WorldServer worldServer: getServer().worldServers){
					EntityPlayerMP playerMp = (EntityPlayerMP)worldServer.getPlayerEntityByName(playerName);
					if(playerMp != null){
						playerMp.setSpawnChunk(new ChunkCoordinates((int)playerMp.posX, (int)playerMp.posY, (int)playerMp.posZ), true, playerMp.worldObj.provider.dimensionId);
						SpiderCore.getPacketHandler().sendPacketToPlayer(new PacketSleepC(true), playerMp);
					}
				}
				getServer().worldServers[0].provider.setWorldTime(13000L);
				checkSleepingCooldown = SLEEP_CHECK_TIME;
			}
		}
	}

	@SubscribeEvent
	public void onTimeCheck(TickEvent.ServerTickEvent evt){
		for(WorldServer worldServer: getServer().worldServers) for(Object obj: worldServer.playerEntities){
			EntityPlayerMP playerMp = (EntityPlayerMP)obj;
			PlayerExtension playerExt = PlayerExtension.get(playerMp);
			if(playerExt != null) playerExt.tick();
			int x = (int)playerMp.posX, y = (int)playerMp.posY, z = (int)playerMp.posZ;
			if(playerMp.worldObj.getBlockLightValue(x, y, z) <= 8){
				playerMp.triggerAchievement(ModAchievements.goInTheDark);
				attemptAddNightVision(playerMp);
				attemptRemovePotionEffect(playerMp, Potion.weakness);
			}
			else{
				if(playerMp.worldObj.canBlockSeeTheSky(x, y, z)){
					attemptRemovePotionEffect(playerMp, Potion.nightVision);
					attemptAddPotionEffect(playerMp, Potion.weakness, 12000, 0, true);
				}
			}
		}
	}

}
