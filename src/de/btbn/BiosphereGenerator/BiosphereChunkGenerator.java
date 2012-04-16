package de.btbn.BiosphereGenerator;

import java.util.List;
import java.util.Random;
import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import de.btbn.BiosphereGenerator.Populators.*;

public class BiosphereChunkGenerator extends ChunkGenerator
{
	public static int mod(int a, int b)
	{
		return ((a % b) + b) % b;
	}

	private static int maxHeight = -1;

	private PerlinOctaveGenerator octGen = null;
	private double frequency = 1.0 / 4.0;
	private double amplitude = 10.0;

	private int sphereDistance = 100;
	private int sphereRadius = 32;
	private int sphereY = 80;

	private Sphere glassDome;
	private Sphere groundSphere;

	public BiosphereChunkGenerator()
	{
		glassDome = new Sphere(sphereDistance / 2, sphereY, sphereDistance / 2, sphereRadius);
		glassDome.material = Material.GLASS;
		glassDome.filled = false;

		groundSphere = new Sphere(sphereDistance / 2, sphereY, sphereDistance / 2, sphereRadius);
		groundSphere.material = Material.GRASS;
		groundSphere.filled = true;
	}

	private int getSurfaceLevel(int x, int z)
	{
		double noise = ((octGen.noise(x, z, frequency, amplitude, true) + 1.0) / 2.0) * amplitude;
		return (int) (noise + sphereY);
	}

	private static void setBlock(short[][] result, int x, int y, int z, Material blkid)
	{
		if (x < 0 || y < 0 || z < 0 || x > 15 || z > 15 || y >= maxHeight)
			return;
		if (result[y >> 4] == null)
			result[y >> 4] = new short[4096];
		result[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (short) blkid.getId();
	}

	@SuppressWarnings("unused")
	private static Material getBlock(short[][] result, int x, int y, int z)
	{
		if (x < 0 || y < 0 || z < 0 || x > 15 || z > 15 || y >= maxHeight)
			return null;
		if (result[y >> 4] == null)
			return null;
		return Material.getMaterial(result[y >> 4][((y & 0xF) << 8) | (z << 4) | x]);
	}

	private Biome calcBiome(World w, int x, int z)
	{
		Random r = new Random(w.getSeed() + (x / sphereDistance) * 7 - (z / sphereDistance) * 2);
		switch (r.nextInt(11))
		{
		case 0:
			return Biome.HELL;
		case 1:
			return Biome.JUNGLE;
		case 2:
			return Biome.FOREST;
		case 3:
			return Biome.SWAMPLAND;
		case 4:
			return Biome.DESERT;
		case 5:
			return Biome.TAIGA;
		case 6:
			return Biome.PLAINS;
		case 7:
			return Biome.ICE_PLAINS;
		case 8:
			return Biome.BEACH;
		case 9:
			return Biome.MUSHROOM_ISLAND;
		case 10:
			return Biome.OCEAN;
		}
		return Biome.PLAINS;
	}

	private Material getBiomeBlock(Biome bio, boolean top)
	{
		if (top)
		{
			switch (bio)
			{
			case HELL:
				return Material.NETHERRACK;
			case JUNGLE:
				return Material.GRASS;
			case FOREST:
				return Material.GRASS;
			case SWAMPLAND:
				return Material.GRASS;
			case DESERT:
				return Material.SAND;
			case TAIGA:
				return Material.SNOW_BLOCK;
			case PLAINS:
				return Material.GRASS;
			case ICE_PLAINS:
				return Material.ICE;
			case BEACH:
				return Material.SAND;
			case MUSHROOM_ISLAND:
				return Material.MYCEL;
			case OCEAN:
				return Material.WATER;
			}
		} else
		{
			switch (bio)
			{
			case HELL:
				return Material.NETHERRACK;
			case JUNGLE:
				return Material.DIRT;
			case FOREST:
				return Material.DIRT;
			case SWAMPLAND:
				return Material.DIRT;
			case DESERT:
				return Material.SANDSTONE;
			case TAIGA:
				return Material.DIRT;
			case PLAINS:
				return Material.DIRT;
			case ICE_PLAINS:
				return Material.DIRT;
			case BEACH:
				return Material.SANDSTONE;
			case MUSHROOM_ISLAND:
				return Material.DIRT;
			case OCEAN:
				return Material.WATER;
			}
		}

		return Material.DIRT;
	}

	@Override
	public short[][] generateExtBlockSections(World world, Random random, int x, int z, BiomeGrid biomes)
	{
		if (maxHeight < 0)
			maxHeight = world.getMaxHeight();
		if (octGen == null)
			octGen = new PerlinOctaveGenerator(world, 4);

		int wx = x << 4;
		int wz = z << 4;
		int chunkHeight = world.getMaxHeight() / 16;
		short[][] result = new short[chunkHeight][];

		for (int lx = 0; lx < 16; lx++)
			for (int lz = 0; lz < 16; lz++)
			{
				Biome bio = calcBiome(world, wx + lx, wz + lz);
				boolean nosphere = true;
				boolean nobio = true;
				boolean firstHit = true;
				int groundLevel = getSurfaceLevel(wx + lx, wz + lz);
				int tx = mod((wx + lx), sphereDistance);
				int tz = mod((wz + lz), sphereDistance);

				for (int ly = maxHeight - 1; ly >= 0; ly--)
				{

					if (ly <= groundLevel && groundSphere.inSphere(tx, ly, tz))
					{
						setBlock(result, lx, ly, lz, getBiomeBlock(bio, firstHit));
						firstHit = false;
						nobio = false;
						if (!bio.equals(Biome.OCEAN))
						{
							nosphere = false;
							continue;
						}
					}

					if (glassDome.inSphere(tx, ly, tz))
					{
						if ((tx > sphereDistance / 2 - 2 && tx < sphereDistance / 2 + 2) || (tz > sphereDistance / 2 - 2 && tz < sphereDistance / 2 + 2))
							if (ly > groundLevel && ly <= groundLevel + 3)
								continue;
						setBlock(result, lx, ly, lz, Material.GLASS);
						continue;
					}
				}

				if (nosphere)
				{
					if ((tx > sphereDistance / 2 - 3 && tx < sphereDistance / 2 + 3) || (tz > sphereDistance / 2 - 3 && tz < sphereDistance / 2 + 3))
						setBlock(result, lx, groundLevel, lz, Material.WOOD);
				}
				if (nobio)
				{
					biomes.setBiome(lx, lz, Biome.SKY);
					if (tx == sphereDistance / 2 - 2 || tx == sphereDistance / 2 + 2 || tz == sphereDistance / 2 - 2 || tz == sphereDistance / 2 + 2)
						setBlock(result, lx, groundLevel + 1, lz, Material.FENCE);
				} else
					biomes.setBiome(lx, lz, bio);
			}

		return result;
	}

	@Override
	public Location getFixedSpawnLocation(World world, Random random)
	{
		int x = (random.nextInt(21) - 10) * sphereDistance + sphereDistance / 2;
		int z = (random.nextInt(21) - 10) * sphereDistance + sphereDistance / 2;
		Location res = new Location(world, x, world.getHighestBlockYAt(x, z), z);
		return res;
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(World world)
	{
		LinkedList<BlockPopulator> res = new LinkedList<BlockPopulator>();
		res.add(new JunglePopulator());
		res.add(new PlainsPopulator());
		res.add(new ForestPopulator());
		res.add(new MushroomIslandPopulator());
		res.add(new DesertPopulator());
		return res;
	}
}
