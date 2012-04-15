package de.btbn.BiosphereGenerator;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin
{
	@Override
	public void onEnable()
	{
		System.out.println("BioGen Loaded");
	}
	
	@Override
	public void onDisable()
	{
		System.out.println("BioGen Unloaded");
	}
	
	@Override
    public ChunkGenerator getDefaultWorldGenerator(String worldName, String id) {
        return new BiosphereChunkGenerator();
    }
}
