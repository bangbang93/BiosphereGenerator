package de.btbn.BiosphereGenerator.Populators;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class TreePopulator extends BlockPopulator
{
	public void populate(World world, Random random, Chunk source)
	{
		Biome bio = source.getBlock(7, 0, 7).getBiome();
		if(!bio.equals(Biome.PLAINS) && !bio.equals(Biome.FOREST) && !bio.equals(Biome.JUNGLE) && !bio.equals(Biome.MUSHROOM_ISLAND))
			return;
		
		Material groundType = Material.GRASS;
		TreeType tt = TreeType.TREE;
		int treeNum = random.nextInt(4);
		if(bio.equals(Biome.FOREST))
			treeNum += 3;
		else if(bio.equals(Biome.JUNGLE))
		{
			treeNum += 8;
			tt = TreeType.JUNGLE;
		}
		else if(bio.equals(Biome.DESERT))
		{
			//tt = TreeType. TODO: Kaktus
		}
		else if(bio.equals(Biome.MUSHROOM_ISLAND))
		{
			tt = TreeType.RED_MUSHROOM;
			groundType = Material.MYCEL;
		}
		
		if(treeNum != 0)
		{
			ArrayList<Location> grassLocs = new ArrayList<Location>();
			
			for(int x = 0; x < 16; x++)
				for(int y = 0; y < world.getMaxHeight(); y++)
					for(int z = 0; z < 16; z++)
					{
						if(source.getBlock(x, y, z).getType().equals(groundType))
							grassLocs.add(source.getBlock(x, y, z).getLocation());
					}
			
			while(treeNum > 0 && grassLocs.size() > 0)
			{
				int p = random.nextInt(grassLocs.size());
				
				Location loc = grassLocs.get(p);
				loc.add(0, 1, 0);
				
				if(tt.equals(TreeType.JUNGLE) && random.nextInt(10) == 0)
					tt = TreeType.JUNGLE_BUSH;
				if(tt.equals(TreeType.RED_MUSHROOM) && random.nextInt(2) == 0)
					tt = TreeType.BROWN_MUSHROOM;
				
				if(world.generateTree(loc, tt))
					treeNum -= 1;
				
				grassLocs.remove(p);
			}
		}
	}
}
