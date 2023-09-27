package com.sulvic.sqfixer.common.item;

import static com.sulvic.sqfixer.SpiderFixerReference.*;

import com.sulvic.sqfixer.common.SpiderQueenData;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import sq.core.SpiderCore;

public class ItemSkinSwitcher extends Item{

	public ItemSkinSwitcher(){
		setCreativeTab(SpiderCore.getCreativeTab());
		setMaxStackSize(1);
		setTextureName(MODID + ":skin_switcher");
		setUnlocalizedName("skin_switcher");
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player){
		if(!world.isRemote){
			SpiderQueenData data = SpiderQueenData.getPlayerData(player);
			if(data != null){
				data.changeSkin(player);
				player.addChatMessage(new ChatComponentText("Skin changed to ").appendText(data.getCurrentSkin().getTextureName() + '.'));
			}
		}
		return stack;
	}

}
