package net.netcoding.nifty.common.minecraft;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.PluginManager;
import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.common.minecraft.command.ConsoleCommandSource;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemFactory;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.core.api.IServer;
import net.netcoding.nifty.core.util.StringUtil;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface Server extends IServer<Player> {

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

	default boolean dispatchCommand(CommandSource source, String command) {
		Preconditions.checkArgument(source != null, "Source cannot be NULL!");
		Preconditions.checkArgument(StringUtil.notEmpty(command), "Command cannot be NULL!");
		String[] parts = StringUtil.split(" ", command);
		command = parts[0];
		String[] args = StringUtil.split(" ", StringUtil.implode(" ", parts, 1));
		Nifty.getPluginManager().dispatch(source, command.replace("/", ""), args);
		return true;
	}

	ConsoleCommandSource getConsoleSource();

	GameMode getDefaultGameMode();

	default ItemFactory getItemFactory() {
		return Nifty.getItemFactory();
	}

	OfflinePlayer getOfflinePlayer(String name);

	OfflinePlayer getOfflinePlayer(UUID uniqueId);

	OfflinePlayer[] getOfflinePlayers();

	@Override
	default Player getPlayer(String name) {
		return this.getPlayer(name, false);
	}

	default Player getPlayer(String name, boolean loose) {
		Preconditions.checkArgument(StringUtil.notEmpty(name), "Name cannot be NULL!");
		name = name.toLowerCase();

		for (Player player : this.getPlayerList()) {
			if (player.getName().equalsIgnoreCase(name))
				return player;
		}

		if (loose) {
			for (Player player : this.getPlayerList()) {
				if (player.getName().toLowerCase().startsWith(name)) return player;
			}
		}

		return null;
	}

	@Override
	default Player getPlayer(UUID uniqueId) {
		Preconditions.checkArgument(uniqueId != null, "UniqueId cannot be NULL!");

		for (Player player : this.getPlayerList()) {
			if (player.getUniqueId().equals(uniqueId))
				return player;
		}

		return null;
	}

	@Override
	Collection<? extends Player> getPlayerList();

	default PluginManager getPluginManager() {
		return Nifty.getPluginManager();
	}

	default World getWorld(String name) {
		Preconditions.checkArgument(StringUtil.notEmpty(name), "Name cannot be NULL!");

		for (World world : this.getWorlds()) {
			if (world.getName().equalsIgnoreCase(name))
				return world;
		}

		return null;
	}

	default World getWorld(UUID uniqueId) {
		Preconditions.checkArgument(uniqueId != null, "UniqueId cannot be NULL!");

		for (World world : this.getWorlds()) {
			if (world.getUniqueId().equals(uniqueId))
				return world;
		}

		return null;
	}

	List<World> getWorlds();

}