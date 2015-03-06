package net.netcoding.niftybukkit.signs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.signs.events.SignBreakEvent;
import net.netcoding.niftybukkit.signs.events.SignCreateEvent;
import net.netcoding.niftybukkit.signs.events.SignInteractEvent;
import net.netcoding.niftybukkit.signs.events.SignUpdateEvent;
import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Attachable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

/**
 * Monitor and update signs by tracking sign update packets.
 */
public class SignMonitor extends BukkitListener {

	private final transient ConcurrentHashMap<SignListener, List<String>> listeners = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Location, SignInfo> signLocations = new ConcurrentHashMap<>();
	private transient PacketAdapter adapter;
	private boolean listening = false;

	private static final transient List<Material> GRAVITY_ITEMS = new ArrayList<>(
		Arrays.asList(
			Material.ANVIL, Material.CARPET, Material.DIODE, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON,
			Material.DRAGON_EGG, Material.GRAVEL, Material.GOLD_PLATE, Material.IRON_DOOR, Material.IRON_PLATE, Material.LEVER,
			Material.LADDER, Material.REDSTONE, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF,
			Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON,
			Material.REDSTONE_WIRE, Material.SAND, Material.SIGN_POST, Material.STONE_BUTTON, Material.STRING, Material.TORCH,
			Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WOOD_BUTTON, Material.WOOD_DOOR, Material.WOOD_PLATE
		)
	);

	private static final transient List<BlockFace> RELATIVE_DIRECTIONS = new ArrayList<>(
		Arrays.asList(
			BlockFace.NORTH, BlockFace.EAST,
			BlockFace.SOUTH, BlockFace.WEST
		)
	);

	private static final transient List<Material> SIGN_ITEMS = new ArrayList<>(
		Arrays.asList(Material.SIGN_POST, Material.WALL_SIGN)
	);

	/**
	 * Create new sign monitor instance.
	 * 
	 * @param plugin Java plugin to run it for.
	 */
	public SignMonitor(JavaPlugin plugin) {
		super(plugin);
	}

	/**
	 * Add listener for the given keys.
	 * 
	 * @param listener Listener to send events to.
	 * @param keys     Keys to check when receiving sign update packets.
	 */
	public void addListener(SignListener listener, String... keys) {
		if (listener == null) throw new IllegalArgumentException("The listener must not be null!");
		if (ListUtil.isEmpty(keys) || keys.length == 0) throw new IllegalArgumentException("You cannot listen to signs without at least one key!");

		for (int i = 0; i < keys.length; i++) {
			if (keys[i].length() > 15)
				throw new IllegalArgumentException("The key must not be longer then 15 characters!");
		}

		List<String> newKeys = new ArrayList<>();
		if (ListUtil.notEmpty(this.listeners.get(listener)))
			newKeys.addAll(this.listeners.get(listener));

		for (int i = 0; i < keys.length; i++) {
			if (!newKeys.contains(keys[i]))
				newKeys.add(StringUtil.format("[{0}]", keys[i]));
		}

		this.listeners.put(listener, newKeys);
	}

	/**
	 * Gets a list of signs that would fall if the given block were to break.
	 * 
	 * @param block Block that is going to break.
	 * @return List of signs locations that will break if the block is broken.
	 */
	public static Set<Location> getSignsThatWouldFall(Block block) {
		Set<Location> locations = new HashSet<>();
		if (SIGN_ITEMS.contains(block.getType())) locations.add(block.getLocation());

		for (BlockFace direction : RELATIVE_DIRECTIONS) {
			Block sideBlock = block.getRelative(direction);
			Material sideMaterial = sideBlock.getType();

			if (isAttachable(sideBlock)) {
				if (isAttachedTo(sideBlock, block)) {
					if (SIGN_ITEMS.contains(sideMaterial)) locations.add(sideBlock.getLocation());
					Block sideUpBlock = sideBlock.getRelative(BlockFace.UP);

					if (GRAVITY_ITEMS.contains(sideUpBlock.getType()))
						locations.addAll(getSignsThatWouldFall(sideUpBlock));
				}
			}
		}

		Block upBlock = block.getRelative(BlockFace.UP);
		if (GRAVITY_ITEMS.contains(upBlock.getType()))
			locations.addAll(getSignsThatWouldFall(upBlock));

		return locations;
	}

	/**
	 * Gets if the passed block is an attachable block.
	 * 
	 * @param block Block to check if it can be attached to other blocks.
	 * @return True if attachable, otherwise false.
	 */
	public static boolean isAttachable(Block block) {
		return block.getState().getData() instanceof Attachable;
	}

	/**
	 * Gets if the passed block is attached to the other passed block.
	 * 
	 * @param isThisAttached Block to check if attached to toThisBlock.
	 * @param toThisBlock    Block to check if isThisAttached is attached to.
	 * @return True if attached, otherwise false.
	 */
	public static boolean isAttachedTo(Block isThisAttached, Block toThisBlock) {
		Attachable attachable = (Attachable)isThisAttached.getState().getData();
		return isThisAttached.getRelative(attachable.getAttachedFace()).equals(toThisBlock);
	}

	/**
	 * Gets if the monitor is currently listening for sign update packets.
	 * 
	 * @return True if listening, otherwise false.
	 */
	public boolean isListening() {
		return this.listening;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Set<Location> fallingSigns = getSignsThatWouldFall(block);
		Set<Location> removeSigns = new HashSet<>();

		if (this.isListening()) {
			for (Location location : fallingSigns) {
				if (this.signLocations.containsKey(location)) {
					SignInfo signInfo = this.signLocations.get(location);

					for (SignListener listener : this.listeners.keySet()) {
						List<String> keys = this.listeners.get(listener);

						for (String line : signInfo.getLines()) {
							for (String key : keys) {
								if (line.toLowerCase().contains(key.toLowerCase())) {
									SignBreakEvent breakEvent = new SignBreakEvent(player, signInfo, key);
									listener.onSignBreak(breakEvent);
									Sign sign = (Sign)location.getBlock().getState();
									for (int j = 0; j < 4; j++) sign.setLine(j, signInfo.getModifiedLine(j));

									if (breakEvent.isCancelled()) {
										event.setCancelled(true);
										return;
									} else {
										removeSigns.add(location);
										break;
									}
								}
							}
						}
					}
				}
			}

			for (Location signLocation : removeSigns) this.signLocations.remove(signLocation);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.hasBlock() && (Action.LEFT_CLICK_BLOCK.equals(event.getAction()) || Action.RIGHT_CLICK_BLOCK.equals(event.getAction()))) {
			Block block = event.getClickedBlock();

			if (this.isListening()) {
				if (this.signLocations.containsKey(block.getLocation())) {
					if (SIGN_ITEMS.contains(block.getType())) {
						SignInfo signInfo = this.signLocations.get(block.getLocation());

						for (SignListener listener : this.listeners.keySet()) {
							List<String> keys = this.listeners.get(listener);

							for (String line : signInfo.getLines()) {
								for (String key : keys) {
									if (line.toLowerCase().contains(key.toLowerCase())) {
										SignInteractEvent interactEvent = new SignInteractEvent(event.getPlayer(), signInfo, event.getAction(), key);
										listener.onSignInteract(interactEvent);
										Sign sign = (Sign)block.getState();
										for (int j = 0; j < 4; j++) sign.setLine(j, signInfo.getModifiedLine(j));

										if (interactEvent.isCancelled()) {
											event.setCancelled(true);
											return;
										}
									}
								}
							}
						}
					} else
						this.signLocations.remove(block.getLocation());
				}
			}
		}
	}

	@EventHandler
	public void onPlayerPostLogin(PlayerPostLoginEvent event) {
		this.sendSignUpdate(event.getProfile());
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onSignChange(SignChangeEvent event) {
		Block block = event.getBlock();

		if (this.isListening()) {
			if (!this.signLocations.containsKey(block.getLocation())) {
				Sign sign = (Sign)block.getState();
				for (int i = 0; i < 4; i++) sign.setLine(i, event.getLine(i));
				SignInfo signInfo = new SignInfo(sign);

				for (SignListener listener : this.listeners.keySet()) {
					List<String> keys = this.listeners.get(listener);

					for (String line : signInfo.getLines()) {
						for (String key : keys) {
							if (line.toLowerCase().contains(key.toLowerCase())) {
								SignCreateEvent createEvent = new SignCreateEvent(event.getPlayer(), signInfo, key);
								listener.onSignCreate(createEvent);
								for (int j = 0; j < 4; j++) sign.setLine(j, signInfo.getModifiedLine(j));

								if (createEvent.isCancelled()) {
									event.setCancelled(true);
									return;
								}
							}
						}
					}
				}

				this.signLocations.put(block.getLocation(), signInfo);
			}
		}
	}

	/**
	 * Remove listener from monitor.
	 * 
	 * @param listener Listener to no longer send packet updates to.
	 */
	public void removeListener(SignListener listener) {
		if (listener != null)
			this.listeners.remove(listener);
	}

	/**
	 * Send an update to any signs we are listening to in the vicinity of all players.
	 */
	public void sendSignUpdate() {
		for (MojangProfile profile : NiftyBukkit.getBungeeHelper().getPlayerList())
			this.sendSignUpdate(profile);
	}

	/**
	 * Send an update to any signs we are listening to in the vicinity of the given player.
	 * 
	 * @param player Player to send updates to.
	 */
	public void sendSignUpdate(MojangProfile profile) {
		this.sendSignUpdate(profile, "");
	}

	/**
	 * Send an update to signs with the given key in the vicinity of the given player.
	 * 
	 * @param player Player to send updates to.
	 * @param key    Only signs with this key.
	 */
	public void sendSignUpdate(MojangProfile profile, String key) {
		if (!profile.getOfflinePlayer().isOnline()) return;
		Player player = profile.getOfflinePlayer().getPlayer();

		if (this.isListening()) {
			for (Location location : this.signLocations.keySet()) {
				if (player.getLocation().distance(location) < 16) {
					Material material = location.getBlock().getType();

					if (SIGN_ITEMS.contains(material)) {
						Sign sign = (Sign)location.getBlock().getState();
						SignInfo signInfo = this.signLocations.get(sign.getLocation());

						for (String line : signInfo.getLines()) {
							if (StringUtil.isEmpty(key) || line.toLowerCase().contains(key.toLowerCase())) {
								SignPacket outgoing = new SignPacket(NiftyBukkit.getProtocolManager().createPacket(PacketType.Play.Server.UPDATE_SIGN));
								outgoing.setPosition(new Vector(sign.getX(), sign.getY(), sign.getZ()));

								try {
									outgoing.setLines(signInfo.getLines());
									NiftyBukkit.getProtocolManager().sendServerPacket(player, outgoing.getPacket());
								} catch (Exception ex) {
									this.getLog().console("Unable to send sign update packet!", ex);
								}

								break;
							}
						}
					} else
						this.signLocations.remove(location);
				}
			}
		}
	}

	/**
	 * Start listening for sign update packets.
	 */
	public void start() {
		if (!this.isListening()) {
			this.listening = true;
			NiftyBukkit.getProtocolManager().addPacketListener(this.adapter = new PacketAdapter(this.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Server.UPDATE_SIGN) {
				@Override
				public void onPacketSending(PacketEvent event) {
					PacketContainer signUpdatePacket = event.getPacket();
					SignPacket incoming = new SignPacket(signUpdatePacket);
					Player player = event.getPlayer();
					Location location = new Location(player.getWorld(), incoming.getPosition().getBlockX(), incoming.getPosition().getBlockY(), incoming.getPosition().getBlockZ());
					Block block = location.getBlock();

					if (SIGN_ITEMS.contains(block.getType())) {
						Sign sign = (Sign)block.getState();

						for (SignListener listener : listeners.keySet()) {
							for (int i = 0; i < 4; i++) {
								for (String key : listeners.get(listener)) {
									if (sign.getLine(i).toLowerCase().contains(key.toLowerCase())) {
										SignInfo signInfo = signLocations.get(location);
										if (!signLocations.containsKey(location)) signLocations.put(location, (signInfo = new SignInfo(sign)));
										SignUpdateEvent updateEvent = new SignUpdateEvent(player, signInfo, key);
										listener.onSignUpdate(updateEvent);

										if (!updateEvent.isCancelled() && updateEvent.isModified()) {
											SignPacket outgoing = new SignPacket(signUpdatePacket.shallowClone());
											outgoing.setLines(updateEvent.getModifiedLines());
											event.setPacket(outgoing.getPacket());
										}
									}
								}
							}
						}
					} else
						signLocations.remove(location);
				}
			});
		}
	}

	/**
	 * Stop listening for sign update packets.
	 */
	public void stop() {
		if (this.isListening()) {
			this.listening = false;
			NiftyBukkit.getProtocolManager().removePacketListener(this.adapter);
			this.adapter = null;
		}
	}

}