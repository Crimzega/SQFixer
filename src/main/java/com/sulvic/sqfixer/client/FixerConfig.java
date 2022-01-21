package com.sulvic.sqfixer.client;

import java.io.File;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.config.Configuration;

public class FixerConfig extends Configuration{
	
	
	public FixerConfig(FMLPreInitializationEvent evt){
		super(new File(evt.getModConfigurationDirectory(), "sq_fixup.cfg"));
	}
	
}
