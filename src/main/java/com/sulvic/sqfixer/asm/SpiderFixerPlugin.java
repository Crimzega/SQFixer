package com.sulvic.sqfixer.asm;

import java.util.Map;

import org.apache.logging.log4j.*;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.*;

@DependsOn({"forge", "SQ"})
@MCVersion("1.7.10")
@SortingIndex(1001)
@TransformerExclusions({"com.sulvic.sqfixer.asm"})
public class SpiderFixerPlugin implements IFMLLoadingPlugin{

	protected static boolean isDeobfuscated;
	protected static Logger logger = LogManager.getLogger("SQFixer ASM");

	public String getAccessTransformerClass(){ return DataTransformHandler.class.getName(); }

	public String[] getASMTransformerClass(){ return null; }

	public String getModContainerClass(){ return null; }

	public String getSetupClass(){ return null; }

	public void injectData(Map<String, Object> data){
		isDeobfuscated = (Boolean)data.get("runtimeDeobfuscationEnabled");
		logger.info("Classes are deobfuscated: {}", isDeobfuscated);
	}

}
