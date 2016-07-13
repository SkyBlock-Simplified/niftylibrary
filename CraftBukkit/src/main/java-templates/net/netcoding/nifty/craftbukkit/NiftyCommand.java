package net.netcoding.nifty.craftbukkit;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.Command;
import net.netcoding.nifty.common.api.plugin.MinecraftListener;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;
import net.netcoding.nifty.core.api.color.ChatColor;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.NumberUtil;

import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

final class NiftyCommand extends MinecraftListener {

    NiftyCommand(MinecraftPlugin plugin) {
		super(plugin);
	}

	@Command(name = "nifty",
			minimumArgs = 0,
			maximumArgs = 2,
			usages = {
					@Command.Usage(match = "search", replace = "<player|uuid>"),
					@Command.Usage(match = "lookup", replace = "<player|uuid>"),
					@Command.Usage(match = "uiid", replace = "<player|uuid>")
	})
	protected void onCommand(CommandSource source, String label, String[] args) throws Exception {
		Set<MinecraftPlugin> pluginCache = Nifty.getPluginManager().getPlugins();

		if (ListUtil.isEmpty(args) || NumberUtil.isNumber(args[0])) {
			int rounded = NumberUtil.roundUp(pluginCache.size(), 5);
			int pages = rounded / 5;
			int page = args.length == 1 ? NumberUtil.isNumber(args[0]) ? pluginCache.size() > 5 ? Integer.parseInt(args[0]) : 0 : 0 : 0;

			if (page <= 0)
				page = 1;

			if (page * 5 > rounded)
				page = pages;

			int start = ((page - 1) * 5);
			int many = Math.min(pluginCache.size() - start, 5);
			this.getLog().message(source, "[{{0}} (Page {{1}}/{{2}})]", "${name} Implementations", page, pages);
			Iterator<MinecraftPlugin> iterator = pluginCache.iterator();

			for (int i = 0; i < start; i++)
				iterator.next();

			for (int i = start; i < (start + many); i++) {
				MinecraftPlugin plugin = iterator.next();
				NiftyPluginInfo info = new NiftyPluginInfo(plugin.getName());
				boolean error = !info.isEnabled() || info.getErrorCount() > 0;
				this.getLog().message(source, "{0} [{{1}}]{2}", plugin.getName(), info.getVersion(), (error ? ChatColor.RED + " !" : ""));
			}
		} else {
			String pluginName = args[0];

			if (pluginName.matches("^(lookup|search|uuid|user(name)?)$")) {
				if (args.length < 2) {
					this.showUsage(source);
					return;
				}

				try {
                    MinecraftMojangProfile profile = Nifty.getMojangRepository().searchByUniqueId(UUID.fromString(args[1]));
					this.getLog().message(source, "The name for {{0}} is {{1}}.", profile.getUniqueId(), profile.getName());
				} catch (Exception ex) {
                    MinecraftMojangProfile profile = Nifty.getMojangRepository().searchByUsername(args[1]);
					this.getLog().message(source, "The UUID of {{0}} is {{1}}.", profile.getName(), profile.getUniqueId());
				}
			} else {
				for (MinecraftPlugin plugin : pluginCache) {
					if (plugin.getName().equalsIgnoreCase(pluginName)) {
						pluginName = plugin.getName();
						break;
					}
				}

				NiftyPluginInfo info = new NiftyPluginInfo(pluginName);

				if ("Bukkit".equals(pluginName)) {
					this.getLog().message(source, "[{{0}} Information]", pluginName);
					this.getLog().message(source, "Version: {{0}}", MinecraftProtocol.getCurrentVersion());
					this.getLog().message(source, "Errors: {{0}}", info.getErrors());
				} else {
					if (info.exists()) {
						this.getLog().message(source, "[{{0}} Information]", pluginName);
						this.getLog().message(source, "Version: {{0}}", info.getVersion());
						this.getLog().message(source, "Status: {{0}}", info.getStatus());
						this.getLog().message(source, "Errors: {{0}}", info.getErrors());
					} else
						this.getLog().message(source, "{{0}} is an invalid plugin name!", pluginName);
				}
			}
		}
	}

	/*@Override
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
	}*/

	private class NiftyPluginInfo {

		private final transient String pluginName;

		public NiftyPluginInfo(String pluginName) {
			this.pluginName = pluginName;
		}

		public boolean exists() {
			return this.getPlugin() != null;
		}

		public int getErrorCount() {
			return Nifty.getPluginManager().getPluginErrors(this.getPlugin()).size();
		}

		public String getErrors() {
			return (this.getErrorCount() == 0 ? ChatColor.GREEN.toString() : "") + this.getErrorCount();
		}

		public MinecraftPlugin getPlugin() {
			return Nifty.getPluginManager().getPlugin(pluginName);
		}

		public String getStatus() {
			return (this.isEnabled() ? ChatColor.GREEN + "En" : "Dis") + "abled";
		}

		public String getVersion() {
			return ChatColor.WHITE + this.getPlugin().getDesc().getVersion();
		}

		public boolean isEnabled() {
			return this.getPlugin().isEnabled();
		}

	}

}