package com.sulvic.sqfixer.item;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import radixcore.util.RadixLogic;
import sq.core.ReputationHandler;
import sq.core.SpiderCore;
import sq.enums.EnumOfferingType;
import sq.items.ItemOffering;

public class ItemOffering2 extends Item{
	
	private static final int OFFER_MAX_COOLDOWN = 20;
	private EnumOfferingType offeringType;
	private int offerCooldown;
	
	public ItemOffering2(EnumOfferingType type){
		offeringType = type;
		String name = type.getName();
		setCreativeTab(SpiderCore.getCreativeTab());
		setTextureName("sq:" + name);
		setUnlocalizedName(name);
		GameRegistry.registerItem(this, name);
	}
	
	public EnumOfferingType getOfferingType(){ return offeringType; }
	
	public boolean onEntityItemUpdate(EntityItem entityItem){
		Class<? extends EntityMob> mobClass = null;
		World world = entityItem.worldObj;
		ItemStack stack = entityItem.getEntityItem();
		Item item = stack.getItem();
		if(item instanceof ItemOffering){
			switch(offeringType){
				case BRAIN:
					mobClass = EntityZombie.class;
				break;
				case HEART:
					mobClass = EntityCreeper.class;
				break;
				case SKULL:
					mobClass = EntitySkeleton.class;
				break;
			}
			List<Entity> entityList = RadixLogic.getAllEntitiesOfTypeWithinDistance(mobClass, entityItem, 8);
			for(Entity entity: entityList) ((EntityLiving)entity).getNavigator().tryMoveToXYZ(entityItem.posX, entityItem.posY, entityItem.posZ, 0.8d);
			if(entityList.size() > 0 && entityItem.age >= 100 && stack.hasTagCompound()){
				Entity entity = entityList.get(0);
				if(offerCooldown > 0) offerCooldown--;
				else{
					if(stack.stackSize > 0) stack.stackSize--;
					else entityItem.setDead();
					offerCooldown = OFFER_MAX_COOLDOWN;
				}
				EntityPlayer player = world.getPlayerEntityByName(stack.getTagCompound().getString("player"));
				if(player != null && !world.isRemote){
					player.addChatComponentMessage(new ChatComponentText("\u00A7AThe " + entity.getCommandSenderName() + "s have accepted your offering."));
					ReputationHandler.onReputationChange(player, (EntityLiving)entity, 1);
				}
			}
			else if(!stack.hasTagCompound()){
				EntityPlayer player = world.getClosestPlayerToEntity(entityItem, 5d);
				if(player != null){
					stack.setTagCompound(new NBTTagCompound());
					stack.getTagCompound().setString("player", player.getCommandSenderName());
				}
			}
			return false;
		}
		else return super.onEntityItemUpdate(entityItem);
	}
	
}
