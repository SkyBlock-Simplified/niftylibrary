package net.netcoding.niftycraftbukkit.api.plugin.messaging;

import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftycore.api.plugin.Plugin;
import net.netcoding.niftybukkit._new_.api.plugin.messaging.BungeeHelper;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;
import net.netcoding.niftycraftbukkit.mojang.CraftMojangProfile;
import net.netcoding.niftycraftbukkit.mojang.CraftMojangRepository;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftBungeeHelper extends BungeeHelper<CraftMojangProfile> {

	private static CraftBungeeHelper INSTANCE;

	protected CraftBungeeHelper() {
		super(new CraftNiftyChannelWrapper());
	}

	public static CraftBungeeHelper getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CraftBungeeHelper();

		return INSTANCE;
	}

	@Override
	protected void sendPluginMessage(Plugin plugin, BukkitMojangProfile profile, String channel, byte[] data) {
		CraftMojangProfile craftProfile = CraftMojangRepository.getInstance().searchByPlayer(profile.getOfflinePlayer().getPlayer());
		Player player = craftProfile.getBukkitOfflinePlayer().getPlayer();
		player.sendPluginMessage(Nifty.getServiceManager().getProvider(JavaPlugin.class), channel, data);
	}

}