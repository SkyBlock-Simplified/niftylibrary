package net.netcoding.nifty.craftbukkit;

import net.milkbowl.vault.permission.Permission;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common._new_.api.BukkitLogger;
import net.netcoding.nifty.common._new_.api.inventory.item.ItemDatabase;
import net.netcoding.nifty.common._new_.api.inventory.item.MiniBlockDatabase;
import net.netcoding.nifty.common._new_.api.inventory.item.enchantment.EnchantmentDatabase;
import net.netcoding.nifty.common._new_.api.nbt.NbtFactory;
import net.netcoding.nifty.common._new_.api.plugin.PluginManager;
import net.netcoding.nifty.common._new_.api.plugin.messaging.BungeeHelper;
import net.netcoding.nifty.common._new_.api.plugin.messaging.Messenger;
import net.netcoding.nifty.common._new_.minecraft.BukkitServer;
import net.netcoding.nifty.common._new_.minecraft.FireworkEffect;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangRepository;
import net.netcoding.nifty.common._new_.yaml.BukkitConfig;
import net.netcoding.nifty.core.api.plugin.Plugin;
import net.netcoding.nifty.core.api.scheduler.MinecraftScheduler;
import net.netcoding.nifty.craftbukkit.api.inventory.item.CraftItemDatabase;
import net.netcoding.nifty.craftbukkit.api.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.api.inventory.item.enchantment.CraftEnchantmentDatabase;
import net.netcoding.nifty.craftbukkit.api.nbt.CraftNbtFactory;
import net.netcoding.nifty.craftbukkit.api.plugin.CraftPlugin;
import net.netcoding.nifty.craftbukkit.api.plugin.CraftPluginManager;
import net.netcoding.nifty.craftbukkit.api.plugin.messaging.CraftBungeeHelper;
import net.netcoding.nifty.craftbukkit.minecraft.CraftServer;
import net.netcoding.nifty.craftbukkit.mojang.CraftMojangRepository;
import net.netcoding.nifty.craftbukkit.yaml.converters.CraftBlockConverter;
import net.netcoding.nifty.craftbukkit.yaml.converters.CraftItemStackConverter;
import net.netcoding.nifty.craftbukkit.yaml.converters.CraftLocationConverter;
import net.netcoding.nifty.craftbukkit.yaml.converters.CraftPotionEffectConverter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class NiftyCraftBukkit extends CraftPlugin {

	@Override
	public void onLoad() {
		this.getLog().console("Registering Builders");
		Nifty.getBuilderManager().provide(this, FireworkEffect.class, FireworkEffect.Builder.class);
		Nifty.getBuilderManager().provide(this, ItemStack.class, CraftItemStack.Builder.class);

		this.getLog().console("Registering Core Services");
		Nifty.getServiceManager().provide(this, Thread.class, Thread.currentThread());
		Nifty.getServiceManager().provide(this, Plugin.class, this);
		Nifty.getServiceManager().provide(this, JavaPlugin.class, this);
		Nifty.getServiceManager().provide(this, BukkitLogger.class, this.getLog());
		Nifty.getServiceManager().provide(this, Messenger.class, Messenger.getInstance());
		Nifty.getServiceManager().provide(this, BukkitServer.class, CraftServer.getInstance());
		Nifty.getServiceManager().provide(this, PluginManager.class, CraftPluginManager.getInstance());
		Nifty.getServiceManager().provide(this, BungeeHelper.class, CraftBungeeHelper.getInstance());
		Nifty.getServiceManager().provide(this, MinecraftScheduler.class, MinecraftScheduler.getInstance());
		Nifty.getServiceManager().provide(this, BukkitMojangRepository.class, CraftMojangRepository.getInstance());
		Nifty.getServiceManager().provide(this, EnchantmentDatabase.class, CraftEnchantmentDatabase.getInstance());
		Nifty.getServiceManager().provide(this, MiniBlockDatabase.class, MiniBlockDatabase.getInstance());
		Nifty.getServiceManager().provide(this, ItemDatabase.class, CraftItemDatabase.getInstance());
		Nifty.getServiceManager().provide(this, NbtFactory.class, CraftNbtFactory.getInstance());

		this.getLog().console("Registering 3rd-party Services");
		if (Nifty.getServer().getPluginManager().hasPlugin("ProtocolLib"))
			Nifty.getServiceManager().provide(this, com.comphenix.protocol.ProtocolManager.class, com.comphenix.protocol.ProtocolLibrary.getProtocolManager());

		if (Nifty.getServer().getPluginManager().hasPlugin("Vault")) {
			RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);

			if (permissionProvider != null)
				Nifty.getServiceManager().provide(this, net.milkbowl.vault.permission.Permission.class, permissionProvider.getProvider());
		}

		this.getLog().console("Registering CraftBukkit Converters");
		BukkitConfig.addGlobalCustomConverter(CraftBlockConverter.class);
		BukkitConfig.addGlobalCustomConverter(CraftItemStackConverter.class);
		BukkitConfig.addGlobalCustomConverter(CraftLocationConverter.class);
		BukkitConfig.addGlobalCustomConverter(CraftPotionEffectConverter.class);
		BukkitConfig.stopAcceptingGlobalConverters();
	}

	@Override
	public void onEnable() {
		new NiftyCommand(this);
		new NiftyListener(this);
	}

	public void WIP() {

	}

	/*private class LogHandler extends Handler {

		private final transient Plugin plugin;

		public LogHandler() {
			this(null);
		}

		public LogHandler(Plugin plugin) {
			this.plugin = plugin;
		}

		@Override
		public void close() throws SecurityException { }

		@Override
		public void flush() { }

		@Override
		public void publish(LogRecord record) {
			if (record.getThrown() != null) {
				String pluginName = this.plugin != null ? this.plugin.getName() : null;

				if (StringUtil.isEmpty(pluginName)) {
					try {
						String[] parts = record.getLoggerName().split("\\.");
						Plugin plugin = NiftyBukkit.this.getServer().getPluginManager().getPlugin(parts[parts.length - 1]);
						if (plugin != null) pluginName = parts[parts.length - 1];
					} catch (Exception ignore) { }
				}

				PLUGINS.get(StringUtil.isEmpty(pluginName) ? "Bukkit" : pluginName).add(record.getThrown().getMessage());
			}
		}

	}*/

}