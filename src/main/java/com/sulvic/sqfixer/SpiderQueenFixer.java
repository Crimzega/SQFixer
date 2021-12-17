package com.sulvic.sqfixer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import radixcore.util.RadixExcept;
import sq.core.SpiderCore;
import sq.core.minecraft.ModBlocks;

@Mod(modid = ReferenceSQF.MODID, name = ReferenceSQF.NAME, version = ReferenceSQF.VERSION, dependencies = ReferenceSQF.DEPENDENCIES)
public class SpiderQueenFixer{
	
	@Instance(ReferenceSQF.MODID)
	public static SpiderQueenFixer instance;
	private Logger logger;
	
	public SpiderQueenFixer(){
		logger = LogManager.getLogger(ReferenceSQF.MODID);
		logger.info("This logger is created to ensure that the mod works as intended");
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent evt){
		SpiderCore.fakePlayerNames = downloadFakePlayerNames();
		logger.info("This should apply missing names from redirects. Name List: {}", Arrays.toString(SpiderCore.fakePlayerNames.toArray()));
	}
	
	@EventHandler
	public void init(FMLInitializationEvent evt){
		logger.info("This init method exists to only set missing unlocalized names.");
		ModBlocks.webBed.setUnlocalizedName("web-bed");
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
			readSkinsFromURL("http://pastebin.com/raw.php?i=MNWrUxwa", returnList);
			readSkinsFromURL("http://pastebin.com/raw.php?i=L5S632xR", returnList);
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
	}
	
}
