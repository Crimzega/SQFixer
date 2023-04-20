package com.sulvic.sqfixer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;
import com.sulvic.sqfixer.client.ConfigSQF;
import com.sulvic.sqfixer.client.PlayerInfoStorage;
import com.sulvic.sqfixer.handler.FixerHandler;
import com.sulvic.sqfixer.helper.PlayerNamesHelper;
import com.sulvic.sqfixer.proxy.ServerSQF;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import sq.core.SpiderCore;
import sq.core.minecraft.ModBlocks;
import sq.core.minecraft.Spawner;
import sq.util.SpawnEntry;

@Mod(modid = ReferenceSQF.MODID, name = ReferenceSQF.NAME, version = ReferenceSQF.VERSION, dependencies = ReferenceSQF.DEPENDENCIES, guiFactory = ReferenceSQF.GUI_FACTORY)
@SuppressWarnings({"unchecked"})
public class SpiderQueenFixer{

	@Instance(ReferenceSQF.MODID)
	public static SpiderQueenFixer instance;
	@SidedProxy(clientSide = ReferenceSQF.CLIENT, serverSide = ReferenceSQF.SERVER)
	public static ServerSQF proxy;
	private Logger logger;
	private ConfigSQF config;
	public static Set<Block> bedLogs = Sets.newHashSet(Blocks.log, Blocks.log2);

	public SpiderQueenFixer(){
		logger = LogManager.getLogger("SQFixer");
		logger.info("This logger is created to ensure that the mod works as intended.");
	}

	private static void applyBlocks(){
		for(String additionalLog: getConfig().additionalBedLogs()){
			String[] split = additionalLog.split(":");
			Block block = GameRegistry.findBlock(split[0], split[1]);
			getLogger().info("Possibly found block: " + block);
			if(block != null) bedLogs.add(block);
		}
		getLogger().info("Web bed block count: " + bedLogs.size());
	}

	public static Logger getLogger(){ return instance.logger; }

	public static ConfigSQF getConfig(){ return instance.config; }

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt){
		config = new ConfigSQF(evt);
		config.build();
		proxy.registerRenders();
		SpiderCore.fakePlayerNames = PlayerNamesHelper.downloadFakePlayerNames();
		logger.info(SpiderCore.fakePlayerNames.size());
		PlayerInfoStorage.init();
		PlayerInfoStorage.populate();
		PlayerInfoStorage.applyConfigData();
		logger.info("This should apply missing names from redirects. Name List: {}", Arrays.toString(SpiderCore.fakePlayerNames.toArray()));
		FMLCommonHandler.instance().bus().register(FixerHandler.instance);
		MinecraftForge.EVENT_BUS.register(FixerHandler.instance);
	}

	@EventHandler
	public void init(FMLInitializationEvent evt){
		logger.info("This init method exists to set missing unlocalized names, apply mod blocks, and Use modifiable spawn data.");
		try{
			logger.info("Ensuring use of Minecraft spawn system.");
			if(SpiderCore.getConfig().useSpawnSystem) logger.info("SpiderQueen is attempting to use it's own spawn system");
			Field field = Spawner.class.getDeclaredField("spawnEntries");
			field.setAccessible(true);
			List<SpawnEntry> entryList = (List<SpawnEntry>)field.get(Spawner.class);
			for(SpawnEntry entry: entryList){
				Class<? extends EntityLiving> livingClass = (Class<? extends EntityLiving>)entry.getSpawnClass();
				EntityRegistry.addSpawn(livingClass, config.getWeightProbability(livingClass), config.getMinCount(livingClass), config.getMaxCount(livingClass), entry.getCreatureType(),
					entry.getSpawnBiomes().toArray(new BiomeGenBase[0]));
			}
		}
		catch(NoSuchFieldException | IllegalAccessException | SecurityException ex){
			logger.catching(ex);
		}
		ModBlocks.webBed.setBlockName("web-bed");
		applyBlocks();
	}

	@EventHandler
	public void serverStartup(FMLServerAboutToStartEvent evt){
		GameData.getItemRegistry().forEach(value -> {
			Item item = (Item)value;
			if(item instanceof ItemFood) ((ItemFood)item).setAlwaysEdible();
		});
	}

}
