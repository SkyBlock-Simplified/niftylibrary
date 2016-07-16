package net.netcoding.nifty.craftbukkit.minecraft;

import net.netcoding.nifty.common.minecraft.GameMode;
import net.netcoding.nifty.common.minecraft.OfflinePlayer;
import net.netcoding.nifty.common.minecraft.Server;
import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.common.minecraft.command.ConsoleCommandSource;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryHolder;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.common.reflection.MinecraftProtocol;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.craftbukkit.minecraft.command.CraftConsoleCommandSource;
import net.netcoding.nifty.craftbukkit.minecraft.entity.living.human.CraftPlayer;
import net.netcoding.nifty.craftbukkit.minecraft.region.CraftWorld;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public final class CraftServer implements Server {

	private static final CraftServer INSTANCE = new CraftServer();
	private final InetSocketAddress address;
	private final org.bukkit.Server server;
	private final Version version;
	private final CraftConsoleCommandSource console;
	private final ConcurrentList<World> worlds;

	private CraftServer() {
		this.server = org.bukkit.Bukkit.getServer();
		this.address = new InetSocketAddress(this.getHandle().getIp(), this.getHandle().getPort());
		this.console = new CraftConsoleCommandSource(this.getHandle().getConsoleSender());
		this.version = new Version(MinecraftProtocol.getCurrentVersion(), MinecraftProtocol.getCurrentProtocol());
		this.worlds = this.getHandle().getWorlds().stream().map(CraftWorld::new).collect(Concurrent.toList());
	}

	@Override
	public Inventory createInventory(InventoryHolder holder, int size, String title) {
		return null;
	}

	@Override
	public boolean dispatchCommand(CommandSource source, String command) {
		return false; // TODO
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
		return GameMode.valueOf(this.getHandle().getDefaultGameMode().name());
	}

	public org.bukkit.Server getHandle() {
		return this.server;
	}

	public static CraftServer getInstance() {
		return INSTANCE;
	}

	@Override
	public int getMaxPlayers() {
		return this.getHandle().getMaxPlayers();
	}

	@Override
	public String getMotd() {
		return this.getHandle().getMotd();
	}

	@Override
	public String getName() {
		return this.getHandle().getServerName();
	}

	@Override
	public OfflinePlayer getOfflinePlayer(String name) {
		return new CraftOfflinePlayer(this.getHandle().getOfflinePlayer(name));
	}

	@Override
	public OfflinePlayer getOfflinePlayer(UUID uniqueId) {
		return new CraftOfflinePlayer(this.getHandle().getOfflinePlayer(uniqueId));
	}

	@Override
	public OfflinePlayer[] getOfflinePlayers() {
		return Arrays.stream(this.getHandle().getOfflinePlayers()).map(CraftOfflinePlayer::new).toArray(OfflinePlayer[]::new);
	}

	@Override
	public Collection<? extends Player> getPlayerList() {
		return Collections.unmodifiableSet(this.getHandle().getOnlinePlayers().stream().map(CraftPlayer::new).collect(Collectors.toSet()));
	}

	@Override
	public Version getVersion() {
		return this.version;
	}

	@Override
	public int hashCode() {
		return this.getHandle().hashCode();
	}

	@Override
	public boolean isOnline() {
		return this.getHandle().getOnlineMode();
	}

	@Override
	public List<World> getWorlds() {
		return this.worlds;
	}

}