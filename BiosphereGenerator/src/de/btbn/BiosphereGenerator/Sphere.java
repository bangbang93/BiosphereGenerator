package de.btbn.BiosphereGenerator;

import org.bukkit.Material;

class Sphere
{
	public enum SphereType
	{
		UPPER, LOWER, FULL
	}

	public Sphere()
	{
		this(0, 64, 0, 16);
	}

	public Sphere(int x, int y, int z, int r)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.r = r;
	}

	public boolean inSphere(int px, int py, int pz)
	{
		px = px - x;
		py = py - y;
		pz = pz - z;

		boolean res = false;
		if (filled)
		{
			if (px * px + py * py + pz * pz < r * r)
				res = true;
		} else
		{
			int tmp = px * px + py * py + pz * pz;
			if (tmp < r * r && tmp >= (r - 1) * (r - 1))
				res = true;
		}

		if (res)
		{
			switch (sphereType)
			{
			case LOWER:
				if (py > 0)
					res = false;
				break;
			case UPPER:
				if (py <= 0)
					res = false;
				break;
			}
		}

		return res;
	}

	public Material material = Material.STONE;
	public boolean filled = true;
	public SphereType sphereType = SphereType.FULL;

	public int x;
	public int y;
	public int z;

	public int r;
}
