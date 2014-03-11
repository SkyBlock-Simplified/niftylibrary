package net.netcoding.niftybukkit.minecraft;

import java.util.List;

import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitHelper {

	private final transient JavaPlugin javaPlugin;
	private final transient Log logger;

	public BukkitHelper(JavaPlugin plugin) {
		this.javaPlugin = plugin;
		this.logger = new Log(plugin.getLogger());
	}	public List<String> getAuthor() {
		return this.getDescriptionFile().getAuthors();
	}

	public String getDescription() {
		return this.getDescriptionFile().getDescription();
	}

	private PluginDescriptionFile getDescriptionFile() {
		return this.getPlugin().getDescription();
	}

	public String getName() {
		return this.getDescriptionFile().getName();
	}

	public String getVersion() {
		return this.getDescriptionFile().getVersion();
	}

	public synchronized Log getLog() {
		return this.logger;
	}

	public synchronized JavaPlugin getPlugin() {
		return this.javaPlugin;
	}

	public <T extends JavaPlugin> T getPlugin(Class<T> plugin) {
		return plugin.cast(this.getPlugin());
	}

	public PluginDescriptionFile getPluginDescription() {
		return this.getPlugin().getDescription();
	}

	public boolean hasPermissions(CommandSender sender, String... permissions) {
		if (isConsole(sender)) return true;
		return sender.hasPermission(String.format("%s.%s", this.getPlugin().getDescription().getName(), StringUtil.implode(".", permissions)));
	}

	public static boolean isConsole(CommandSender sender) {
		return !isPlayer(sender);
	}

	public static boolean isConsole(String senderName) {
		return !isPlayer(senderName);
	}

	public static boolean isPlayer(CommandSender sender) {
		return isPlayer(sender.getName());
	}

	public static boolean isPlayer(String senderName) {
		return !senderName.equalsIgnoreCase("console");
	}

	public static Player matchPlayer(String playerName) {
		Player player = Bukkit.getPlayer(playerName);

		if (player != null)
			return player;
		else {
			for (Player iPlayer : Bukkit.getOnlinePlayers()) {
				if (iPlayer.getName().toLowerCase().startsWith(playerName.toLowerCase()))
					return iPlayer;
			}

			return null;
		}
	}

	public static String matchPlayerName(String playerName) {
		Player player = matchPlayer(playerName);

		if (player != null)
			return player.getName();
		else
			return playerName;
	}

}