package net.netcoding.nifty.common;

import net.netcoding.nifty.common.api.inventory.item.ItemDatabase;
import net.netcoding.nifty.common.api.inventory.item.MiniBlockDatabase;
import net.netcoding.nifty.common.api.inventory.item.enchantment.EnchantmentDatabase;
import net.netcoding.nifty.common.api.nbt.NbtFactory;
import net.netcoding.nifty.common.api.plugin.MinecraftLogger;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.api.plugin.PluginManager;
import net.netcoding.nifty.common.api.plugin.messaging.BungeeHelper;
import net.netcoding.nifty.common.api.plugin.messaging.Messenger;
import net.netcoding.nifty.common.minecraft.Server;
import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemFactory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.common.mojang.MinecraftMojangRepository;
import net.netcoding.nifty.core.api.builder.BuilderManager;
import net.netcoding.nifty.core.api.plugin.Plugin;
import net.netcoding.nifty.core.api.scheduler.MinecraftScheduler;
import net.netcoding.nifty.core.api.service.ServiceManager;

@SuppressWarnings("unchecked")
public final class Nifty {

	private static final BuilderManager<Plugin> BUILDER_MANAGER = new BuilderManager<>();
	private static final ServiceManager<Plugin> SERVICE_MANAGER = new ServiceManager<>();

	public static BuilderManager<Plugin> getBuilderManager() {
		return BUILDER_MANAGER;
	}

	public static <T extends MinecraftMojangProfile> BungeeHelper<T> getBungeeHelper() {
		return getServiceManager().getProvider(BungeeHelper.class);
	}

	public static EnchantmentDatabase getEnchantmentDatabase() {
		return getServiceManager().getProvider(EnchantmentDatabase.class);
	}

	public static ItemFactory getItemFactory() {
		return getServiceManager().getProvider(ItemFactory.class);
	}

	public static ItemDatabase getItemDatabase() {
		return getServiceManager().getProvider(ItemDatabase.class);
	}

	public static MinecraftLogger getLog() {
		return getServiceManager().getProvider(MinecraftLogger.class);
	}

	public static Messenger getMessenger() {
		return getServiceManager().getProvider(Messenger.class);
	}

	public static MiniBlockDatabase getMiniBlockDatabase() {
		return getServiceManager().getProvider(MiniBlockDatabase.class);
	}

	public static <T extends MinecraftMojangProfile> MinecraftMojangRepository<T> getMojangRepository() {
		return getServiceManager().getProvider(MinecraftMojangRepository.class);
	}

	public static NbtFactory<ItemStack, Block, Entity> getNbtFactory() {
		return getServiceManager().getProvider(NbtFactory.class);
	}

	public static MinecraftPlugin getPlugin() {
		return getServiceManager().getProvider(MinecraftPlugin.class);
	}

	public static PluginManager getPluginManager() {
		return getServiceManager().getProvider(PluginManager.class);
	}

	public static MinecraftScheduler getScheduler() {
		return getServiceManager().getProvider(MinecraftScheduler.class);
	}

	public static Server getServer() {
		return getServiceManager().getProvider(Server.class);
	}

	public static ServiceManager<Plugin> getServiceManager() {
		return SERVICE_MANAGER;
	}

}
