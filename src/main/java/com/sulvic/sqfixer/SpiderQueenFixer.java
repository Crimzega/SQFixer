package com.sulvic.sqfixer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Sets;
import com.sulvic.sqfixer.client.ConfigSQF;
import com.sulvic.sqfixer.client.PlayerInfoStorage;
import com.sulvic.sqfixer.handler.ServerTickHandler;
import com.sulvic.sqfixer.proxy.ServerSQF;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import radixcore.util.RadixExcept;
import sq.core.SpiderCore;
import sq.core.minecraft.ModBlocks;

@Mod(modid = ReferenceSQF.MODID, name = ReferenceSQF.NAME, version = ReferenceSQF.VERSION, dependencies = ReferenceSQF.DEPENDENCIES, guiFactory = ReferenceSQF.GUI_FACTORY)
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
		proxy.registerRenders();
		config = new ConfigSQF(evt);
		config.build();
		SpiderCore.fakePlayerNames = downloadFakePlayerNames();
		PlayerInfoStorage.init();
		PlayerInfoStorage.populate();
		PlayerInfoStorage.applyConfigData();
		logger.info("This should apply missing names from redirects. Name List: {}", Arrays.toString(SpiderCore.fakePlayerNames.toArray()));
		FMLCommonHandler.instance().bus().register(new ServerTickHandler());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt){
		logger.info("This init method exists to set missing unlocalized names and apply mod blocks.");
		ModBlocks.webBed.setBlockName("web-bed");
		applyBlocks();
	}
	
	private HttpURLConnection findFinalPath(String urlStr, String cookies) throws IOException{
		URL url = new URL(urlStr);
		logger.info("Attempting to use link \"{}\": ", urlStr);
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		if(cookies != null && !cookies.equals("")) conn.setRequestProperty("Cookie", cookies);
		boolean redirects = false;
		int status = conn.getResponseCode();
		if(status != HttpURLConnection.HTTP_OK) redirects = status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_SEE_OTHER;
		if(redirects){
			logger.info("Redirecting...");
			String newUrl = conn.getHeaderField("Location"), cookies1 = conn.getHeaderField("Set-Cookie");
			conn = findFinalPath(newUrl, cookies1);
		}
		return conn;
	}
	
	private List<String> downloadFakePlayerNames(){
		logger.info("Downloading contributor/volunteered player names with redirecting...");
		List<String> returnList = new ArrayList<String>();
		try{
			readSkinsFromURL(SpiderCore.PERM_SKINS_URL, returnList);
			readSkinsFromURL(SpiderCore.SKINS_URL, returnList);
			logger.info("Contributor/volunteer player names downloaded successfully!");
		}
		catch(Throwable ex){
			RadixExcept.logErrorCatch(ex, "Unable to download player names.");
		}
		return returnList;
	}
	
	private void readSkinsFromURL(String stringUrl, List<String> returnList) throws IOException{
		HttpURLConnection conn = findFinalPath(stringUrl, null);
		if(conn != null){
			Scanner scanner = new Scanner(conn.getInputStream());
			while(scanner.hasNext()) returnList.add(scanner.next());
			scanner.close();
		}
		logger.info("");
	}
	
}
