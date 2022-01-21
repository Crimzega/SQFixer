package com.sulvic.sqfixer.asm;

public class ObfuscatorCheck{
	
	protected static final String entityItemCls(){ return "L" + (FixerLoadingPlugin.isObfuscated? "xk": "net/minecraft/entity/item/EntityItem") + ";"; }
	
}
