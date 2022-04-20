package com.sulvic.sqfixer.handler;

import com.sulvic.sqfixer.SpiderQueenFixer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatBase;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import radixcore.util.RadixMath;
import sq.core.SpiderCore;
import sq.core.minecraft.ModAchievements;
import sq.core.minecraft.Spawner;
import sq.entity.ai.PlayerExtension;
import sq.packet.PacketSleepC;

public class ServerTickHandler{
	
	private int counter, timeUntilSpawnWeb, timeUntilSpawnerRun;
	
	public ServerTickHandler(){}
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent evt){
		timeUntilSpawnWeb--;
		if(timeUntilSpawnWeb <= 0){
			for(WorldServer worldServer: (MinecraftServer.getServer()).worldServers){
				for(Object obj: worldServer.playerEntities){
					EntityPlayer player = (EntityPlayer)obj;
					if(!player.capabilities.isCreativeMode) player.inventory.addItemStackToInventory(new ItemStack(Items.string, RadixMath.getNumberInRange(1, 3)));
					SpiderQueenFixer.getLogger().info("All possible players should have gotten string");
				}
			}
			timeUntilSpawnWeb = 3600;
		}
		if(counter <= 0){
			int totalPlayers = MinecraftServer.getServer().getCurrentPlayerCount();
			if(SpiderCore.sleepingPlayers.size() >= totalPlayers && totalPlayers != 0){
				for(String s: SpiderCore.sleepingPlayers){
					for(WorldServer world: (MinecraftServer.getServer()).worldServers){
						EntityPlayerMP player = (EntityPlayerMP)world.getPlayerEntityByName(s);
						if(player != null){
							player.setSpawnChunk(new ChunkCoordinates((int)player.posX, (int)player.posY, (int)player.posZ), true, player.worldObj.provider.dimensionId);
							SpiderCore.getPacketHandler().sendPacketToPlayer((IMessage)new PacketSleepC(true), player);
						}
					}
				}
				((MinecraftServer.getServer()).worldServers[0]).provider.setWorldTime(13000L);
				SpiderCore.sleepingPlayers.clear();
			}
			counter = 20;
		}
		counter--;
		for(WorldServer worldServer: (MinecraftServer.getServer()).worldServers){
			for(Object obj: worldServer.playerEntities){
				EntityPlayer player = (EntityPlayer)obj;
				PlayerExtension extension = PlayerExtension.get(player);
				extension.tick();
				if(player.worldObj.getBlockLightValue((int)player.posX, (int)player.posY, (int)player.posZ) <= 8){
					player.triggerAchievement((StatBase)ModAchievements.goInTheDark);
					if((SpiderCore.getConfig()).enableNightVision && player.getActivePotionEffect(Potion.nightVision) == null)
						player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 12000, 0, true));
					if(player.getActivePotionEffect(Potion.weakness) != null) player.removePotionEffect(Potion.weakness.id);
					continue;
				}
				if(player.worldObj.canBlockSeeTheSky((int)player.posX, (int)player.posY, (int)player.posZ)){
					if(player.getActivePotionEffect(Potion.nightVision) != null) player.removePotionEffect(Potion.nightVision.id);
					if(player.getActivePotionEffect(Potion.weakness) == null) player.addPotionEffect(new PotionEffect(Potion.weakness.id, 12000, 0, true));
				}
			}
		}
		if(timeUntilSpawnerRun <= 0){
			timeUntilSpawnerRun = 1200;
			for(WorldServer worldServer: (MinecraftServer.getServer()).worldServers) Spawner.runSpawner((World)worldServer);
		}
		timeUntilSpawnerRun--;
	}
	
}
