package net.netcoding.nifty.common;

import net.netcoding.nifty.common._new_.api.BukkitLogger;
import net.netcoding.nifty.common._new_.api.inventory.item.ItemDatabase;
import net.netcoding.nifty.common._new_.api.inventory.item.MiniBlockDatabase;
import net.netcoding.nifty.common._new_.api.inventory.item.enchantment.EnchantmentDatabase;
import net.netcoding.nifty.common._new_.api.nbt.NbtFactory;
import net.netcoding.nifty.common._new_.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common._new_.api.plugin.messaging.BungeeHelper;
import net.netcoding.nifty.common._new_.api.plugin.messaging.Messenger;
import net.netcoding.nifty.common._new_.minecraft.BukkitServer;
import net.netcoding.nifty.common._new_.minecraft.block.Block;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemFactory;
import net.netcoding.nifty.common._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangProfile;
import net.netcoding.nifty.common._new_.mojang.BukkitMojangRepository;
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

	public static BukkitLogger getLog() {
		return getServiceManager().getProvider(BukkitLogger.class);
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

	public static net.milkbowl.vault.permission.Permission getPermissions() {
		return getServiceManager().getProvider(net.milkbowl.vault.permission.Permission.class);
	}

	public static MinecraftPlugin getPlugin() {
		return getServiceManager().getProvider(MinecraftPlugin.class);
	}

	public static com.comphenix.protocol.ProtocolManager getProtocolManager() {
		return getServiceManager().getProvider(com.comphenix.protocol.ProtocolManager.class);
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
