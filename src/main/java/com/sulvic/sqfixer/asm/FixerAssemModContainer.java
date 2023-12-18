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
	protected boolean useFixerPlayerModel;

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
			useFixerPlayerModel = config.getBoolean("useFixerPlayerModel", "sqfixer.render", false, "Switches the Player SpiderQueen models for the fixer model.");
		}
		catch(Exception ex){}
		finally{
			if(config.hasChanged()) config.save();
		}
		SpiderFixerPlugin.instance.container = this;
	}

	public boolean useFixerPlayerModel(){ return useFixerPlayerModel; }

	private static File getConfigFolder(){ return new File(Minecraft.getMinecraft().mcDataDir, "config"); }

}
