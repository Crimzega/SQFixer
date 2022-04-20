package com.sulvic.sqfixer.asm;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.DependsOn;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@MCVersion("1.7.10")
@DependsOn({"forge", "SQ"})
@SortingIndex(1001)
@TransformerExclusions("com.sulvic.sqfixer.asm.*")
public class FixerLoadingPlugin implements IFMLLoadingPlugin{
	
	protected static boolean isObfuscated;
	protected static Logger logger = LogManager.getLogger("SQFixer ASM");
	
	public String getAccessTransformerClass(){ return DataHandlerTransformer.class.getName(); }
	
	public String[] getASMTransformerClass(){ return null; }
	
	public String getModContainerClass(){ return null; }
	
	public String getSetupClass(){ return null; }
	
	public void injectData(Map<String, Object> data){
		isObfuscated = (Boolean)data.get("runtimeDeobfuscationEnabled");
		logger.info("Classes are obfuscated: " + isObfuscated);
	}
	
}
