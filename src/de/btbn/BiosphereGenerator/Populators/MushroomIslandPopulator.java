package de.btbn.BiosphereGenerator.Populators;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
//import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;

public class MushroomIslandPopulator extends BlockPopulator
{

	@Override
	public void populate(World world, Random random, Chunk source)
	{
		if (!source.getBlock(7, 0, 7).getBiome().equals(Biome.MUSHROOM_ISLAND))
			return;

		int treeNum = random.nextInt(5);
		int mooNum = random.nextInt(4)+1;
		ArrayList<Location> locs = Helper.getBlockLocations(source, Material.MYCEL);
		ArrayList<Location> mooLocs = new ArrayList<Location>(locs);

		while (treeNum > 0 && locs.size() > 0)
		{
			int p = random.nextInt(locs.size());
			Location loc = locs.get(p).clone();
			loc.add(0, 1, 0);
			locs.remove(p);

			TreeType tt = TreeType.RED_MUSHROOM;
			if (random.nextBoolean())
				tt = TreeType.BROWN_MUSHROOM;

			if (world.generateTree(loc, tt))
				treeNum -= 1;
		}

		while (mooNum > 0 && mooLocs.size() > 0)
		{
			int p = random.nextInt(mooLocs.size());
			Location loc = mooLocs.get(p).clone();
			loc.add(0, 1, 0);
			mooLocs.remove(p);
			
			//world.spawnCreature(loc, EntityType.MUSHROOM_COW);
			mooNum -= 1;
		}
	}

}
