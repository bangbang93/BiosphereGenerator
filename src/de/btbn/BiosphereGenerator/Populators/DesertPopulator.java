package de.btbn.BiosphereGenerator.Populators;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class DesertPopulator extends BlockPopulator
{
	@Override
	public void populate(World world, Random random, Chunk source)
	{
		if (!source.getBlock(7, 0, 7).getBiome().equals(Biome.DESERT))
			return;

		int treeNum = random.nextInt(3);
		ArrayList<Location> locs = Helper.getBlockLocations(source, Material.SAND);

		while (treeNum > 0 && locs.size() > 0)
		{
			int p = random.nextInt(locs.size());
			Location loc = locs.get(p);
			loc.add(0, 1, 0);
			locs.remove(p);

			if (world.getBlockAt(loc).getType().equals(Material.AIR))
			{
				treeNum -= 1;
				int h = random.nextInt(3) + 1;
				for (int i = 0; i < h; i++)
				{
					world.getBlockAt(loc).setType(Material.CACTUS);
					loc.add(0, 1, 0);
				}
			}
		}
	}
}
