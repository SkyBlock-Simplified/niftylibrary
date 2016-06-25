package net.netcoding.nifty.common;

import net.netcoding.nifty.common.api.MinecraftLogger;
import net.netcoding.nifty.common.api.inventory.item.ItemDatabase;
import net.netcoding.nifty.common.api.inventory.item.MiniBlockDatabase;
import net.netcoding.nifty.common.api.inventory.item.enchantment.EnchantmentDatabase;
import net.netcoding.nifty.common.api.nbt.NbtFactory;
import net.netcoding.nifty.common.api.plugin.IMinecraftPlugin;
import net.netcoding.nifty.common.api.plugin.messaging.BungeeHelper;
import net.netcoding.nifty.common.api.plugin.messaging.Messenger;
import net.netcoding.nifty.common.minecraft.BukkitServer;
import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemFactory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common.mojang.BukkitMojangRepository;
import net.netcoding.nifty.core.api.builder.BuilderManager;
import net.netcoding.nifty.core.api.plugin.Plugin;
import net.netcoding.nifty.core.api.scheduler.MinecraftScheduler;
import net.netcoding.nifty.core.api.service.ServiceManager;

@SuppressWarnings("unchecked")
public final class Nifty {

	private static final BuilderManager<Plugin> BUILDER_MANAGER = new BuilderManager<>();
	private static final ServiceManager<Plugin> SERVICE_MANAGER = new ServiceManager<>();
	// TODO: FIREWORK BUILDER

	public static BuilderManager<Plugin> getBuilderManager() {
		return BUILDER_MANAGER;
	}

	public static <T extends BukkitMojangProfile> BungeeHelper<T> getBungeeHelper() {
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

	public static <T extends BukkitMojangProfile> BukkitMojangRepository<T> getMojangRepository() {
		return getServiceManager().getProvider(BukkitMojangRepository.class);
	}

	public static NbtFactory<ItemStack, Block> getNbtFactory() {
		return getServiceManager().getProvider(NbtFactory.class);
	}

	public static IMinecraftPlugin getPlugin() {
		return getServiceManager().getProvider(IMinecraftPlugin.class);
	}

	public static MinecraftScheduler getScheduler() {
		return getServiceManager().getProvider(MinecraftScheduler.class);
	}

	public static BukkitServer getServer() {
		return getServiceManager().getProvider(BukkitServer.class);
	}

	public static ServiceManager<Plugin> getServiceManager() {
		return SERVICE_MANAGER;
	}

}
