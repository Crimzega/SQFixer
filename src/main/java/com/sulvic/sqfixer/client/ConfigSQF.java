package com.sulvic.sqfixer.client;

import java.io.File;

import com.sulvic.sqfixer.SpiderQueenFixer;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class ConfigSQF extends Configuration{
	
	public static final String SQF_BASE_CATEGORY = "sqfixer";
	
	public ConfigSQF(FMLPreInitializationEvent evt){ super(new File(evt.getModConfigurationDirectory(), "sq_fixup.cfg")); }
	
	public boolean addCreatorToPlayerList(){ return getBoolean("addFixerCreator", SQF_BASE_CATEGORY, false, "Adds Crimzega to the list of player names", "sqfixer.config.addFixerCreator"); }
	
	public int getPickupDelay(){ return getInt("pickupDelay", SQF_BASE_CATEGORY + ".items", 1, 1, 20, "The amount of seconds before entities can pickup items again.", "sqfixer.config.pickupDelay") * 20; }
	
	public void build(){
		try{
			load();
			getPickupDelay();
		}
		catch(Exception ex){
			SpiderQueenFixer.getLogger().catching(ex);
		}
		finally{
			if(hasChanged()) save();
		}
	}
	
}
