package net.netcoding.niftybukkit.minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class BukkitTabCommand extends BukkitCommand implements TabExecutor {

	// Binder
	public BukkitTabCommand(JavaPlugin plugin, String command) {
		super(plugin, command);
		this.getCommand().setTabCompleter(this);
	}

	public abstract List<String> tabComplete(CommandSender sender, String[] args);

	@Override
	@EventHandler(priority = EventPriority.HIGHEST)
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if (this.hasPermissions(sender, this.getCommand().getPermission()))
			return this.tabComplete(sender, args);
		else
			return Collections.emptyList();
	}

	protected static <T> List<T> iterableToList(Iterator<T> list) {
		List<T> copy = new ArrayList<T>();
		while (list.hasNext()) copy.add(list.next());
		return copy;
	}

}