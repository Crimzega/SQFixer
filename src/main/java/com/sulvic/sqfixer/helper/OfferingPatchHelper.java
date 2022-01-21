package com.sulvic.sqfixer.helper;

import java.util.List;

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
import sq.enums.EnumOfferingType;
import sq.items.ItemOffering;

public class OfferingPatchHelper{
	
	private static final int OFFER_MAX_COOLDOWN = 20;
	private int offerCooldown;
	
	public OfferingPatchHelper(){}
	
	public boolean onEntityItemUpdate(EntityItem entityItem, EnumOfferingType type){
		Class<? extends EntityMob> mobClass = null;
		World world = entityItem.worldObj;
		ItemStack stack = entityItem.getEntityItem();
		Item item = stack.getItem();
		if(item instanceof ItemOffering){
			switch(type){
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
				if(!world.isRemote){
					if(offerCooldown > 0) offerCooldown--;
					else{
						if(stack.stackSize > 0) stack.stackSize--;
						else entityItem.setDead();
						EntityPlayer player = world.getPlayerEntityByName(stack.getTagCompound().getString("player"));
						if(player != null){
							player.addChatComponentMessage(new ChatComponentText("\u00A7AThe " + entity.getCommandSenderName() + "s have accepted your offering."));
							ReputationHandler.onReputationChange(player, (EntityLiving)entity, 1);
						}
						offerCooldown = OFFER_MAX_COOLDOWN;
					}
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
		else return false;
	}
	
}
