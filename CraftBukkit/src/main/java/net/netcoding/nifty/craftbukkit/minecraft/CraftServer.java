package net.netcoding.nifty.craftbukkit.minecraft;

import net.netcoding.nifty.common.minecraft.BukkitServer;
import net.netcoding.nifty.common.minecraft.GameMode;
import net.netcoding.nifty.common.minecraft.OfflinePlayer;
import net.netcoding.nifty.common.minecraft.command.source.ConsoleCommandSource;
import net.netcoding.nifty.common.minecraft.entity.living.Player;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftPlayer;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftWorld;
import net.netcoding.nifty.craftbukkit.minecraft.source.command.CraftConsoleCommandSource;
import org.bukkit.Bukkit;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class CraftServer implements BukkitServer {

	private static CraftServer INSTANCE;
	private final InetSocketAddress address;
	private final Version version;
	private final CraftConsoleCommandSource console;
	private ConcurrentList<World> worlds = new ConcurrentList<>();

	private CraftServer() {
		this.address = new InetSocketAddress(Bukkit.getIp(), Bukkit.getPort());
		this.version = new Version(MinecraftProtocol.getCurrentVersion(), MinecraftProtocol.getCurrentProtocol());
		this.console = new CraftConsoleCommandSource(this);
		Bukkit.getWorlds().stream().forEach(world -> this.worlds.add(new CraftWorld(world)));
	}

	@Override
	public Inventory createInventory(InventoryHolder holder, int size, String title) {
		return null;
	}

	@Override
	public InetSocketAddress getAddress() {
		return this.address;
	}

	@Override
	public ConsoleCommandSource getConsoleSource() {
		return this.console;
	}

	@Override
	public GameMode getDefaultGameMode() {
		return GameMode.valueOf(org.bukkit.Bukkit.getDefaultGameMode().name());
	}

	public static CraftServer getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CraftServer();

		return INSTANCE;
	}

	@Override
	public int getMaxPlayers() {
		return Bukkit.getMaxPlayers();
	}

	@Override
	public String getMotd() {
		return Bukkit.getMotd();
	}

	@Override
	public String getName() {
		return Bukkit.getServerName();
	}

	@Override
	public Player getPlayer(String name) {
		for (Player player : this.getPlayerList()) {
			if (player.getName().equalsIgnoreCase(name))
				return player;
		}

		return null;
	}

	@Override
	public Player getPlayer(UUID uniqueId) {
		for (Player player : this.getPlayerList()) {
			if (player.getUniqueId().equals(uniqueId))
				return player;
		}

		return null;
	}

	@Override
	public OfflinePlayer getOfflinePlayer(String name) {
		return null; // TODO
	}

	@Override
	public OfflinePlayer getOfflinePlayer(UUID uniqueId) {
		return null; // TODO
	}

	@Override
	public int getPlayerCount() {
		return ListUtil.sizeOf(this.getPlayerList());
	}

	@Override
	public Collection<? extends Player> getPlayerList() {
		return Collections.unmodifiableSet(Bukkit.getOnlinePlayers().stream().map(CraftPlayer::new).collect(Collectors.toSet()));
	}

	@Override
	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		return 31 * (this.getAddress() == null ? super.hashCode() : this.getAddress().hashCode());
	}

	@Override
	public boolean isOnlineMode() {
		return Bukkit.getOnlineMode();
	}

	@Override
	public List<World> getWorlds() {
		return this.worlds;
	}

}