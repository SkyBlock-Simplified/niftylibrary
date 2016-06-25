package net.netcoding.nifty.common.minecraft;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.api.plugin.PluginManager;
import net.netcoding.nifty.common.minecraft.entity.living.Player;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryType;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.common.minecraft.command.source.ConsoleCommandSource;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.core.api.Server;
import net.netcoding.nifty.core.util.StringUtil;

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

	GameMode getDefaultGameMode();

	@Override
	Player getPlayer(String name);

	@Override
	Player getPlayer(UUID uniqueId);

	OfflinePlayer getOfflinePlayer(String name);

	OfflinePlayer getOfflinePlayer(UUID uniqueId);

	@Override
	Collection<? extends Player> getPlayerList();

	default <T extends MinecraftPlugin> T getPlugin(Class<T> plugin) {
		return this.getPluginManager().getPlugin(plugin);
	}

	default PluginManager getPluginManager() {
		return Nifty.getServiceManager().getProvider(PluginManager.class);
	}

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