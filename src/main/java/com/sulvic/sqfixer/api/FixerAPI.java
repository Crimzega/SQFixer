package com.sulvic.sqfixer.api;

import com.sulvic.sqfixer.client.model.ModelCocoonNew;
import com.sulvic.sqfixer.client.render.RenderCocoonNew;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.util.EnumHelper;
import sq.enums.EnumCocoonSize;
import sq.enums.EnumCocoonType;
import sq.enums.EnumSpiderType;
import sq.items.ItemCocoon;

public class FixerAPI{

	public static ItemCocoon addCocoonedEntity(String name, int catchChance, EnumCocoonSize size, EnumSpiderType type, Class<? extends Entity> entityClass, ModelCocoonNew cocoonModel){
		EnumCocoonType newType = EnumHelper.addEnum(EnumCocoonType.class, name, EnumCocoonType.values().length, catchChance, size, type, entityClass);
		RenderCocoonNew.addType(newType, cocoonModel);
		return new ItemCocoon(newType);
	}

}
