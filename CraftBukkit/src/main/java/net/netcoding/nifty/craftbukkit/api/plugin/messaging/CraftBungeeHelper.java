package net.netcoding.nifty.craftbukkit.api.plugin.messaging;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.common.api.plugin.messaging.BungeeHelper;
import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;
import net.netcoding.nifty.craftbukkit.mojang.CraftMojangProfile;
import net.netcoding.nifty.craftbukkit.mojang.CraftMojangRepository;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftBungeeHelper extends BungeeHelper<CraftMojangProfile> {

	private static final CraftBungeeHelper INSTANCE = new CraftBungeeHelper();

	protected CraftBungeeHelper() {
		super(new CraftNiftyChannelWrapper());
	}

	public static CraftBungeeHelper getInstance() {
		return INSTANCE;
	}

	@Override
	protected void handleNiftyHook(String channel, byte[] message) {
		this.handleNifty(channel, message);
	}

	@Override
	protected void sendPluginMessage(MinecraftPlugin plugin, MinecraftMojangProfile profile, String channel, byte[] data) {
		CraftMojangProfile craftProfile = CraftMojangRepository.getInstance().searchByPlayer(profile.getOfflinePlayer().getPlayer());
		Player player = craftProfile.getBukkitOfflinePlayer().getPlayer();
		player.sendPluginMessage(Nifty.getServiceManager().getProvider(JavaPlugin.class), channel, data);
	}

}