package com.sulvic.sqfixer.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import radixcore.data.WatchedInt;
import sq.core.ReputationHandler;
import sq.core.SpiderCore;
import sq.core.radix.PlayerData;
import sq.entity.IRep;
import sq.entity.ai.RepEntityExtension;
import sq.entity.ai.ReputationContainer;

public class SulvicReputationHandler{
	
	private static void execRepChangeHandler(String name, Object... values){
		try{
			Class<ReputationHandler> handlerClass = ReputationHandler.class;
			Class<?>[] classes = new Class<?>[values.length];
			Method method = handlerClass.getDeclaredMethod(name, new Class<?>[]{EntityPlayer.class, EntityLivingBase.class, int.class, int.class});
			if(!method.isAccessible()) method.setAccessible(true);
			method.invoke(handlerClass, values);
		}
		catch(NoSuchMethodException ex){
			ex.printStackTrace();
		}
		catch(IllegalAccessException ex){
			ex.printStackTrace();
		}
		catch(IllegalArgumentException ex){
			ex.printStackTrace();
		}
		catch(InvocationTargetException ex){
			ex.printStackTrace();
		}
	}
	
	public static void onReputationChange(EntityPlayer player, EntityLivingBase livingBase, int changeAmount){
		PlayerData playerData = SpiderCore.getPlayerData(player);
		RepEntityExtension repExtension = (RepEntityExtension)livingBase.getExtendedProperties("SpiderQueenRepEntityExtension");
		WatchedInt likeData = null;
		if(repExtension != null) likeData = ReputationContainer.getLikeDataByClass(livingBase.getClass(), playerData);
		else if(livingBase instanceof IRep) ((IRep)livingBase).getLikeData(playerData);
		if(likeData != null){
			int oldRep = likeData.getInt();
			likeData.setValue(likeData.getInt() + changeAmount);
			int newRep = likeData.getInt();
			if(changeAmount > 0) execRepChangeHandler("handlePositiveRepChange", player, livingBase, oldRep, newRep);
			else if(changeAmount < 0) execRepChangeHandler("handleNegativeRepChange", player, livingBase, oldRep, newRep);
		}
	}
	
}
