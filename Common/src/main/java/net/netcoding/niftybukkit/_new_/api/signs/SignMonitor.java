package net.netcoding.niftybukkit._new_.api.signs;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.netcoding.niftybukkit.Nifty;
import net.netcoding.niftybukkit._new_.api.BukkitListener;
import net.netcoding.niftybukkit._new_.api.Event;
import net.netcoding.niftybukkit._new_.api.inventory.InventoryWorkaround;
import net.netcoding.niftybukkit._new_.api.nbt.NbtCompound;
import net.netcoding.niftybukkit._new_.api.plugin.MinecraftPlugin;
import net.netcoding.niftybukkit._new_.api.signs.events.SignBreakEvent;
import net.netcoding.niftybukkit._new_.api.signs.events.SignCreateEvent;
import net.netcoding.niftybukkit._new_.api.signs.events.SignInteractEvent;
import net.netcoding.niftybukkit._new_.api.signs.events.SignUpdateEvent;
import net.netcoding.niftybukkit._new_.minecraft.GameMode;
import net.netcoding.niftybukkit._new_.minecraft.block.Action;
import net.netcoding.niftybukkit._new_.minecraft.block.Block;
import net.netcoding.niftybukkit._new_.minecraft.block.BlockFace;
import net.netcoding.niftybukkit._new_.minecraft.entity.living.Player;
import net.netcoding.niftybukkit._new_.minecraft.event.profile.ProfileJoinEvent;
import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.niftybukkit._new_.minecraft.material.Material;
import net.netcoding.niftybukkit._new_.minecraft.region.Location;
import net.netcoding.niftybukkit._new_.mojang.BukkitMojangProfile;
import net.netcoding.niftybukkit._new_.reflection.MinecraftPackage;
import net.netcoding.niftybukkit._new_.reflection.MinecraftProtocol;
import net.netcoding.niftycore.reflection.Reflection;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentList;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import net.netcoding.niftycore.util.misc.Vector;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Attachable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Monitor and update signs by tracking sign update packets.
 */
public class SignMonitor {

	private static final Reflection NMS_MAP_CHUNK = new Reflection("PacketPlayOutMapChunk", MinecraftPackage.MINECRAFT_SERVER);
	private static final ConcurrentMap<UUID, ConcurrentList<NbtCompound>> CHUNK_ADJUSTMENT = new ConcurrentMap<>();
	static final boolean IS_PRE_1_9_3 = MinecraftProtocol.getCurrentProtocol() < MinecraftProtocol.v1_9_3_pre1.getProtocol();
	static final boolean IS_POST_1_9_3 = !IS_PRE_1_9_3;
	private final transient ConcurrentMap<SignListener, List<String>> listeners = new ConcurrentMap<>();
	private final ConcurrentMap<Location, SignInfo> signLocations = new ConcurrentMap<>();
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

	static {
		if (IS_POST_1_9_3) {
			Nifty.getProtocolManager().addPacketListener(new PacketAdapter(Nifty.getPlugin(), PacketType.Play.Server.MAP_CHUNK) {
				@SuppressWarnings("unchecked")
				@Override
				public void onPacketSending(PacketEvent event) {
					List<Object> handles = (List<Object>) NMS_MAP_CHUNK.getValue(List.class, event.getPacket().getHandle());

					for (Object handle : handles) {
						NbtCompound compound = Nifty.getNbtFactory().fromCompound(handle);
						boolean isSign;

						if (compound.containsKey("id"))
							isSign = "Sign".equals(compound.get("id"));
						else
							isSign = compound.containsKey("Text1") && compound.containsKey("Text2") && compound.containsKey("Text3") && compound.containsKey("Text4");

						if (isSign) {
							UUID uniqueId = event.getPlayer().getUniqueId();
							ConcurrentList<NbtCompound> list;

							if (!CHUNK_ADJUSTMENT.containsKey(uniqueId))
								CHUNK_ADJUSTMENT.put(uniqueId, list = new ConcurrentList<>());
							else
								list = CHUNK_ADJUSTMENT.get(uniqueId);

							list.add(compound);
						}
					}
				}
			});
		}
	}

	/**
	 * Create new sign monitor instance.
	 *
	 * @param plugin Java plugin to run it for.
	 */
	public SignMonitor(MinecraftPlugin plugin) {
		this.signListener = new BukkitSignListener(plugin);
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

			if (isAttachedTo(sideBlock, block)) {
				if (SIGN_ITEMS.contains(sideMaterial))
					locations.add(sideBlock.getLocation());

				Block sideUpBlock = sideBlock.getRelative(BlockFace.UP);

				if (GRAVITY_ITEMS.contains(sideUpBlock.getType()))
					locations.addAll(getSignsThatWouldFall(sideUpBlock));
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
		if (isAttachable(isThisAttached)) {
			Attachable attachable = (Attachable)isThisAttached.getState().getData();
			return isThisAttached.getRelative(attachable.getAttachedFace()).equals(toThisBlock);
		}

		return false;
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
		Nifty.getBungeeHelper().getPlayerList().forEach(this::sendSignUpdate);
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
		for (BukkitMojangProfile profile : Nifty.getBungeeHelper().getPlayerList())
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
			this.signLocations.keySet().stream().filter(location -> location.getWorld().equals(player.getWorld())).filter(location -> player.getLocation().distance(location) < 16).forEach(location -> {
				Material material = location.getBlock().getType();

				if (SIGN_ITEMS.contains(material)) {
					Sign sign = (Sign) location.getBlock().getState();
					SignInfo signInfo = this.signLocations.get(sign.getLocation());
					this.sendSignUpdate(player, key, signInfo);
				} else this.signLocations.remove(location);
			});
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
				SignPacket outgoing = new SignPacket(Nifty.getProtocolManager().createPacket(IS_PRE_1_9_3 ? PacketType.Play.Server.UPDATE_SIGN : PacketType.Play.Server.TILE_ENTITY_DATA));
				outgoing.setPosition(new Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

				if (IS_POST_1_9_3)
					outgoing.getPacket().getIntegers().write(0, 9);

				try {
					outgoing.setLines(signInfo.getLines());
					Nifty.getProtocolManager().sendServerPacket(player, outgoing.getPacket());
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

			Nifty.getProtocolManager().addPacketListener(this.adapter = new PacketAdapter(this.signListener.getPlugin(), ListenerPriority.HIGH, (IS_PRE_1_9_3 ? PacketType.Play.Server.UPDATE_SIGN : PacketType.Play.Server.TILE_ENTITY_DATA)) {
				@Override
				public void onPacketSending(PacketEvent event) {
					PacketContainer signUpdatePacket = event.getPacket();
					SignPacket incoming = new SignPacket(signUpdatePacket);
					BukkitMojangProfile profile = Nifty.getMojangRepository().searchByPlayer(event.getPlayer());
					Location location = new Location(profile.getOfflinePlayer().getPlayer().getWorld(), incoming.getPosition().getBlockX(), incoming.getPosition().getBlockY(), incoming.getPosition().getBlockZ());
					Block block = location.getBlock();

					if (SIGN_ITEMS.contains(block.getType())) {
						Sign sign = (Sign)block.getState();

						for (Map.Entry<SignListener, List<String>> entry : SignMonitor.this.listeners.entrySet()) {
							for (String line : sign.getLines()) {
								for (String key : entry.getValue()) {
									if (line.toLowerCase().contains(key)) {
										SignInfo signInfo = SignMonitor.this.signLocations.get(location);

										if (!SignMonitor.this.signLocations.containsKey(location))
											SignMonitor.this.signLocations.put(location, signInfo = new SignInfo(sign));

										SignUpdateEvent updateEvent = new SignUpdateEvent(profile, signInfo, key);
										entry.getKey().onSignUpdate(updateEvent);

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
						SignMonitor.this.signLocations.remove(location);
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
			Nifty.getProtocolManager().removePacketListener(this.adapter);
			this.adapter = null;
		}
	}

	private class BukkitSignListener extends BukkitListener {

		public BukkitSignListener(MinecraftPlugin plugin) {
			super(plugin);
		}

		@Event(priority = Event.Priority.HIGHEST)
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

		@Event(priority = Event.Priority.HIGH)
		public void onBlockBreak(BlockBreakEvent event) {
			BukkitMojangProfile profile = Nifty.getMojangRepository().searchByPlayer(event.getPlayer());
			Block block = event.getBlock();
			Set<Location> fallingSigns = getSignsThatWouldFall(block);
			Set<Location> removeSigns = new HashSet<>();

			if (SignMonitor.this.isListening()) {
				for (Location location : fallingSigns) {
					if (SignMonitor.this.signLocations.containsKey(location)) {
						SignInfo signInfo = SignMonitor.this.signLocations.get(location);

						for (Map.Entry<SignListener, List<String>> entry : SignMonitor.this.listeners.entrySet()) {
							for (String line : signInfo.getLines()) {
								for (String key : entry.getValue()) {
									if (line.toLowerCase().contains(key)) {
										SignBreakEvent breakEvent = new SignBreakEvent(profile, signInfo, key);
										entry.getKey().onSignBreak(breakEvent);

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

				removeSigns.forEach(SignMonitor.this.signLocations::remove);
			}
		}

		@Event(priority = Event.Priority.HIGHEST)
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

		@Event(priority = Event.Priority.HIGHEST)
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

		@Event(ignoreCancelled = true)
		public void onPlayerInteract(PlayerInteractEvent event) {
			if (!(GameMode.CREATIVE == event.getPlayer().getGameMode() && Action.LEFT_CLICK_BLOCK == event.getAction())) {
				if (event.hasBlock() && (Action.LEFT_CLICK_BLOCK == event.getAction() || Action.RIGHT_CLICK_BLOCK == event.getAction())) {
					Block block = event.getClickedBlock();

					if (SignMonitor.this.isListening()) {
						if (SignMonitor.this.signLocations.containsKey(block.getLocation())) {
							if (SIGN_ITEMS.contains(block.getType())) {
								SignInfo signInfo = SignMonitor.this.signLocations.get(block.getLocation());

								for (Map.Entry<SignListener, List<String>> entry : SignMonitor.this.listeners.entrySet()) {
									for (String line : signInfo.getLines()) {
										for (String key : entry.getValue()) {
											if (line.toLowerCase().contains(key)) {
												BukkitMojangProfile profile = Nifty.getMojangRepository().searchByPlayer(event.getPlayer());
												SignInteractEvent interactEvent = new SignInteractEvent(profile, signInfo, event.getAction(), key);
												entry.getKey().onSignInteract(interactEvent);
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

		@Event
		public void onProfileJoin(ProfileJoinEvent event) {
			SignMonitor.this.sendSignUpdate(event.getProfile());

			if (IS_POST_1_9_3) {
				Player player = event.getProfile().getOfflinePlayer().getPlayer();
				List<NbtCompound> compounds = CHUNK_ADJUSTMENT.get(player.getUniqueId());

				for (NbtCompound compound : compounds) {
					SignInfo signInfo = SignInfo.fromCompound(player.getWorld(), compound);

					for (Map.Entry<SignListener, List<String>> entry : SignMonitor.this.listeners.entrySet()) {
						for (String line : signInfo.getLines()) {
							for (String key : entry.getValue()) {
								if (line.toLowerCase().contains(key)) {
									SignMonitor.this.sendSignUpdate(player, key, signInfo);
								}
							}
						}
					}
				}
			}
		}

		@Event(priority = Event.Priority.LOW)
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
									BukkitMojangProfile profile = Nifty.getMojangRepository().searchByPlayer(player);
									SignCreateEvent createEvent = new SignCreateEvent(profile, signInfo, key);
									listener.onSignCreate(createEvent);

									if (createEvent.isCancelled()) {
										event.setCancelled(true);
										block.setType(Material.AIR);

										if (GameMode.CREATIVE != player.getGameMode())
											InventoryWorkaround.addItems(player.getInventory(), ItemStack.of(Material.SIGN));

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