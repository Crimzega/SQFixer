package com.sulvic.sqfixer.client;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.sulvic.sqfixer.SpiderQueenFixer;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.config.Configuration;
import sq.entity.creature.*;

public class ConfigSQF extends Configuration{

	public static final String SQF_BASE_CATEGORY = "sqfixer";
	private Map<Class<? extends EntityLiving>, EntitySpawnHandler> entitySpawnHandler = new HashMap<>();
	private String[] additionalBedLogs;
	private boolean addFixerCreator, allowCreativeString;
	private double humanLabelDistance;
	private int pickupDelay;

	public ConfigSQF(FMLPreInitializationEvent evt){ super(new File(evt.getModConfigurationDirectory(), "sq_fixup.cfg")); }

	public String[] additionalBedLogs(){ return additionalBedLogs; }

	public boolean addCreatorToPlayerList(){ return addFixerCreator; }

	public boolean toggleCreativeString(){ return allowCreativeString; }

	public double renderHumanLabelDistance(){ return humanLabelDistance; }

	public int getMaxCount(Class<? extends EntityLiving> livingClass){ return entitySpawnHandler.get(livingClass).getMaxCount(); }

	public int getMinCount(Class<? extends EntityLiving> livingClass){ return entitySpawnHandler.get(livingClass).getMinCount(); }

	public int getWeightProbability(Class<? extends EntityLiving> livingClass){ return entitySpawnHandler.get(livingClass).getWeightProbability(); }

	public int getPickupDelay(){ return pickupDelay; }

	public void build(){
		try{
			load();
			pickupDelay = getInt("pickupDelay", SQF_BASE_CATEGORY + ".items", 1, 1, 20, "The amount of seconds before entities can pickup items again.", "sqfixer.config.pickupDelay") * 20;
			addFixerCreator = getBoolean("addFixerCreator", SQF_BASE_CATEGORY, false, "Adds Crimzega to the list of player names", "sqfixer.config.addFixerCreator");
			additionalBedLogs = getStringList("moreBedLogs", SQF_BASE_CATEGORY, new String[]{}, "Allows additional loc blocks for web beds (e.g.: modid:block) [default: []]",
				(String[])null, "sqfixer.config.moreBedLogs");
			humanLabelDistance = (double)getFloat("humanLabelMaxDistance", SQF_BASE_CATEGORY + ".render", 32f, 8f, 64f, "Sets the maximum distance for human labels to render",
				"sqfixer.config.humanLabelMaxDistance");
			allowCreativeString = getBoolean("allowCreativeString", SQF_BASE_CATEGORY + ".items", false, "Allows the creation of string in Creative Mode");
			Class<? extends EntityLiving> entityClass = EntityBeetle.class;
			entitySpawnHandler.put(entityClass, new EntitySpawnHandler(entityClass).build(this, 70, 1, 3));
			entityClass = EntityFly.class;
			entitySpawnHandler.put(entityClass, new EntitySpawnHandler(entityClass).build(this, 75, 1, 3));
			entityClass = EntityMandragora.class;
			entitySpawnHandler.put(entityClass, new EntitySpawnHandler(entityClass).build(this, 50, 1, 3));
			entityClass = EntityOctopus.class;
			entitySpawnHandler.put(entityClass, new EntitySpawnHandler(entityClass).build(this, 70, 1, 3));
			entityClass = EntityWasp.class;
			entitySpawnHandler.put(entityClass, new EntitySpawnHandler(entityClass).build(this, 45, 1, 3));
			entityClass = EntityHuman.class;
			entitySpawnHandler.put(entityClass, new EntitySpawnHandler(entityClass).build(this, 30, 1, 3));
			entityClass = EntitySpiderQueen.class;
			entitySpawnHandler.put(entityClass, new EntitySpawnHandler(entityClass).build(this, 15, 1, 2));
			entityClass = EntityYuki.class;
			entitySpawnHandler.put(entityClass, new EntitySpawnHandler(entityClass).build(this, 10, 1, 3));
		}
		catch(Exception ex){
			SpiderQueenFixer.getLogger().catching(ex);
		}
		finally{
			if(hasChanged()) save();
		}
	}

	public void sync(){ build(); }

	private static class EntitySpawnHandler{

		private final String entityName;
		private int weightProbability;
		private int maxCount, minCount;

		private EntitySpawnHandler(Class<? extends EntityLiving> livingClass){ entityName = livingClass.getSimpleName().substring(6).toLowerCase(); }

		private EntitySpawnHandler build(ConfigSQF cfg, int weightProb, int min, int max){
			String categName = SQF_BASE_CATEGORY + ".world";
			weightProbability = cfg.getInt(entityName + "WeightedProb", categName, weightProb, 20, 80, "Sets the weighted probability");
			minCount = cfg.getInt(entityName + "MinCount", categName, min, 1, 3, "Sets the minimum amount of spawnable entities");
			maxCount = cfg.getInt(entityName + "MaxCount", categName, max, 3, 8, "Sets the minimum amount of spawnable entities");
			return this;
		}

		public int getMinCount(){ return minCount; }

		public int getMaxCount(){ return maxCount; }

		public int getWeightProbability(){ return weightProbability; }

	}

}
