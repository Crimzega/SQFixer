package com.sulvic.sqfixer;

import static com.sulvic.sqfixer.SpiderFixerReference.*;

import java.util.Arrays;

import org.apache.logging.log4j.*;

import com.sulvic.sqfixer.asm.FixerHandlers;
import com.sulvic.sqfixer.client.SpiderFixerConfig;
import com.sulvic.sqfixer.common.HumanInfo;
import com.sulvic.sqfixer.common.item.ItemSkinSwitcher;
import com.sulvic.sqfixer.proxy.SpiderFixerServer;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.registry.*;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;
import sq.core.SpiderCore;
import sq.core.minecraft.*;

@Mod(modid = MODID, name = NAME, version = VERSION, dependencies = DEPENDENCIES, guiFactory = GUI_FACTORY)
public class SpiderQueenFixer{

	public ItemSkinSwitcher skinSwitcher;
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
		skinSwitcher = new ItemSkinSwitcher();
		GameRegistry.registerItem(skinSwitcher, "skin_switcher");
		GameRegistry.addRecipe(new ShapedOreRecipe(skinSwitcher, new String[]{" I ", "IGI", " I "}, 'I', Items.iron_ingot, 'G', "paneGlass"));
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
