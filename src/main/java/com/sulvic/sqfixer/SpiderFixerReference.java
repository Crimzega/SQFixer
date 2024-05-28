package com.sulvic.sqfixer;

public class SpiderFixerReference{

	private static final String PROXY = "com.sulvic.sqfixer.proxy.";
	public static final String CLIENT = PROXY + "SpiderFixerClient", DEPENDENCIES = "required-after:SQ@[1-7-10-1.1.0,)", GUI_FACTORY = "com.sulvic.sqfixer.client.gui.FixerGuiFactory",
		MODID = "sqfixer", NAME = "Spider Queen Fixer", SERVER = PROXY + "SpiderFixerServer", VERSION = "1.3.4";

}
