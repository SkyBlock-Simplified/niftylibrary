package net.netcoding.niftybukkit.minecraft.signs;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.netcoding.niftybukkit.NiftyBukkit;
import net.netcoding.niftybukkit.minecraft.BukkitListener;
import net.netcoding.niftybukkit.minecraft.events.PlayerPostLoginEvent;
import net.netcoding.niftybukkit.minecraft.inventory.InventoryWorkaround;
import net.netcoding.niftybukkit.minecraft.signs.events.SignBreakEvent;
import net.netcoding.niftybukkit.minecraft.signs.events.SignCreateEvent;
import net.netcoding.niftybukkit.minecraft.signs.events.SignInteractEvent;
import net.netcoding.niftybukkit.minecraft.signs.events.SignUpdateEvent;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Attachable;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Monitor and update signs by tracking sign update packets.
 */
public class SignMonitor {

	private final transient ConcurrentHashMap<SignListener, List<String>> listeners = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Location, SignInfo> signLocations = new ConcurrentHashMap<>();
	private final transient BukkitSignListener signListener;
	private transient PacketAdapter adapter;
	private transient boolean listening = false;

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
		signListener = new BukkitSignListener(plugin);
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

		for (String key : keys) {
			if (key.length() > 15)
				throw new IllegalArgumentException("The key must not be longer then 15 characters!");
		}

		List<String> newKeys = new ArrayList<>();
		if (ListUtil.notEmpty(this.listeners.get(listener)))
			newKeys.addAll(this.listeners.get(listener));

		for (String key : keys) {
			if (!newKeys.contains(key))
				newKeys.add(StringUtil.format("[{0}]", key.toLowerCase()));
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

		if (SIGN_ITEMS.contains(block.getType()))
			locations.add(block.getLocation());

		for (BlockFace direction : RELATIVE_DIRECTIONS) {
			Block sideBlock = block.getRelative(direction);
			Material sideMaterial = sideBlock.getType();

			if (isAttachable(sideBlock)) {
				if (isAttachedTo(sideBlock, block)) {
					if (SIGN_ITEMS.contains(sideMaterial))
						locations.add(sideBlock.getLocation());

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
		for (BukkitMojangProfile profile : NiftyBukkit.getBungeeHelper().getPlayerList())
			this.sendSignUpdate(profile);
	}

	/**
	 * Send an update to any signs we are listening to in the vicinity of the given player.
	 *
	 * @param profile Player to send updates to.
	 */
	public void sendSignUpdate(BukkitMojangProfile profile) {
		this.sendSignUpdate(profile, "");
	}

	/**
	 * Send an update to any signs we are listening to in the vicinity of the given player.
	 *
	 * @param key Only signs containing this key.
	 */
	public void sendSignUpdate(String key) {
		for (BukkitMojangProfile profile : NiftyBukkit.getBungeeHelper().getPlayerList())
			this.sendSignUpdate(profile, key);
	}

	/**
	 * Send an update to signs with the given key in the vicinity of the given player.
	 *
	 * @param profile Profile to send updates to.
	 * @param key     Only signs containing this key.
	 */
	public void sendSignUpdate(BukkitMojangProfile profile, String key) {
		if (!profile.isOnlineLocally()) return;
		Player player = profile.getOfflinePlayer().getPlayer();

		if (this.isListening()) {
			for (Location location : this.signLocations.keySet()) {
				if (location.getWorld().equals(player.getWorld())) {
					if (player.getLocation().distance(location) < 16) {
						Material material = location.getBlock().getType();

						if (SIGN_ITEMS.contains(material)) {
							Sign sign = (Sign)location.getBlock().getState();
							SignInfo signInfo = this.signLocations.get(sign.getLocation());
							this.sendSignUpdate(player, key, signInfo);
						} else
							this.signLocations.remove(location);
					}
				}
			}
		}
	}

	/**
	 *
	 * @param player   Player the outgoing packet is going to.
	 * @param key      Only signs containing this key.
	 * @param signInfo The private sign information.
	 */
	private void sendSignUpdate(Player player, String key, SignInfo signInfo) {
		Location location = signInfo.getLocation();

		for (String line : signInfo.getLines()) {
			if (StringUtil.isEmpty(key) || line.toLowerCase().contains(key.toLowerCase())) {
				SignPacket outgoing = new SignPacket(NiftyBukkit.getProtocolManager().createPacket(PacketType.Play.Server.UPDATE_SIGN));
				outgoing.setPosition(new Vector(location.getX(), location.getY(), location.getZ()));

				try {
					outgoing.setLines(signInfo.getLines());
					NiftyBukkit.getProtocolManager().sendServerPacket(player, outgoing.getPacket());
				} catch (Exception ex) {
					this.signListener.getLog().console("Unable to send sign update packet!", ex);
				}

				break;
			}
		}
	}

	/**
	 * Start listening for sign update packets.
	 */
	public void start() {
		if (!this.isListening()) {
			this.listening = true;
			NiftyBukkit.getProtocolManager().addPacketListener(this.adapter = new PacketAdapter(this.signListener.getPlugin(), ListenerPriority.HIGH, PacketType.Play.Server.UPDATE_SIGN) {
				@Override
				public void onPacketSending(PacketEvent event) {
					PacketContainer signUpdatePacket = event.getPacket();
					SignPacket incoming = new SignPacket(signUpdatePacket);
					BukkitMojangProfile bukkitProfile = NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer());
					Location location = new Location(bukkitProfile.getOfflinePlayer().getPlayer().getWorld(), incoming.getPosition().getBlockX(), incoming.getPosition().getBlockY(), incoming.getPosition().getBlockZ());
					Block block = location.getBlock();

					if (SIGN_ITEMS.contains(block.getType())) {
						Sign sign = (Sign)block.getState();

						for (SignListener listener : listeners.keySet()) {
							for (int i = 0; i < 4; i++) {
								for (String key : listeners.get(listener)) {
									if (sign.getLine(i).toLowerCase().contains(key)) {
										SignInfo signInfo = signLocations.get(location);
										if (!signLocations.containsKey(location)) signLocations.put(location, (signInfo = new SignInfo(sign)));
										SignUpdateEvent updateEvent = new SignUpdateEvent(bukkitProfile, signInfo, key);
										listener.onSignUpdate(updateEvent);

										if (!updateEvent.isCancelled() && updateEvent.isModified()) {
											SignPacket outgoing = new SignPacket(signUpdatePacket.shallowClone());
											outgoing.setLines(updateEvent.getModifiedLines(true));
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

	private class BukkitSignListener extends BukkitListener {

		public BukkitSignListener(JavaPlugin plugin) {
			super(plugin);
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onEntityBreakEvent(EntityExplodeEvent event) {
			if (EntityType.PLAYER != event.getEntityType()) {
				for (Block block : event.blockList()) {
					Set<Location> fallingSigns = getSignsThatWouldFall(block);

					for (Location fallingSign : fallingSigns) {
						if (SignMonitor.this.signLocations.containsKey(fallingSign)) {
							event.setCancelled(true);
							return;
						}
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGH)
		public void onBlockBreak(BlockBreakEvent event) {
			BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer());
			Block block = event.getBlock();
			Set<Location> fallingSigns = getSignsThatWouldFall(block);
			Set<Location> removeSigns = new HashSet<>();

			if (SignMonitor.this.isListening()) {
				for (Location location : fallingSigns) {
					if (SignMonitor.this.signLocations.containsKey(location)) {
						SignInfo signInfo = SignMonitor.this.signLocations.get(location);

						for (SignListener listener : SignMonitor.this.listeners.keySet()) {
							List<String> keys = SignMonitor.this.listeners.get(listener);

							for (String line : signInfo.getLines()) {
								for (String key : keys) {
									if (line.toLowerCase().contains(key)) {
										SignBreakEvent breakEvent = new SignBreakEvent(profile, signInfo, key);
										listener.onSignBreak(breakEvent);

										if (breakEvent.isCancelled()) {
											event.setCancelled(true);
											Sign sign = (Sign)location.getBlock().getState();

											for (int j = 0; j < 4; j++)
												sign.setLine(j, signInfo.getLine(j));

											return;
										}

										removeSigns.add(location);
										break;
									}
								}
							}
						}
					}
				}

				for (Location signLocation : removeSigns)
					SignMonitor.this.signLocations.remove(signLocation);
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockPistonExtend(BlockPistonExtendEvent event) {
			for (Block block : event.getBlocks()) {
				Set<Location> fallingSigns = getSignsThatWouldFall(block);

				for (Location fallingSign : fallingSigns) {
					if (SignMonitor.this.signLocations.containsKey(fallingSign)) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onBlockPistonRetract(BlockPistonRetractEvent event) {
			for (Block block : event.getBlocks()) {
				Set<Location> fallingSigns = getSignsThatWouldFall(block);

				for (Location fallingSign : fallingSigns) {
					if (SignMonitor.this.signLocations.containsKey(fallingSign)) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}

		@EventHandler(ignoreCancelled = true)
		public void onPlayerInteract(PlayerInteractEvent event) {
			if (!(GameMode.CREATIVE == event.getPlayer().getGameMode() && Action.LEFT_CLICK_BLOCK == event.getAction())) {
				if (event.hasBlock() && (Action.LEFT_CLICK_BLOCK == event.getAction() || Action.RIGHT_CLICK_BLOCK == event.getAction())) {
					Block block = event.getClickedBlock();

					if (SignMonitor.this.isListening()) {
						if (SignMonitor.this.signLocations.containsKey(block.getLocation())) {
							if (SIGN_ITEMS.contains(block.getType())) {
								SignInfo signInfo = SignMonitor.this.signLocations.get(block.getLocation());

								for (SignListener listener : SignMonitor.this.listeners.keySet()) {
									List<String> keys = SignMonitor.this.listeners.get(listener);

									for (String line : signInfo.getLines()) {
										for (String key : keys) {
											if (line.toLowerCase().contains(key)) {
												BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer(event.getPlayer());
												SignInteractEvent interactEvent = new SignInteractEvent(profile, signInfo, event.getAction(), key);
												listener.onSignInteract(interactEvent);
												Sign sign = (Sign)block.getState();

												for (int j = 0; j < 4; j++)
													sign.setLine(j, signInfo.getLine(j));

												if (interactEvent.isCancelled()) {
													event.setCancelled(true);
													return;
												} else
													SignMonitor.this.sendSignUpdate(profile.getOfflinePlayer().getPlayer(), key, signInfo);
											}
										}
									}
								}
							} else
								SignMonitor.this.signLocations.remove(block.getLocation());
						}
					}
				}
			}
		}

		@EventHandler
		public void onPlayerPostLogin(PlayerPostLoginEvent event) {
			SignMonitor.this.sendSignUpdate(event.getProfile());
		}

		@EventHandler(priority = EventPriority.LOW)
		public void onSignChange(SignChangeEvent event) {
			Block block = event.getBlock();

			if (SignMonitor.this.isListening()) {
				if (!SignMonitor.this.signLocations.containsKey(block.getLocation())) {
					Sign sign = (Sign)block.getState();

					for (int i = 0; i < 4; i++)
						sign.setLine(i, event.getLine(i));

					SignInfo signInfo = new SignInfo(sign);

					for (SignListener listener : SignMonitor.this.listeners.keySet()) {
						List<String> keys = SignMonitor.this.listeners.get(listener);

						for (String line : signInfo.getLines()) {
							for (String key : keys) {
								if (line.toLowerCase().contains(key)) {
									Player player = event.getPlayer();
									BukkitMojangProfile profile = NiftyBukkit.getMojangRepository().searchByPlayer(player);
									SignCreateEvent createEvent = new SignCreateEvent(profile, signInfo, key);
									listener.onSignCreate(createEvent);

									if (createEvent.isCancelled()) {
										event.setCancelled(true);
										block.setType(Material.AIR);

										if (GameMode.CREATIVE != player.getGameMode())
											InventoryWorkaround.addItems(player.getInventory(), new ItemStack(Material.SIGN));

										return;
									} else {
										for (int j = 0; j < 4; j++)
											sign.setLine(j, signInfo.getLine(j));

										SignMonitor.this.signLocations.put(block.getLocation(), signInfo);
									}
								}
							}
						}
					}
				}
			}
		}

	}

}