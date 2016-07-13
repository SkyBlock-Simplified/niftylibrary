package net.netcoding.nifty.craftbukkit.minecraft.block;

import net.netcoding.nifty.common.minecraft.block.state.*;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.craftbukkit.minecraft.block.state.*;

import java.util.Arrays;

public enum CraftBlockType {

	BANNER(CraftBanner.class, "org.bukkit.block.Banner"),
	BEACON(CraftBeacon.class, "org.bukkit.block.Beacon"),
	BREWING_STAND(CraftBrewingStand.class, "org.bukkit.block.BrewingStand"),
	CHEST(CraftChest.class, "org.bukkit.block.Chest"),
	COMMAND_BLOCK(CraftCommandBlock.class, "org.bukkit.block.CommandBlock"),
	CREATURE_SPAWNER(CraftCreatureSpawner.class, "org.bukkit.block.CreatureSpawner"),
	DISPENSER(CraftDispenser.class, "org.bukkit.block.Dispenser"),
	DROPPER(CraftDropper.class, "org.bukkit.block.Dropper"),
	END_GATEWAY(CraftEndGateway.class, "org.bukkit.block.EndGateway"),
	FLOWER_POT(CraftFlowerPot.class, "org.bukkit.block.FlowerPot"),
	FURNACE(CraftFurnace.class, "org.bukkit.block.Furnace"),
	HOPPER(CraftHopper.class, "org.bukkit.block.Hopper"),
	JUKEBOX(CraftJukebox.class, "org.bukkit.block.Jukebox"),
	NOTE_BLOCK(CraftNoteBlock.class, "org.bukkit.block.NoteBlock"),
	SIGN(CraftSign.class, "org.bukkit.block.Sign"),
	SKULL(CraftSkull.class, "org.bukkit.block.Skull"),
	STRUCTURE(CraftStructure.class, "org.bukkit.block.Structure");

	private static final ConcurrentMap<Class<? extends org.bukkit.block.BlockState>, CraftBlockType> BY_STATE = Concurrent.newMap();
	private final Class<? extends BlockState> clazz;
	private final Class<? extends org.bukkit.block.BlockState> bukkitClazz;

	static {
		Arrays.stream(values()).filter(type -> type.getBukkitClass() != null).forEach(type -> BY_STATE.put(type.getBukkitClass(), type));
	}

	@SuppressWarnings("unchecked")
	CraftBlockType(Class<? extends BlockState> clazz, String bukkitBlockPath) {
		this.clazz = clazz;
		Class<? extends org.bukkit.block.BlockState> bukkitClazz = null;
		boolean supported = false;

		if (StringUtil.notEmpty(bukkitBlockPath)) {
			try {
				bukkitClazz = (Class<? extends org.bukkit.block.BlockState>)Class.forName(bukkitBlockPath);
			} catch (ClassNotFoundException ignore) { }
		}

		this.bukkitClazz = bukkitClazz;
	}

	public Class<? extends BlockState> getBlockClass() {
		return this.clazz;
	}

	public Class<? extends org.bukkit.block.BlockState> getBukkitClass() {
		return this.bukkitClazz;
	}

	public static CraftBlockType getByState(Class<? extends org.bukkit.block.BlockState> bukkitState) {
		return BY_STATE.get(bukkitState);
	}

}