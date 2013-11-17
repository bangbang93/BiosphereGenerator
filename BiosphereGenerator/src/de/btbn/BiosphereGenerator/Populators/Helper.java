package de.btbn.BiosphereGenerator.Populators;

import java.util.ArrayList;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;

public class Helper
{
	public static ArrayList<Location> getBlockLocations(Chunk chunk, Material mat)
	{
		ArrayList<Location> grassLocs = new ArrayList<Location>();

		for (int x = 0; x < 16; x++)
			for (int y = 0; y < chunk.getWorld().getMaxHeight(); y++)
				for (int z = 0; z < 16; z++)
				{
					if (chunk.getBlock(x, y, z).getType().equals(mat) && chunk.getBlock(x, y + 1, z).getType().equals(Material.AIR))
						grassLocs.add(chunk.getBlock(x, y, z).getLocation());
				}

		return grassLocs;
	}
}
