package com.sulvic.sqfixer.helper;

import com.sulvic.sqfixer.SpiderQueenFixer;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import radixcore.util.BlockHelper;
import sq.blocks.BlockWebFull;
import sq.core.minecraft.ModBlocks;
import sq.enums.EnumWebType;

public class BedCheckHelper{
	
	private BlockWebFull webFull;
	
	public BedCheckHelper(BlockWebFull web){ webFull = web; }
	
	public void checkForBed(World world, int x, int y, int z, int itr){
		if(webFull.getWebType() == EnumWebType.NORMAL){
			if(BlockHelper.getBlock(world, x, y, z) != ModBlocks.webFull) return;
			SpiderQueenFixer.getLogger().info("Attempting to check [Count: " + itr + "]");
			int fillerBlocks = 0, outlineBlocks = 0;
			for(int i = -1; i <= 1; i++) for(int j = -1; j <= 1; j++) if(BlockHelper.getBlock(world, x + i, y, z + j) == ModBlocks.webFull) fillerBlocks++;
			for(Block block: SpiderQueenFixer.bedLogs) for(int i = -2; i <= 2; i++) for(int j = -2; j <= 2; j++)
				if(BlockHelper.getBlock(world, x + i, y, z + j) == block && (i == -2 || i == 2 || j == -2 || j == 2)) outlineBlocks++;
			SpiderQueenFixer.getLogger().info("Filler Blocks: {}, Outline Blocks: {}", fillerBlocks, outlineBlocks);
			if(fillerBlocks == 9 && outlineBlocks == 16) for(int i = -1; i <= 1; i++) for(int j = -1; j <= 1; j++) BlockHelper.setBlock(world, x + i, y, z + j, ModBlocks.webBed);
			else if(itr < 3) for(int i = -1; i <= 1; i++) for(int j = -1; j <= 1; j++) checkForBed(world, x + i, y, z + j, itr + 1);
		}
	}
	
}
