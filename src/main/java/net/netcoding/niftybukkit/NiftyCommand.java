package net.netcoding.niftybukkit;

import net.netcoding.niftybukkit.minecraft.BukkitCommand;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.BukkitPlugin;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit.reflection.MinecraftProtocol;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.NumberUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

final class NiftyCommand extends BukkitCommand {

    NiftyCommand(JavaPlugin plugin) {
		super(plugin, "nifty");
		this.setMinimumArgsLength(0);
	    this.setMaximumArgsLength(1);
		this.editUsage(1, "search", "<player|uuid>");
		this.editUsage(1, "lookup", "<player|uuid>");
	}

	@Override
	protected void onCommand(CommandSender sender, String alias, String[] args) throws Exception {
		List<String> nameCache = BukkitPlugin.getPluginCache();

		if (ListUtil.isEmpty(args) || NumberUtil.isInt(args[0])) {
			int rounded = NumberUtil.roundUp(nameCache.size(), 5);
			int pages = rounded / 5;
			int page = args.length == 1 ? NumberUtil.isInt(args[0]) ? nameCache.size() > 5 ? Integer.parseInt(args[0]) : 0 : 0 : 0;

			if (page <= 0)
				page = 1;

			if (page * 5 > rounded)
				page = pages;

			int start = ((page - 1) * 5);
			int many = Math.min(nameCache.size() - start, 5);
			this.getLog().message(sender, "[{{0}} (Page {{1}}/{{2}})]", "NiftyBukkit Implementations", page, pages);

			for (int i = start; i < (start + many); i++) {
				String pluginName = nameCache.get(i);
				NiftyPluginInfo info = new NiftyPluginInfo(pluginName);
				boolean trouble = !info.isUsingBukkitPlugin() || info.getBukkitCommandCount() < info.getTotalCommandCount();
				boolean error = !info.isEnabled() || info.getErrorCount() > 0;
				this.getLog().message(sender, "{0} [{{1}}]{2}", pluginName, info.getVersion(), (error ? ChatColor.RED + " !" : (trouble ? ChatColor.YELLOW + " !" : "")));
			}
		} else {
			String pluginName = args[0];

			if (pluginName.matches("^(lookup|search|uuid|user(name)?)$")) {
				if (args.length < 2) {
					this.showUsage(sender);
					return;
				}

				try {
                    BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByUniqueId(UUID.fromString(args[1]));
					this.getLog().message(sender, "The name for {{0}} is {{1}}.", profile.getUniqueId(), profile.getName());
				} catch (Exception ex) {
                    BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByUsername(args[1]);
					this.getLog().message(sender, "The UUID of {{0}} is {{1}}.", profile.getName(), profile.getUniqueId());
				}
			} else {
				for (String cache : nameCache) {
					if (cache.equalsIgnoreCase(pluginName)) {
						pluginName = cache;
						break;
					}
				}

				NiftyPluginInfo info = new NiftyPluginInfo(pluginName);

				if ("Bukkit".equals(pluginName)) {
					this.getLog().message(sender, "[{{0}} Information]", pluginName);
					this.getLog().message(sender, "Version: {{0}}", MinecraftProtocol.getCurrentVersion());
					this.getLog().message(sender, "Errors: {{0}}", info.getErrors());
				} else {
					if (info.exists()) {
						this.getLog().message(sender, "[{{0}} Information]", pluginName);
						this.getLog().message(sender, "Version: {{0}}", info.getVersion());
						this.getLog().message(sender, "Status: {{0}}", info.getStatus());
						this.getLog().message(sender, "Errors: {{0}}", info.getErrors());
						this.getLog().message(sender, "Helpers:");
						this.getLog().message(sender, "   BukkitPlugin: {{0}}", info.getUsingBukkitPlugin());
						this.getLog().message(sender, "   Commands: {{0}}/{{1}}", info.getBukkitCommands(), info.getTotalCommands());
						this.getLog().message(sender, "   Listeners: {{0}}/{{1}}", info.getBukkitListeners(), info.getTotalListeners());
					} else
						this.getLog().message(sender, "{{0}} is an invalid plugin name!", pluginName);
				}
			}
		}
	}

	@Override
	protected List<String> onTabComplete(CommandSender sender, String label, String[] args) throws Exception {
		Plugin[] plugins = this.getPlugin().getServer().getPluginManager().getPlugins();
		final String arg = args[0].toLowerCase();
		List<String> names = new ArrayList<>();

		for (Plugin plugin : plugins) {
			String pluginName = plugin.getName();

			if (pluginName.toLowerCase().startsWith(arg) || pluginName.toLowerCase().contains(arg))
				names.add(pluginName);
		}

		return names;
	}

	private class NiftyPluginInfo {

		private final transient String pluginName;

		public NiftyPluginInfo(String pluginName) {
			this.pluginName = pluginName;
		}

		public boolean exists() {
			return this.getPlugin() != null;
		}

		public int getBukkitCommandCount() {
			return BukkitCommand.getPluginCache(this.getPlugin().getName());
		}

		public String getBukkitCommands() {
			return (this.getBukkitCommandCount() == this.getTotalCommandCount() ? ChatColor.GREEN.toString() : "") + this.getBukkitCommandCount();
		}

		public int getBukkitListenerCount() {
			return BukkitListener.getPluginCache(this.getPlugin().getName());
		}

		public String getBukkitListeners() {
			return (this.getBukkitListenerCount() == this.getTotalListenerCount() ? ChatColor.GREEN.toString() : "") + this.getBukkitListenerCount();
		}

		public int getErrorCount() {
			return NiftyBukkit.getPluginCache(this.getPlugin().getName()).size();
		}

		public String getErrors() {
			return (this.getErrorCount() == 0 ? ChatColor.GREEN.toString() : "") + this.getErrorCount();
		}

		public JavaPlugin getPlugin() {
			return (JavaPlugin)NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin(pluginName);
		}

		public String getStatus() {
			return (this.isEnabled() ? ChatColor.GREEN + "En" : "Dis") + "abled";
		}

		public int getTotalCommandCount() {
			int totalCommands = 0;

			for (HelpTopic topic : this.getPlugin().getServer().getHelpMap().getHelpTopics()) {
				if (topic.getName().equals(this.getPlugin().getName())) {
					totalCommands = topic.getFullText(this.getPlugin().getServer().getConsoleSender()).split("\n").length - 1;
					break;
				}
			}

			return totalCommands;
		}

		public String getTotalCommands() {
			return (this.getBukkitCommandCount() == this.getTotalCommandCount() ? ChatColor.GREEN.toString() : "") + this.getTotalCommandCount();
		}

		public int getTotalListenerCount() {
			int totalListeners = 0;
			HashSet<String> used = new HashSet<>();

			for (RegisteredListener listener : HandlerList.getRegisteredListeners(this.getPlugin())) {
				Class<?> clazz = listener.getListener().getClass();

				if (!used.contains(clazz.getName())) {
					used.add(clazz.getName());
					totalListeners++;
				}
			}

			return totalListeners;
		}

		public String getTotalListeners() {
			return (this.getBukkitListenerCount() == this.getTotalListenerCount() ? ChatColor.GREEN.toString() : "") + this.getTotalListenerCount();
		}

		public String getUsingBukkitPlugin() {
			return BukkitPlugin.getPluginCache().contains(this.getPlugin().getName()) ? ChatColor.GREEN + "Yes" : "No";
		}

		public String getVersion() {
			return ChatColor.WHITE + this.getPlugin().getDescription().getVersion();
		}

		public boolean isEnabled() {
			return this.getPlugin().isEnabled();
		}

		public boolean isUsingBukkitPlugin() {
			return BukkitPlugin.getPluginCache().contains(this.getPlugin().getName());
		}

	}

}