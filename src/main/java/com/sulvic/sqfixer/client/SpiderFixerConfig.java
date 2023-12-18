package com.sulvic.sqfixer.client;

import static com.sulvic.sqfixer.SpiderQueenFixer.*;

import java.io.File;
import java.util.*;

import com.google.common.collect.*;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.config.Configuration;
import sq.entity.creature.*;

public class SpiderFixerConfig extends Configuration{

	public static final String FIXER_CATEGORY_BASE = "sqfixer";
	private String[] moreBedLogs;
	private boolean addFixerCreator, allowCreativeString, useFixerAntModel, useFixerCocoonModel, useFixerHumanModel, useFixerQueenModel;
	private double maxLabelDistance, maxCropFriendlyDist;
	private int pickupDelay;

	public SpiderFixerConfig(FMLPreInitializationEvent evt){ super(new File(evt.getModConfigurationDirectory(), "sq_fixup.cfg")); }

	public String[] getMoreBedLogs(){ return moreBedLogs; }

	public boolean addFixerCreator(){ return addFixerCreator; }

	public boolean allowCreativeString(){ return allowCreativeString; }

	public boolean useFixerAntModel(){ return useFixerAntModel; }

	public boolean useFixerCocoonModel(){ return useFixerCocoonModel; }

	public boolean useFixerHumanModel(){ return useFixerHumanModel; }

	public boolean useFixerQueenModel(){ return useFixerQueenModel; }

	public int getPickupDelay(){ return pickupDelay; }

	public double getMaxCropFriendlyDistance(){ return maxCropFriendlyDist; }

	public double getMaxLabelDistance(){ return maxLabelDistance; }

	public void build(){
		try{
			load();
			useFixerAntModel = getBoolean("useFixerAntModel", FIXER_CATEGORY_BASE + ".render", false, "Switches the Ant models for the fixer model.", "sqfixer.config.useFixerAntModel");
			useFixerCocoonModel = getBoolean("useFixerCocoonModel", FIXER_CATEGORY_BASE + ".render", false, "Switches the Cocoon models for the fixer model.", "sqfixer.config.useFixerCocoonModel");
			useFixerHumanModel = getBoolean("useFixerHumanModel", FIXER_CATEGORY_BASE + ".render", false, "Switches the Human models for the fixer model.",
				"sqfixer.config.useFixerHumanModel");
			useFixerQueenModel = getBoolean("useFixerQueenModel", FIXER_CATEGORY_BASE + ".render", false, "Switches the SpiderQueen models for the fixer model.", "sqfixer.config.useFixerQueenModel");
			pickupDelay = getInt("pickupDelay", FIXER_CATEGORY_BASE + ".items", 1, 1, 20, "The cooldown time for item offerings (in seconds).", "sqfixer.config.pickupDelay") * 20;
			addFixerCreator = getBoolean("addFixerCreator", FIXER_CATEGORY_BASE, false, "Adds Crimzega to the list of player names.", "sqfixer.config.addFixerCreator");
			moreBedLogs = getStringList("moreBedLogs", FIXER_CATEGORY_BASE, new String[]{}, "Allows more log blocks for web beds (e.g: modid:block)", (String[])null,
				"sqfixer.config.moreBedLogs");
			getLogger().info("Block collection attempt: {}", Arrays.toString(moreBedLogs));
			maxLabelDistance = (double)getFloat("humanLabelMaxDistance", FIXER_CATEGORY_BASE + ".render", 32f, 8f, 64f, "Sets the max distance for human labels to render",
				"sqfixer.config.maxLabelDist");
			allowCreativeString = getBoolean("allowCreativeString", FIXER_CATEGORY_BASE + ".items", true, "Allows the creation of string in Creative Mode",
				"sqfixer.config.allowCreativeString");
			maxCropFriendlyDist = (double)getFloat("maxCropFriendlyDist", FIXER_CATEGORY_BASE + ".world", 16f, 8f, 48f, "Sets the player's max distance for growing friendly mandragoras.",
				"sqfixer.config.maxCropFriendlyDist");
			new SpawnConfig(EntityBeetle.class).build(this, 70, 1, 3);
			new SpawnConfig(EntityFly.class).build(this, 75, 1, 3);
			new SpawnConfig(EntityMandragora.class).build(this, 50, 1, 3);
			new SpawnConfig(EntityOctopus.class).build(this, 70, 1, 3);
			new SpawnConfig(EntityWasp.class).build(this, 45, 1, 3);
			new SpawnConfig(EntityHuman.class).build(this, 30, 1, 3);
			new SpawnConfig(EntitySpiderQueen.class).build(this, 15, 1, 2);
			new SpawnConfig(EntityYuki.class).build(this, 10, 1, 3);
		}
		catch(Exception ex){
			getLogger().error("An unknown error occured creating the config file.");
			getLogger().catching(ex);
		}
		finally{
			if(hasChanged()) save();
		}
	}

	public void sync(){ build(); }

	public static class SpawnConfig{

		private static final Map<Class<? extends EntityLiving>, SpawnConfig> CONFIG_SPAWNS = Maps.newHashMap();
		private final String entityName;
		private int maxCount, minCount, weightProb;

		public SpawnConfig(Class<? extends EntityLiving> livingClass){
			entityName = livingClass.getSimpleName().substring(6).toLowerCase();
			CONFIG_SPAWNS.put(livingClass, this);
		}

		public SpawnConfig build(SpiderFixerConfig cfg, int weight, int min, int max){
			String catName = FIXER_CATEGORY_BASE + ".world";
			weightProb = cfg.getInt(entityName + "WeightedProb", catName, weight, 10, 80, "Sets the weighted probability.", "cfg.config." + entityName + "Weight");
			minCount = cfg.getInt(entityName + "MinCount", catName, min, 1, 5, "Sets the minimum spawnable entities.", "cfg.config." + entityName + "Min");
			maxCount = cfg.getInt(entityName + "MaxCount", catName, max, minCount, 10, "Sets the maximum spawnable entities. Never goes lower than the min count.",
				"cfg.config." + entityName + "Max");
			return this;
		}

		public int getMaxCount(){ return maxCount; }

		public int getMinCount(){ return minCount; }

		public int getProbability(){ return weightProb; }

	}

}
