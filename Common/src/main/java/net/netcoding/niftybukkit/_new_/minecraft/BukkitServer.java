package net.netcoding.niftybukkit._new_.minecraft;

import com.google.common.base.Preconditions;
import net.netcoding.niftybukkit._new_.minecraft.entity.living.Player;
import net.netcoding.niftybukkit._new_.minecraft.inventory.Inventory;
import net.netcoding.niftybukkit._new_.minecraft.inventory.InventoryHolder;
import net.netcoding.niftybukkit._new_.minecraft.inventory.InventoryType;
import net.netcoding.niftybukkit._new_.minecraft.region.World;
import net.netcoding.niftybukkit._new_.minecraft.source.command.ConsoleCommandSource;
import net.netcoding.niftycore.api.Server;
import net.netcoding.niftycore.util.StringUtil;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BukkitServer extends Server<Player> {

	default Inventory createInventory(InventoryHolder holder, InventoryType type) {
		return this.createInventory(holder, type, type.getDefaultTitle());
	}

	default Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
		return this.createInventory(holder, type.getDefaultSize(), title);
	}

	default Inventory createInventory(InventoryHolder holder, int size) {
		return this.createInventory(holder, size, InventoryType.CHEST.getDefaultTitle());
	}

	Inventory createInventory(InventoryHolder holder, int size, String title); // TODO

	ConsoleCommandSource getConsoleSource();

	@Override
	Player getPlayer(String name);

	@Override
	Player getPlayer(UUID uniqueId);

	OfflinePlayer getOfflinePlayer(String name);

	OfflinePlayer getOfflinePlayer(UUID uniqueId);

	@Override
	Collection<? extends Player> getPlayerList();

	default World getWorld(String name) {
		Preconditions.checkArgument(StringUtil.notEmpty(name), "Name cannot be NULL!");

		for (World world : this.getWorlds()) {
			if (world.getName().equalsIgnoreCase(name))
				return world;
		}

		throw new IllegalArgumentException(StringUtil.format("Unable to find world with name ''{0}''!", name));
	}

	default World getWorld(UUID uniqueId) {
		Preconditions.checkArgument(uniqueId != null, "UniqueId cannot be NULL!");

		for (World world : this.getWorlds()) {
			if (world.getUniqueId().equals(uniqueId))
				return world;
		}

		throw new IllegalArgumentException(StringUtil.format("Unable to find world with uid ''{0}''!", uniqueId));
	}

	List<World> getWorlds();

}