package com.sulvic.sqfixer.client.gui;

import static com.sulvic.sqfixer.SpiderQueenFixer.*;
import static com.sulvic.sqfixer.SpiderFixerReference.*;
import static com.sulvic.sqfixer.client.SpiderFixerConfig.*;

import java.util.*;

import com.google.common.collect.Lists;
import com.sulvic.sqfixer.asm.FixerHandlers;
import com.sulvic.sqfixer.common.HumanInfo;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.client.config.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;

@SuppressWarnings({"rawtypes", "unchecked"})
public class FixerGuiFactory implements IModGuiFactory{

	public void initialize(Minecraft minecraftInstance){}

	public Class<? extends GuiScreen> mainConfigGuiClass(){ return GuiFixerConfig.class; }

	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories(){ return null; }

	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element){ return null; }

	public static class GuiFixerConfig extends GuiConfig{

		public GuiFixerConfig(GuiScreen parent){ super(parent, initElements(), MODID, false, false, I18n.format("Spider Queen Fixer [Config]")); }

		private static List<IConfigElement> initElements(){
			List<IConfigElement> result = Lists.newArrayList();
			result.add(new DummyConfigElement.DummyCategoryElement("sqf_items", "sqfixer.config.category.items", ItemsEntry.class));
			result.add(new DummyConfigElement.DummyCategoryElement("sqf_render", "sqfixer.config.category.render", RenderEntry.class));
			result.add(new DummyConfigElement.DummyCategoryElement("sqf_world", "sqfixer.config.category.world", WorldEntry.class));
			for(IConfigElement elem: (List<IConfigElement>)new ConfigElement(getConfig().getCategory(FIXER_CATEGORY_BASE)).getChildElements()) if(elem.isProperty()) result.add(elem);
			return result;
		}

		protected void actionPerformed(GuiButton btn){
			super.actionPerformed(btn);
			if(btn.id == 2000 && getConfig().hasChanged()){
				getConfig().save();
				HumanInfo.applyConfig();
				FixerHandlers.applyNewLogs();
			}
		}

	}

	public static class ItemsEntry extends GuiConfigEntries.CategoryEntry{

		public ItemsEntry(GuiConfig parent, GuiConfigEntries entries, IConfigElement cfgElem){ super(parent, entries, cfgElem); }

		protected GuiScreen buildChildScreen(){
			return new GuiConfig(owningScreen, new ConfigElement(getConfig().getCategory(FIXER_CATEGORY_BASE + ".items")).getChildElements(), MODID, false, false,
				I18n.format("Item Config Properties"));
		}

	}

	public static class RenderEntry extends GuiConfigEntries.CategoryEntry{

		public RenderEntry(GuiConfig parent, GuiConfigEntries entries, IConfigElement cfgElem){ super(parent, entries, cfgElem); }

		protected GuiScreen buildChildScreen(){
			return new GuiConfig(owningScreen, new ConfigElement(getConfig().getCategory(FIXER_CATEGORY_BASE + ".render")).getChildElements(), MODID, false, false,
				I18n.format("Render Config Properties"));
		}

	}

	public static class WorldEntry extends GuiConfigEntries.CategoryEntry{

		public WorldEntry(GuiConfig parent, GuiConfigEntries entries, IConfigElement cfgElem){ super(parent, entries, cfgElem); }

		protected GuiScreen buildChildScreen(){
			return new GuiConfig(owningScreen, new ConfigElement(getConfig().getCategory(FIXER_CATEGORY_BASE + ".world")).getChildElements(), MODID, false, false,
				I18n.format("World Config Properties"));
		}

	}

}
