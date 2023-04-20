package com.sulvic.sqfixer.client.gui;

import java.util.List;

import com.google.common.collect.Lists;
import com.sulvic.sqfixer.ReferenceSQF;
import com.sulvic.sqfixer.SpiderQueenFixer;
import com.sulvic.sqfixer.client.ConfigSQF;
import com.sulvic.sqfixer.client.PlayerInfoStorage;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;

@SuppressWarnings({"rawtypes", "unchecked"})
public class GuiConfigSQF extends GuiConfig{

	public GuiConfigSQF(GuiScreen parent){ super(parent, theConfigElements(), ReferenceSQF.MODID, false, false, I18n.format("Spider Queen Fixer [Config]")); }

	private static List<IConfigElement> theConfigElements(){
		List<IConfigElement> cfgElems = Lists.newArrayList();
		cfgElems.add(new DummyConfigElement.DummyCategoryElement("sqf_items", "sqfixer.config.category.items", ItemsEntry.class));
		cfgElems.add(new DummyConfigElement.DummyCategoryElement("sqf_entities", "sqfixer.config.category.world", EntitiesEntry.class));
		List<IConfigElement> elems = new ConfigElement(theConfig().getCategory(ConfigSQF.SQF_BASE_CATEGORY)).getChildElements();
		for(IConfigElement elem: elems) if(elem.isProperty()) cfgElems.add(elem);
		return cfgElems;
	}

	private static ConfigSQF theConfig(){ return SpiderQueenFixer.getConfig(); }

	protected void actionPerformed(GuiButton button){
		if(button.id == 2000 && theConfig().hasChanged()){
			theConfig().save();
			PlayerInfoStorage.applyConfigData();
		}
		super.actionPerformed(button);
	}

	public static class EntitiesEntry extends GuiConfigEntries.CategoryEntry{

		public EntitiesEntry(GuiConfig parent, GuiConfigEntries entries, IConfigElement cfgElem){ super(parent, entries, cfgElem); }

		protected GuiScreen buildChildScreen(){
			return new GuiConfig(owningScreen, new ConfigElement(theConfig().getCategory(ConfigSQF.SQF_BASE_CATEGORY + ".world")).getChildElements(), ReferenceSQF.MODID, false, false,
				I18n.format("Entity Config Properties"));
			// return super.buildChildScreen();
		}

	}

	public static class ItemsEntry extends GuiConfigEntries.CategoryEntry{

		public ItemsEntry(GuiConfig parent, GuiConfigEntries entries, IConfigElement cfgElem){ super(parent, entries, cfgElem); }

		protected GuiScreen buildChildScreen(){
			return new GuiConfig(owningScreen, new ConfigElement(theConfig().getCategory(ConfigSQF.SQF_BASE_CATEGORY + ".items")).getChildElements(), ReferenceSQF.MODID, false, false,
				I18n.format("Item Config Properties"));
		}

	}

}
