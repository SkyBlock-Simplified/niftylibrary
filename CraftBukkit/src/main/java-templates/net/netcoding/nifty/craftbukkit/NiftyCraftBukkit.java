package net.netcoding.nifty.craftbukkit;

import net.milkbowl.vault.permission.Permission;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.inventory.item.ItemDatabase;
import net.netcoding.nifty.common.api.inventory.item.MiniBlockDatabase;
import net.netcoding.nifty.common.api.inventory.item.enchantment.EnchantmentDatabase;
import net.netcoding.nifty.common.api.nbt.NbtFactory;
import net.netcoding.nifty.common.api.plugin.MinecraftLogger;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.api.plugin.PluginManager;
import net.netcoding.nifty.common.api.plugin.messaging.BungeeHelper;
import net.netcoding.nifty.common.api.plugin.messaging.Messenger;
import net.netcoding.nifty.common.minecraft.FireworkEffect;
import net.netcoding.nifty.common.minecraft.Server;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemFactory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.potion.Potion;
import net.netcoding.nifty.common.minecraft.potion.PotionBrewer;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.mojang.MinecraftMojangRepository;
import net.netcoding.nifty.common.yaml.BukkitConfig;
import net.netcoding.nifty.core.api.plugin.PluginDescription;
import net.netcoding.nifty.core.api.plugin.annotations.Dependency;
import net.netcoding.nifty.core.api.plugin.annotations.Plugin;
import net.netcoding.nifty.core.api.scheduler.MinecraftScheduler;
import net.netcoding.nifty.craftbukkit.api.inventory.item.CraftItemDatabase;
import net.netcoding.nifty.craftbukkit.api.inventory.item.enchantment.CraftEnchantmentDatabase;
import net.netcoding.nifty.craftbukkit.api.nbt.CraftNbtFactory;
import net.netcoding.nifty.craftbukkit.api.plugin.CraftPluginManager;
import net.netcoding.nifty.craftbukkit.api.plugin.messaging.CraftBungeeHelper;
import net.netcoding.nifty.craftbukkit.minecraft.CraftServer;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemFactory;
import net.netcoding.nifty.craftbukkit.minecraft.inventory.item.CraftItemStack;
import net.netcoding.nifty.craftbukkit.minecraft.potion.CraftPotionBrewer;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftLocation;
import net.netcoding.nifty.craftbukkit.mojang.CraftMojangRepository;
import net.netcoding.nifty.craftbukkit.yaml.converters.CraftBlockConverter;
import net.netcoding.nifty.craftbukkit.yaml.converters.CraftItemStackConverter;
import net.netcoding.nifty.craftbukkit.yaml.converters.CraftLocationConverter;
import net.netcoding.nifty.craftbukkit.yaml.converters.CraftPotionEffectConverter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

@Plugin(name = "${name}", version = "${version}", dependencies = {
		@Dependency(id = "ProtocolLib", optional = true),
		@Dependency(id = "Vault", optional = true)
})
public final class NiftyCraftBukkit extends JavaPlugin {

	private NiftyMinecraft nifty;

	@Override
	public void onLoad() {
		this.nifty = new NiftyMinecraft(this);
	}

	@Override
	public void onEnable() {
		this.nifty.onEnable();
	}

	private class NiftyMinecraft extends MinecraftPlugin {

		private final NiftyCraftBukkit main;
		private final PluginDescription description;

		public NiftyMinecraft(NiftyCraftBukkit main) {
			this.main = main;
			this.description = new PluginDescription(main.getName(), main.getFile(), main.getDataFolder(), main.getDescription().getVersion());
			this.load();
		}

		@Override
		public PluginDescription getDesc() {
			return this.description;
		}

		public NiftyCraftBukkit getMain() {
			return this.main;
		}

		private void load() {
			this.getLog().console("Registering Builders");
			Nifty.getBuilderManager().provide(this, FireworkEffect.class, FireworkEffect.Builder.class);
			Nifty.getBuilderManager().provide(this, ItemStack.class, CraftItemStack.Builder.class);
			Nifty.getBuilderManager().provide(this, Location.class, CraftLocation.Builder.class);
			Nifty.getBuilderManager().provide(this, Potion.class, Potion.Builder.class);

			this.getLog().console("Registering Core Services");
			Nifty.getServiceManager().provide(this, Thread.class, Thread.currentThread());
			Nifty.getServiceManager().provide(this, MinecraftPlugin.class, this);
			Nifty.getServiceManager().provide(this, JavaPlugin.class, this.getMain());
			Nifty.getServiceManager().provide(this, MinecraftLogger.class, this.getLog());
			Nifty.getServiceManager().provide(this, Messenger.class, Messenger.getInstance());
			Nifty.getServiceManager().provide(this, Server.class, CraftServer.getInstance());
			Nifty.getServiceManager().provide(this, PluginManager.class, CraftPluginManager.getInstance());
			Nifty.getServiceManager().provide(this, BungeeHelper.class, CraftBungeeHelper.getInstance());
			Nifty.getServiceManager().provide(this, MinecraftScheduler.class, MinecraftScheduler.getInstance());
			Nifty.getServiceManager().provide(this, MinecraftMojangRepository.class, CraftMojangRepository.getInstance());
			Nifty.getServiceManager().provide(this, EnchantmentDatabase.class, CraftEnchantmentDatabase.getInstance());
			Nifty.getServiceManager().provide(this, ItemFactory.class, CraftItemFactory.getInstance());
			Nifty.getServiceManager().provide(this, MiniBlockDatabase.class, MiniBlockDatabase.getInstance());
			Nifty.getServiceManager().provide(this, ItemDatabase.class, CraftItemDatabase.getInstance());
			Nifty.getServiceManager().provide(this, NbtFactory.class, CraftNbtFactory.getInstance());
			Nifty.getServiceManager().provide(this, PotionBrewer.class, CraftPotionBrewer.getinstance());

			this.getLog().console("Registering 3rd-party Services");
			if (Nifty.getPluginManager().hasPlugin("ProtocolLib"))
				Nifty.getServiceManager().provide(this, com.comphenix.protocol.ProtocolManager.class, com.comphenix.protocol.ProtocolLibrary.getProtocolManager());

			if (Nifty.getPluginManager().hasPlugin("Vault")) {
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

			this.getLog().console("Loading Plugins");
			Nifty.getPluginManager().loadPlugins();
		}

		@Override
		public void onEnable() {
			new NiftyCommand(this);
			new NiftyListener(this.getMain(), this);
		}

	}

}