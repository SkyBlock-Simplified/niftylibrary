package net.netcoding.niftybukkit.libraries;

import java.util.UUID;

import net.netcoding.niftybukkit.NiftyBukkit;

import org.bukkit.World;
import org.bukkit.entity.Entity;

public class EntityFinder {
	public static boolean isEntityFound(Entity entity) {
		return isEntityFound(entity.getUniqueId());
	}
	
	public static boolean isEntityFound(World world, Entity entity) {
		return isEntityFound(world, entity.getUniqueId());
	}
	
	public static boolean isEntityFound(UUID entity) {
		for (World w: NiftyBukkit.getPlugin().getServer().getWorlds()) {
			if (isEntityFound(w, entity))
				return true;
		}
		return false;
	}
	
	public static boolean isEntityFound(World world, UUID entity) {
		for (Entity e: world.getEntities()) {
			if (e.getUniqueId().equals(entity))
				return true;
		}
		return false;
	}
}