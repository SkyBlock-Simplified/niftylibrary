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
import net.netcoding.niftybukkit.signs.events.SignBreakEvent;
import net.netcoding.niftybukkit.signs.events.SignCreateEvent;
import net.netcoding.niftybukkit.signs.events.SignInteractEvent;
import net.netcoding.niftybukkit.signs.events.SignUpdateEvent;
import net.netcoding.niftybukkit.utilities.ConcurrentSet;

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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.Button;
import org.bukkit.material.Ladder;
import org.bukkit.material.Lever;
import org.bukkit.material.Torch;
import org.bukkit.material.TrapDoor;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;

public class SignMonitor extends BukkitListener {

	private final transient ConcurrentHashMap<SignListener, List<String>> listeners = new ConcurrentHashMap<>();
	private final transient ConcurrentSet<Location> signLocations = new ConcurrentSet<>();
	private transient boolean listening = false;
	private transient PacketAdapter adapter;

	private static final transient List<Material> gravityItems = new ArrayList<>(
		Arrays.asList(
			Material.ANVIL, Material.CARPET, Material.DIODE, Material.DIODE_BLOCK_OFF, Material.DIODE_BLOCK_ON,
			Material.DRAGON_EGG, Material.GRAVEL, Material.GOLD_PLATE, Material.IRON_PLATE, Material.LEVER,
			Material.LADDER, Material.REDSTONE, Material.REDSTONE_COMPARATOR, Material.REDSTONE_COMPARATOR_OFF,
			Material.REDSTONE_COMPARATOR_ON, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON,
			Material.REDSTONE_WIRE, Material.SAND, Material.SIGN_POST, Material.STRING, Material.TORCH,
			Material.TRIPWIRE, Material.TRIPWIRE_HOOK, Material.WOOD_PLATE
		)
	);

	private static final transient List<Material> attachableItems = new ArrayList<>(
		Arrays.asList(
			Material.LADDER, Material.LEVER, Material.REDSTONE_TORCH_OFF, Material.REDSTONE_TORCH_ON,
			Material.STONE_BUTTON, Material.TORCH, Material.TRAP_DOOR, Material.WALL_SIGN,
			Material.WOOD_BUTTON
		)
	);

	private static final transient List<Material> signItems = new ArrayList<>(
		Arrays.asList(Material.SIGN_POST, Material.WALL_SIGN)
	);

	private static final transient List<BlockFace> relativeDirections = new ArrayList<>(
		Arrays.asList(
			BlockFace.NORTH, BlockFace.EAST,
			BlockFace.SOUTH, BlockFace.WEST
		)
	);

	public SignMonitor(JavaPlugin plugin) {
		super(plugin);
	}

	public void addListener(SignListener listener, String... keys) {
		if (listener == null) throw new IllegalArgumentException("The listener must not be null!");
		if (keys == null || keys.length == 0) throw new IllegalArgumentException("You cannot listen to signs without at least one key!");

		for (int i = 0; i < keys.length; i++) {
			if (keys[i].length() > 15)
				throw new IllegalArgumentException("The key must not be longer then 15 characters!");
		}

		List<String> keyList = new ArrayList<>();
		for (int i = 0; i < keys.length; i++) keyList.add("[" + keys[i] + "]");
		this.listeners.put(listener, keyList);
	}

	public static boolean isAttachedTo(Block isThisAttached, Block toThisBlock) {
		switch (isThisAttached.getType()) {
		case LADDER:
			Ladder ladder = (Ladder)isThisAttached.getState().getData();
			return isThisAttached.getRelative(ladder.getAttachedFace()).equals(toThisBlock);
		case LEVER:
			Lever lever = (Lever)isThisAttached.getState().getData();
			return isThisAttached.getRelative(lever.getAttachedFace()).equals(toThisBlock);
		case REDSTONE_TORCH_OFF:
		case REDSTONE_TORCH_ON:
		case TORCH:
			Torch torch = (Torch)isThisAttached.getState().getData();
			return isThisAttached.getRelative(torch.getAttachedFace()).equals(toThisBlock);
		case TRAP_DOOR:
			TrapDoor trap = (TrapDoor)isThisAttached.getState().getData();
			return isThisAttached.getRelative(trap.getAttachedFace()).equals(toThisBlock);
		case WALL_SIGN:
			org.bukkit.material.Sign sign = (org.bukkit.material.Sign)isThisAttached.getState().getData();
			return isThisAttached.getRelative(sign.getAttachedFace()).equals(toThisBlock);
		case STONE_BUTTON:
		case WOOD_BUTTON:
			Button button = (Button)isThisAttached.getState().getData();
			return isThisAttached.getRelative(button.getAttachedFace()).equals(toThisBlock);
		default:
			return false;
		}
	}

	public static Set<Location> getSignsThatWouldFall(Block block) {
		Set<Location> locations = new HashSet<>();
		if (signItems.contains(block.getType())) locations.add(block.getLocation());

		for (BlockFace direction : relativeDirections) {
			Block sideBlock = block.getRelative(direction);
			Material sideMaterial = sideBlock.getType();

			if (attachableItems.contains(sideMaterial)) {
				if (isAttachedTo(sideBlock, block)) {
					if (signItems.contains(sideMaterial)) locations.add(sideBlock.getLocation());
					Block sideUpBlock = sideBlock.getRelative(BlockFace.UP);

					if (gravityItems.contains(sideUpBlock.getType()))
						locations.addAll(getSignsThatWouldFall(sideUpBlock));
				}
			}
		}

		Block upBlock = block.getRelative(BlockFace.UP);
		if (gravityItems.contains(upBlock.getType()))
			locations.addAll(getSignsThatWouldFall(upBlock));

		return locations;
	}

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
			for (Location signLocation : fallingSigns) {
				if (this.signLocations.contains(signLocation)) {
					Sign sign = (Sign)signLocation.getBlock().getState();
					String[] lines = sign.getLines();

					for (SignListener listener : this.listeners.keySet()) {
						List<String> keys = this.listeners.get(listener);
						boolean skip = false;

						for (String line : lines) {
							for (String key : keys) {
								if (line.contains(key)) {
									SignBreakEvent breakEvent = new SignBreakEvent(player, sign);
									listener.onSignBreak(breakEvent);

									if (breakEvent.isCancelled()) {
										event.setCancelled(true);
										return;
									} else {
										skip = true;
										removeSigns.add(signLocation);
										break;
									}
								}
							}

							if (skip) break;
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
				if (this.signLocations.contains(block.getLocation())) {
					if (Material.WALL_SIGN.equals(block.getType()) || Material.SIGN_POST.equals(block.getType())) {
						Sign sign = (Sign)block.getState();
						String[] lines = sign.getLines();

						for (SignListener listener : this.listeners.keySet()) {
							List<String> keys = this.listeners.get(listener);
							boolean skip = false;

							for (String line : lines) {
								for (String key : keys) {
									if (line.contains(key)) {
										SignInteractEvent interactEvent = new SignInteractEvent(event.getPlayer(), sign, event.getAction());
										listener.onSignInteract(interactEvent);
										if (interactEvent.isCancelled()) event.setCancelled(true);
										skip = true;
										break;
									}
								}

								if (skip) break;
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
		this.sendSignUpdate(event.getPlayer());
	}

	private static void sendChangePacket(Player player, Sign sign) {
		if (sign == null) return;
		if (player == null || !player.isOnline()) return;
		PacketContainer result = NiftyBukkit.getProtocolManager().createPacket(PacketType.Play.Server.UPDATE_SIGN);
		Integer[] coords = new Integer[] { sign.getX(), sign.getY(), sign.getZ() };

		try {
			for (int i = 0; i < 3; i++) result.getSpecificModifier(int.class).write(i, coords[i]);
			result.getStringArrays().write(0, sign.getLines());
			NiftyBukkit.getProtocolManager().sendServerPacket(player, result);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void sendSignUpdate() {
		for (Player player : super.getPlugin().getServer().getOnlinePlayers())
			this.sendSignUpdate(player, "");
	}

	public void sendSignUpdate(Player player) {
		this.sendSignUpdate(player, "");
	}

	public void sendSignUpdate(Player player, String key) {
		for (Location location : this.signLocations) {
			if (player.getLocation().distance(location) < 16) {
				Material material = location.getBlock().getType();

				if (Material.WALL_SIGN.equals(material) || Material.SIGN_POST.equals(material)) {
					Sign sign = (Sign)location.getBlock().getState();

					for (String line : sign.getLines()) {
						if ("".equals(key) || (!"".equals(line) && line.contains(key))) {
							sendChangePacket(player, sign);
							break;
						}
					}
				} else
					this.signLocations.remove(location);
			}
		}
	}

	public void start() {
		if (!this.isListening()) {
			this.listening = true;

			NiftyBukkit.getProtocolManager().addPacketListener(this.adapter = new PacketAdapter(super.getPlugin(), ListenerPriority.NORMAL, PacketType.Play.Server.UPDATE_SIGN) {
				@Override
				public void onPacketSending(PacketEvent event) {
					PacketContainer signUpdatePacket = event.getPacket();
					SignPacket incoming = new SignPacket(signUpdatePacket);
					Player player = event.getPlayer();
					Location location = new Location(player.getWorld(), incoming.getX(), incoming.getY(), incoming.getZ());
					Block block = location.getBlock();
					Sign sign = (Sign)block.getState();

					for (SignListener listener : listeners.keySet()) {
						List<String> keys = listeners.get(listener);

						for (int i = 0; i < 4; i++) {
							String line = sign.getLine(i);

							for (String key : keys) {
								if (line.contains(key)) {
									SignCreateEvent createEvent = new SignCreateEvent(player, sign);

									if (!signLocations.contains(location)) {
										listener.onSignCreate(createEvent);

										if (createEvent.isCancelled())
											block.breakNaturally();
										else
											signLocations.add(location);
									}

									if (!createEvent.isCancelled()) {
										SignUpdateEvent updateEvent = new SignUpdateEvent(player, sign, i, key);
										listener.onSignUpdate(updateEvent);

										if (!updateEvent.isCancelled() && updateEvent.isModified()) {
											String[] changed = updateEvent.getLines();

											for (int j = 0; j < changed.length; j++) {
												if (changed[i].length() > 15) {
													if (i < changed.length - 1 && changed[i + 1].isEmpty())
														changed[i + 1] = changed[i].substring(15);

													changed[i] = changed[i].substring(0, 15);
												}
											}

											SignPacket outgoing = new SignPacket(signUpdatePacket.shallowClone());
											outgoing.setLines(changed);
											event.setPacket(outgoing.getPacket());
										}
									}
								}
							}
						}
					}
				}
			});
		}
	}

	public void stop() {
		if (this.isListening()) {
			this.listening = false;
			NiftyBukkit.getProtocolManager().removePacketListener(this.adapter);
		}
	}

}