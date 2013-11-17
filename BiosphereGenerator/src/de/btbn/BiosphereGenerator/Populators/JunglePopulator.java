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

public class JunglePopulator extends BlockPopulator
{

	@Override
	public void populate(World world, Random random, Chunk source)
	{
		if (!source.getBlock(7, 0, 7).getBiome().equals(Biome.JUNGLE))
			return;

		int treeNum = random.nextInt(4) + 8;
		ArrayList<Location> locs = Helper.getBlockLocations(source, Material.GRASS);

		while (treeNum > 0 && locs.size() > 0)
		{
			int p = random.nextInt(locs.size());
			Location loc = locs.get(p);
			loc.add(0, 1, 0);
			locs.remove(p);

			TreeType tt = TreeType.JUNGLE;
			if (random.nextInt(10) == 0)
				tt = TreeType.JUNGLE_BUSH;

			if (world.generateTree(loc, tt))
				treeNum -= 1;
		}
	}
}
