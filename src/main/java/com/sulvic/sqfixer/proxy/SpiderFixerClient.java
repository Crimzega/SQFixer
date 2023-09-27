package com.sulvic.sqfixer.proxy;

import com.sulvic.sqfixer.SpiderQueenFixer;
import com.sulvic.sqfixer.client.render.*;

import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.entity.player.EntityPlayer;
import sq.core.SpiderCore;
import sq.entity.creature.*;

public class SpiderFixerClient extends SpiderFixerServer{

	public void registerRenders(){
		RenderingRegistry.registerEntityRenderingHandler(EntityAnt.class, new RenderRetexedAnt());
		RenderingRegistry.registerEntityRenderingHandler(EntityCocoon.class, new RenderTypedCocoon());
		RenderingRegistry.registerEntityRenderingHandler(EntityHuman.class, new RenderHumanFix());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpiderQueen.class, new RenderRetexedSpiderQueen());
		if(SpiderCore.getConfig().useSpiderQueenModel){
			SpiderQueenFixer.getLogger().info("Attempting to apply custom Spider Queen player renderer.");
			RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new RenderRetexedPlayerQueen());
		}
	}

}
