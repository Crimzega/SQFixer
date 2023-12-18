package com.sulvic.sqfixer.asm;

import java.io.File;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

public class FixerAssemModContainer extends DummyModContainer{

	private static final ModMetadata FIXER_ASM_METADATA = new ModMetadata();
	private static Configuration config;
	protected boolean useFixerAntModel, useFixerCocoonModel, useFixerHumanModel, useFixerPlayerModel, useFixerQueenModel;

	static{
		FIXER_ASM_METADATA.modId = "sqfixer_asm";
		FIXER_ASM_METADATA.name = "SpiderQueen Fixer (ASM)";
		FIXER_ASM_METADATA.authorList = Lists.newArrayList("Crimzega");
		FIXER_ASM_METADATA.credits = "Forge for making it possible to update Minecraft & Mods.";
	}

	public FixerAssemModContainer(){
		super(FIXER_ASM_METADATA);
		config = new Configuration(new File(getConfigFolder(), "sq_fixup_asm.cfg"), true);
		try{
			config.load();
			useFixerAntModel = config.getBoolean("useFixerAntModel", "sqfixer.render", false, "Switches the Ant models for the fixer model.");
			useFixerCocoonModel = config.getBoolean("useFixerCocoonModel", "sqfixer.render", false, "Switches the Cocoon models for the fixer model.");
			useFixerHumanModel = config.getBoolean("useFixerHumanModel", "sqfixer.render", false, "Switches the Human models for the fixer model.");
			useFixerPlayerModel = config.getBoolean("useFixerPlayerModel", "sqfixer.render", false, "Switches the Player SpiderQueen models for the fixer model.");
			useFixerQueenModel = config.getBoolean("useFixerQueenModel", "sqfixer.render", false, "Switches the SpiderQueen models for the fixer model.");
		}
		catch(Exception ex){}
		finally{
			if(config.hasChanged()) config.save();
		}
		SpiderFixerPlugin.instance.container = this;
	}

	public boolean useFixerAntModel(){ return useFixerAntModel; }

	public boolean useFixerCocoonModel(){ return useFixerCocoonModel; }

	public boolean useFixerHumanModel(){ return useFixerHumanModel; }

	public boolean useFixerPlayerModel(){ return useFixerPlayerModel; }

	public boolean useFixerQueenModel(){ return useFixerQueenModel; }

	private static File getConfigFolder(){ return new File(Minecraft.getMinecraft().mcDataDir, "config"); }

}
