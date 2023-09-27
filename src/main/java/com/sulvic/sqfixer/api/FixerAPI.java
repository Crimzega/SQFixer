package com.sulvic.sqfixer.api;

import net.minecraft.entity.EntityLiving;
import net.minecraftforge.common.util.EnumHelper;
import sq.enums.EnumCocoonSize;
import sq.enums.EnumCocoonType;
import sq.enums.EnumSpiderType;

public class FixerAPI{

	public static EnumCocoonType newCocoonType(String name, int chance, EnumCocoonSize size, EnumSpiderType spiderType, Class<? extends EntityLiving> livingClass){
		return EnumHelper.addEnum(EnumCocoonType.class, name, EnumCocoonType.values().length, chance, size, spiderType, livingClass);
	}

}
