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
@SortingIndex(1002)
@TransformerExclusions("com.sulvic.sqfixer.asm.*")
public class FixerLoadingPlugin implements IFMLLoadingPlugin{
	
	protected static boolean isDeobfuscated;
	protected static Logger logger = LogManager.getLogger("sqfixer-asm");
	
	public String getAccessTransformerClass(){ return DataHandlerTransformer.class.getName(); }
	
	public String[] getASMTransformerClass(){ return null; }
	
	public String getModContainerClass(){ return null; }
	
	public String getSetupClass(){ return null; }
	
	public void injectData(Map<String, Object> data){ isDeobfuscated = (Boolean)data.get("runtimeDeobfuscationEnabled"); }
	
}
