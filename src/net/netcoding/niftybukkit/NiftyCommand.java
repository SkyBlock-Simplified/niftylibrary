package net.netcoding.niftybukkit;

import java.util.HashSet;
import java.util.Iterator;

import net.netcoding.niftybukkit.minecraft.BukkitCommand;
import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.BukkitPlugin;
import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.NumberUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;

final class NiftyCommand extends BukkitCommand {

	public NiftyCommand(JavaPlugin plugin) {
		super(plugin, "nifty");
		this.setMinimumArgsLength(0);
	}

	@Override
	protected void onCommand(CommandSender sender, String alias, String[] args) throws Exception {
		HashSet<String> pluginCache = new HashSet<>(BukkitPlugin.getPluginCache());
		HashSet<String> helperCache = new HashSet<>(BukkitHelper.getPluginCache());
		HashSet<String> totalCache = new HashSet<>(pluginCache);
		totalCache.addAll(helperCache);

		if (ListUtil.isEmpty(args) || NumberUtil.isInt(args[0])) {
			int page = ListUtil.isEmpty(args) ? 0 : NumberUtil.isInt(args[0]) ? totalCache.size() > 5 ? Integer.parseInt(args[0]) : 0 : 0;
			if (page == 0) page = 1;
			Iterator<String> totalIterator = totalCache.iterator();
			this.getLog().message(sender, "[{{0}}]", "Plugins Implementing NiftyBukkit");
			this.getLog().message(sender, "----------------------------");

			if (totalCache.size() > 5 && page > 1) {
				for (int i = 0; i < (5 * page); i++)
					totalIterator.next();
			}

			for (int i = 0; i < (totalCache.size() < 5 ? totalCache.size() : 5); i++) {
				String pluginName = totalIterator.next();
				NiftyPluginInfo info = new NiftyPluginInfo(pluginName);
				boolean trouble = !info.isUsingBukkitPlugin() || info.getBukkitCommandCount() < info.getTotalCommandCount();
				boolean error = !info.isEnabled() || info.getErrorCount() > 0;
				this.getLog().message(sender, "{0} [{{1}}]{2}", pluginName, info.getVersion(), (error ? ChatColor.RED + " !" : (trouble ? ChatColor.YELLOW + " !" : "")));
			}
		} else {
			String pluginName = args[0];

			for (String cache : totalCache) {
				if (cache.equalsIgnoreCase(pluginName)) {
					pluginName = cache;
					break;
				}
			}

			NiftyPluginInfo info = new NiftyPluginInfo(pluginName);

			if (info.exists()) {
				this.getLog().message(sender, "[{{0}}]", StringUtil.format("Information Regarding {0}", pluginName));
				this.getLog().message(sender, "--------------------------------");
				this.getLog().message(sender, "Version: {{0}}", info.getVersion());
				this.getLog().message(sender, "Status: {{0}}", info.getStatus());
				this.getLog().message(sender, "Errors: {{0}}", info.getErrors());
				this.getLog().message(sender, "Helpers:");
				this.getLog().message(sender, "  BukkitPlugin: {{0}}", info.getUsingBukkitPlugin());
				this.getLog().message(sender, "  Commands: {{0}}/{{1}}", info.getBukkitCommands(), info.getTotalCommands());
				this.getLog().message(sender, "  Listeners: {{0}}/{{1}}", info.getBukkitListeners(), info.getTotalListeners());
			} else
				this.getLog().message(sender, "{{0}} is an invalid plugin name!", pluginName);
		}
	}

	private class NiftyPluginInfo {

		private final transient JavaPlugin plugin;

		public NiftyPluginInfo(String pluginName) {
			this.plugin = (JavaPlugin)NiftyBukkit.getPlugin().getServer().getPluginManager().getPlugin(pluginName);
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
			return this.plugin;
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
					if (BukkitListener.class.isAssignableFrom(clazz)) {
						used.add(clazz.getName());
						totalListeners++;
					}
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