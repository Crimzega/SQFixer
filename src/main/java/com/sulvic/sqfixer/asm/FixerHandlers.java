package com.sulvic.sqfixer.asm;

import static com.sulvic.sqfixer.SpiderQueenFixer.*;

import java.lang.reflect.*;
import java.util.*;

import com.google.common.collect.*;
import com.sulvic.sqfixer.SpiderQueenFixer;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.*;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import radixcore.data.WatchedInt;
import sq.blocks.BlockWebFull;
import sq.core.*;
import sq.core.minecraft.*;
import sq.core.radix.PlayerData;
import sq.entity.IRep;
import sq.entity.ai.*;
import sq.entity.creature.*;
import sq.entity.friendly.EntityFriendlyCreeper;
import sq.entity.friendly.EntityFriendlyMandragora;
import sq.entity.friendly.EntityFriendlySkeleton;
import sq.entity.friendly.EntityFriendlyZombie;
import sq.entity.friendly.IFriendlyEntity;
import sq.enums.EnumOfferingType;
import sq.items.ItemOffering;

public class FixerHandlers{

	private static Set<Block> mainLogs = Sets.newHashSet(Blocks.log, Blocks.log2), extraLogs = Sets.newHashSet();

	private static void execRepChangeHandler(String name, EntityPlayer player, EntityLivingBase livingBase, int oldRep, int newRep){
		try{
			Class<ReputationHandler> handlerClass = ReputationHandler.class;
			Method method = handlerClass.getDeclaredMethod(name, new Class<?>[]{EntityPlayer.class, EntityLivingBase.class, int.class, int.class});
			method.setAccessible(true);
			method.invoke(handlerClass, player, livingBase, oldRep, newRep);
		}
		catch(NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex){
			getLogger().error("There was an issue trying to invoke \"{}\" in ReputationHandler", name);
			getLogger().catching(ex);
		}
	}

	public static ItemStack getEntityPickedResult(EntityLiving living){
		if(living instanceof EntityAnt) return new ItemStack(ModItems.eggAntBlack);
		else if(living instanceof EntityBee) return new ItemStack(living instanceof IFriendlyEntity? ModItems.eggFriendlyBee: ModItems.eggBee);
		else if(living instanceof EntityBeetle) return new ItemStack(ModItems.eggBeetle);
		else if(living instanceof EntityCocoon){
			Item item = null;
			switch(((EntityCocoon)living).getCocoonType()){
				case COW:
					item = ModItems.cocoonCow;
				break;
				case CREEPER:
					item = ModItems.cocoonCreeper;
				break;
				case GATHERER_BEE:
					item = ModItems.cocoonGathererBee;
				break;
				case HUMAN:
					item = ModItems.cocoonHuman;
				break;
				case PIG:
					item = ModItems.cocoonPig;
				break;
				case QUEEN_BEE:
					item = ModItems.cocoonQueenBee;
				break;
				case SHEEP:
					item = ModItems.cocoonSheep;
				break;
				case SKELETON:
					item = ModItems.cocoonSkeleton;
				break;
				case WARRIOR_BEE:
					item = ModItems.cocoonWarriorBee;
				break;
				case WASP:
					item = ModItems.cocoonWasp;
				break;
				case WOLF:
					item = ModItems.cocoonWolf;
				break;
				case ZOMBIE:
					item = ModItems.cocoonZombie;
				break;
				case ENDERMAN:
					item = ModItems.cocoonEnderman;
				break;
				case CHICKEN:
					item = ModItems.cocoonChicken;
				break;
				case VILLAGER:
					item = ModItems.cocoonVillager;
				break;
				case HORSE:
					item = ModItems.cocoonHorse;
				break;
				case RED_ANT:
					item = ModItems.cocoonRedAnt;
				break;
				default:
					item = ModItems.cocoonBlackAnt;
				break;
			}
			return new ItemStack(item);
		}
		else if(living instanceof EntityFly) return new ItemStack(ModItems.eggFly);
		else if(living instanceof EntityFriendlyCreeper) return new ItemStack(ModItems.eggFriendlyCreeper);
		else if(living instanceof EntityFriendlySkeleton) return new ItemStack(ModItems.eggFriendlySkeleton);
		else if(living instanceof EntityFriendlyZombie) return new ItemStack(ModItems.eggFriendlyZombie);
		else if(living instanceof EntityHuman) return new ItemStack(ModItems.eggHuman);
		else if(living instanceof EntityJack) return new ItemStack(ModItems.eggJack);
		else if(living instanceof EntityOctopus) return new ItemStack(ModItems.eggOctopus);
		else if(living instanceof EntityMandragora) return new ItemStack(living instanceof IFriendlyEntity? ModItems.eggFriendlyMandragora: ModItems.eggMandragora);
		else if(living instanceof EntitySpiderQueen) return new ItemStack(ModItems.eggSpiderQueen);
		else if(living instanceof EntitySpiderEgg) return new ItemStack(living instanceof EntityGhastEgg? ModItems.ghastEgg: ModItems.spiderEgg);
		else if(living instanceof EntityWasp) return new ItemStack(ModItems.eggWasp);
		else if(living instanceof EntityYuki) return new ItemStack(ModItems.eggYuki);
		else return null;
	}

	public static List<EntityLiving> getTypeEntitiesWithinDistance(Class<? extends EntityLiving> livingClass, Entity entity, double dist){
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(entity.posX - dist, entity.posY - dist, entity.posZ - dist, entity.posX + dist, entity.posY + dist, entity.posZ + dist);
		List<EntityLiving> result = Lists.newArrayList();
		for(Object obj: entity.worldObj.getEntitiesWithinAABB(livingClass, bounds)) result.add((EntityLiving)obj);
		return result;
	}

	public static void applyNewLogs(){
		if(!extraLogs.isEmpty()) extraLogs.clear();
		for(String log: getConfig().getMoreBedLogs()){
			String[] split = log.split(":");
			Block block = GameRegistry.findBlock(split[0], split[1]);
			if(block != null) extraLogs.add(block);
		}
	}

	public static void checkForBed(BlockWebFull webBlock, World world, int x, int y, int z, int itr){
		switch(webBlock.getWebType()){
			case NORMAL:
				if(world.getBlock(x, y, z) != ModBlocks.webFull) return;
				int fillerBlocks = 0, outlineBlocks = 0;
				for(int i = -1; i <= 1; i++) for(int j = -1; j <= 1; j++) if(world.getBlock(x + i, y, z + j) == ModBlocks.webFull) fillerBlocks++;
				for(int i = -2; i <= 2; i++) for(int j = -2; j <= 2; j++){
					Block block = world.getBlock(x + i, y, z + j);
					if((mainLogs.contains(block) || extraLogs.contains(block)) && (i == -2 || i == 2 || j == -2 || j == 2)) outlineBlocks++;
				}
				if(fillerBlocks == 9 && outlineBlocks == 16) for(int i = -1; i <= 1; i++) for(int j = -1; j <= 1; j++) world.setBlock(x + i, y, z + j, ModBlocks.webBed);
				else if(itr < 3) for(int i = -1; i <= 1; i++) for(int j = -1; j <= 1; j++) checkForBed(webBlock, world, x + i, y, z + j, itr + 1);
			break;
			default:
			break;
		}
	}

	public static void onReputationChange(EntityPlayer player, EntityLivingBase livingBase, int amount){
		PlayerData playerData = SpiderCore.getPlayerData(player);
		RepEntityExtension repExtension = (RepEntityExtension)livingBase.getExtendedProperties("SpiderQueenRepEntityExtension");
		WatchedInt likeData = null;
		if(repExtension != null) likeData = ReputationContainer.getLikeDataByClass(livingBase.getClass(), playerData);
		else if(livingBase instanceof IRep) ((IRep)livingBase).getLikeData(playerData);
		if(likeData != null){
			int oldRep = likeData.getInt(), newRep = oldRep + 1;
			likeData.setValue(newRep);
			if(amount > 0) execRepChangeHandler("handlePositiveRepChange", player, livingBase, oldRep, newRep);
			else if(amount < 0) execRepChangeHandler("handleNegativeRepChange", player, livingBase, oldRep, newRep);
		}
	}

	public static void updateMandragoraTick(World world, int x, int y, int z, Random random){
		if(world.getBlockMetadata(x, y, z) >= 7){
			world.setBlockToAir(x, y, z);
			EntityPlayer player = world.getClosestPlayer((double)x + 0.5d, (double)y, (double)z + 0.5d, getConfig().getMaxCropFriendlyDistance());
			EntityMandragora mandragora = player != null? new EntityFriendlyMandragora(world, player): new EntityMandragora(world);
			mandragora.setPosition((double)x + 0.5d, (double)y, (double)z + 0.5d);
			world.spawnEntityInWorld(mandragora);
		}
	}

	public static class OfferingPatch{

		private int offerCooldown;

		public boolean onEntityItemUpdate(EntityItem entityItem, EnumOfferingType type){
			Class<? extends EntityCreature> creatureClass = null;
			World world = entityItem.worldObj;
			ItemStack stack = entityItem.getEntityItem();
			Item item = stack.getItem();
			if(item instanceof ItemOffering){
				switch(type){
					case BRAIN:
						creatureClass = EntityZombie.class;
					break;
					case HEART:
						creatureClass = EntityCreeper.class;
					break;
					case SKULL:
						creatureClass = EntitySkeleton.class;
					break;
				}
				List<EntityLiving> entities = getTypeEntitiesWithinDistance(creatureClass, entityItem, 8d);
				for(EntityLiving living: entities) living.getNavigator().tryMoveToXYZ(entityItem.posX, entityItem.posY, entityItem.posZ, 0.8d);
				if(entities.size() > 0 && entityItem.age >= 100 && stack.hasTagCompound()){
					EntityLiving living = entities.get(0);
					if(!world.isRemote){
						if(offerCooldown > 0) offerCooldown--;
						else{
							if(stack.stackSize > 0) stack.stackSize--;
							else entityItem.setDead();
							EntityPlayer player = world.getPlayerEntityByName(stack.getTagCompound().getString("player"));
							if(player != null){
								player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN + "The" + living.getCommandSenderName() + "s have accepted your offering."));
								ReputationHandler.onReputationChange(player, living, 1);
							}
							offerCooldown = SpiderQueenFixer.getConfig().getPickupDelay();
						}
					}
				}
				else if(!stack.hasTagCompound()){
					EntityPlayer player = world.getClosestPlayerToEntity(entityItem, 5d);
					if(player != null){
						NBTTagCompound nbtCompound = new NBTTagCompound();
						nbtCompound.setString("player", player.getCommandSenderName());
						stack.setTagCompound(nbtCompound);
					}
				}
				return false;
			}
			else return false;
		}

	}

}
