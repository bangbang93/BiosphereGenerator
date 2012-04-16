package de.btbn.BiosphereGenerator.Populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;

public class PlainsPopulator extends BlockPopulator
{

	@Override
	public void populate(World world, Random random, Chunk source)
	{
		if(!source.getBlock(7, 0, 7).getBiome().equals(Biome.PLAINS))
			return;
	}

}
