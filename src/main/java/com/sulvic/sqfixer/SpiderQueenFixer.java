package com.sulvic.sqfixer;

import static com.sulvic.sqfixer.SpiderFixerReference.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.*;

import com.sulvic.sqfixer.asm.FixerHandlers;
import com.sulvic.sqfixer.asm.SpiderFixerPlugin;
import com.sulvic.sqfixer.client.SpiderFixerConfig;
import com.sulvic.sqfixer.common.HumanInfo;
import com.sulvic.sqfixer.common.item.ItemSkinSwitcher;
import com.sulvic.sqfixer.proxy.SpiderFixerServer;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import sq.core.SpiderCore;
import sq.core.minecraft.*;
import sq.util.SpawnEntry;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES, guiFactory = GUI_FACTORY)
public class SpiderQueenFixer{

	public ItemSkinSwitcher skinSwitcher = new ItemSkinSwitcher();
	@Instance(MODID)
	public static SpiderQueenFixer instance;
	private Logger logger;
	@SidedProxy(clientSide = CLIENT, serverSide = SERVER)
	public static SpiderFixerServer proxy;
	private SpiderFixerConfig config;

	public SpiderQueenFixer(){
		logger = LogManager.getLogger("SQFixer");
		logger.info("This logger is created to ensure that the mod works as intended.");
	}

	public static Logger getLogger(){ return instance.logger; }

	public static SpiderFixerConfig getConfig(){ return instance.config; }

	@EventHandler
	public void serverStarting(FMLServerStartingEvent evt){ FixerEvents.getSpiderqueensFolder(evt.getServer().getEntityWorld()); }

	@EventHandler
	public void preInit(FMLPreInitializationEvent evt){
		logger.info("This preInit method exists to make the config file, apply new renders, register events, and handle human info.");
		config = new SpiderFixerConfig(evt);
		config.build();
		proxy.registerRenders();
		SpiderCore.fakePlayerNames.addAll(HumanInfo.redownloadPlayerNames());
		logger.info("The player names should now be applied from redirects.");
		logger.info("Player Names: {}", Arrays.toString(SpiderCore.fakePlayerNames.toArray()));
		HumanInfo.storeIds();
		FMLCommonHandler.instance().bus().register(FixerEvents.getInstance());
		MinecraftForge.EVENT_BUS.register(FixerEvents.getInstance());
	}

	@EventHandler
	public void init(FMLInitializationEvent evt){
		logger.info("This init method exists to set missing unlocalized names, apply mod blocks, increase max stacks, and use modified spawn data.");
		try{
			logger.info("Ensureing use of Minecraft spawn system.");
			Field field = Spawner.class.getDeclaredField("spawnEntries");
			field.setAccessible(true);
			List<SpawnEntry> entryList = (List<SpawnEntry>)field.get(Spawner.class);
			boolean ownSpawnSystem = SpiderCore.getConfig().useSpawnSystem;
			if(ownSpawnSystem) logger.info("SpiderQueen is attempting to use it's own spawn system. Disabliing...");
			else logger.info("SpiderQueen is using Minecraft's spawn system. Updating with config options...");
			for(SpawnEntry entry: entryList){
				Class<? extends EntityLiving> livingClass = (Class<? extends EntityLiving>)entry.getSpawnClass();
				BiomeGenBase[] biomes = entry.getSpawnBiomes().toArray(new BiomeGenBase[0]);
				EnumCreatureType type = entry.getCreatureType();
				if(!ownSpawnSystem) EntityRegistry.removeSpawn(livingClass, type, biomes);
				EntityRegistry.addSpawn(livingClass, config.getProbability(livingClass), config.getMinCount(livingClass), config.getMaxCount(livingClass), type, biomes);
			}
			
		}
		catch(NoSuchFieldException | IllegalAccessException | SecurityException ex){
			logger.catching(ex);
		}
		if(SpiderFixerPlugin.instance.getContainer().useFixerPlayerModel()){
			GameRegistry.registerItem(skinSwitcher, "skin_switcher");
			GameRegistry.addRecipe(new ShapedOreRecipe(skinSwitcher, new String[]{" I ", "IGI", " I "}, 'I', Items.iron_ingot, 'G', "paneGlass"));
		}
		ModBlocks.webBed.setBlockName("web-bed");
		FixerHandlers.applyNewLogs();
		ModItems.royalBlood.setMaxStackSize(16);
		ModItems.spiderEgg.setMaxStackSize(16);
	}

	@EventHandler
	public void serverStartup(FMLServerAboutToStartEvent evt){
		GameData.getItemRegistry().forEach(value -> {
			Item item = (Item)value;
			if(item instanceof ItemFood) ((ItemFood)item).setAlwaysEdible();
		});
	}

}
