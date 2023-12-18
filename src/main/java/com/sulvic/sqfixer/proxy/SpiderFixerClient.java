package com.sulvic.sqfixer.proxy;

import com.sulvic.sqfixer.SpiderQueenFixer;
import com.sulvic.sqfixer.asm.FixerAssemModContainer;
import com.sulvic.sqfixer.asm.SpiderFixerPlugin;
import com.sulvic.sqfixer.client.render.*;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import sq.core.SpiderCore;
import sq.entity.creature.*;

public class SpiderFixerClient extends SpiderFixerServer{

	public void registerRenders(){
		if(SpiderQueenFixer.getConfig().useFixerAntModel()) RenderingRegistry.registerEntityRenderingHandler(EntityAnt.class, new RenderRetexedAnt());
		if(SpiderQueenFixer.getConfig().useFixerCocoonModel()) RenderingRegistry.registerEntityRenderingHandler(EntityCocoon.class, new RenderTypedCocoon());
		if(SpiderQueenFixer.getConfig().useFixerHumanModel()) RenderingRegistry.registerEntityRenderingHandler(EntityHuman.class, new RenderHumanFix());
		if(SpiderQueenFixer.getConfig().useFixerQueenModel()) RenderingRegistry.registerEntityRenderingHandler(EntitySpiderQueen.class, new RenderRetexedSpiderQueen());
		FixerAssemModContainer container = SpiderFixerPlugin.instance.getContainer();
		if(SpiderCore.getConfig().useSpiderQueenModel && container.useFixerPlayerModel()){
			SpiderQueenFixer.getLogger().info("Attempting to apply custom Spider Queen player renderer.");
			RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new RenderRetexedPlayerQueen());
		}
	}

}
