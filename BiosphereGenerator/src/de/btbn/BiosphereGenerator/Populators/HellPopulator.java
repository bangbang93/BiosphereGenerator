package de.btbn.BiosphereGenerator.Populators;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class HellPopulator extends BlockPopulator
{

	@Override
	public void populate(World world, Random random, Chunk source)
	{
		if (!source.getBlock(7, 0, 7).getBiome().equals(Biome.HELL))
			return;

		int fireNum = random.nextInt(5) + 2;
		ArrayList<Location> locs = Helper.getBlockLocations(source, Material.NETHERRACK);

		while (fireNum > 0 && locs.size() > 0)
		{
			int p = random.nextInt(locs.size());
			Location loc = locs.get(p);
			loc.add(0, 1, 0);
			locs.remove(p);

			loc.getBlock().setType(Material.FIRE);
			fireNum -= 1;
		}
	}

}
