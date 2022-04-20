package com.sulvic.sqfixer.client;

import java.io.File;

import com.sulvic.sqfixer.SpiderQueenFixer;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ConfigSQF extends Configuration{
	
	public static final String SQF_BASE_CATEGORY = "sqfixer";
	
	public ConfigSQF(FMLPreInitializationEvent evt){ super(new File(evt.getModConfigurationDirectory(), "sq_fixup.cfg")); }
	
	public String[] additionalBedLogs(){
		Property property = get(SQF_BASE_CATEGORY, "moreBedLogs", new String[]{});
		property.setLanguageKey("sqfixer.config.moreBedLogs");
		property.comment = "Allows additional log blocks for web beds (e.g.: modid:block)" + " [default: " + property.getDefault() + "]";
		return property.getStringList();
	}
	
	public boolean addCreatorToPlayerList(){ return getBoolean("addFixerCreator", SQF_BASE_CATEGORY, false, "Adds Crimzega to the list of player names", "sqfixer.config.addFixerCreator"); }
	
	public int getPickupDelay(){ return getInt("pickupDelay", SQF_BASE_CATEGORY + ".items", 1, 1, 20, "The amount of seconds before entities can pickup items again.", "sqfixer.config.pickupDelay") * 20; }
	
	public void build(){
		try{
			load();
			getPickupDelay();
			addCreatorToPlayerList();
			additionalBedLogs();
		}
		catch(Exception ex){
			SpiderQueenFixer.getLogger().catching(ex);
		}
		finally{
			if(hasChanged()) save();
		}
	}
	
}
