package com.sulvic.sqfixer.client.gui;

import java.util.Set;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiFactorySQF implements IModGuiFactory{
	
	public void initialize(Minecraft mc){}
	
	public Class<? extends GuiScreen> mainConfigGuiClass(){ return GuiConfigSQF.class; }
	
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories(){ return null; }
	
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement elem){ return null; }
	
}
