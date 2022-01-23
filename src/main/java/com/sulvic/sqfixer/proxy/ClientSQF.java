package com.sulvic.sqfixer.proxy;

import com.sulvic.sqfixer.client.RenderHumanFix;

import cpw.mods.fml.client.registry.RenderingRegistry;
import sq.entity.creature.EntityHuman;

public class ClientSQF extends ServerSQF{
	
	public void registerRenders(){
		RenderingRegistry.registerEntityRenderingHandler(EntityHuman.class, new RenderHumanFix());
	}
	
}
