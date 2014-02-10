package net.netcoding.niftybukkit;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class GhostBusters extends BukkitHelper {

	private static final OfflinePlayer[] EMPTY_PLAYERS = new OfflinePlayer[0];
	private final transient String name;
	private final transient Team team;
	private final transient Set<String> members = new HashSet<>();
	private final transient BukkitTask task;
	private transient boolean closed;

	public GhostBusters(JavaPlugin plugin) {
		this(plugin, "Ghost");
	}

	public GhostBusters(JavaPlugin plugin, String teamName) {
		super(plugin);
		Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		this.name = teamName;
		Team team = board.getTeam(this.getName());
		this.team = (team == null ? board.registerNewTeam(this.getName()) : team);
		this.team.setCanSeeFriendlyInvisibles(true);
		this.task = this.createTask();
	}

	private BukkitTask createTask() {
		return Bukkit.getScheduler().runTaskTimerAsynchronously(super.getPlugin(), new Runnable() {
			@Override
			public void run() {
				for (OfflinePlayer member : getMembers()) {
					Player player = member.getPlayer();

					if (player != null) {
						if (!player.isDead())
							updatePlayer(player, isGhost(player));
					} else {
						try {
							members.remove(member.getName());
							team.removePlayer(member);
						} catch (Exception ex) { }
					}
				}
			}
		}, 10, 10);
	}

	/**
	 * Remove all existing player members and ghosts.
	 */
	public void clearMembers() {
		this.validateState();

		for (OfflinePlayer player : this.getMembers())
			this.team.removePlayer(player);
	}

	/**
	 * Unregister ghost team.
	 */
	public void close() {
		if (!this.isClosed()) {
			this.task.cancel();
			this.team.unregister();
			this.closed = true;
		}
	}

	/**
	 * Checks if the current ghost instance is closed.
	 * @return True if the current ghost instance is closed.
	 */
	public boolean isClosed() {
		return this.closed;
	}

	/**
	 * Add the given player to this ghost team and lets them see ghosts.
	 * @param player - The player to add to the ghost team.
	 */
	public void add(Player player) {
		this.setGhost(player, false);
	}

	/**
	 * Get the current team name.
	 * @return Current team name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Retrieve every ghost member.
	 * @return Every ghost member.
	 */
	public OfflinePlayer[] getGhosts() {
		Set<OfflinePlayer> ghosts = this._getMembers();
		ghosts.removeAll(this._getPlayers());
		return this.toArray(ghosts);
	}

	private Set<OfflinePlayer> _getPlayers() {
		Set<OfflinePlayer> players = this._getMembers();

		for (Iterator<OfflinePlayer> it = players.iterator(); it.hasNext();) {
			if (!this.members.contains(it.next().getName()))
				it.remove();
		}

		return players;
	}

	/**
	 * Retrieve every team member.
	 * @return Every team member.
	 */
	public OfflinePlayer[] getPlayers() {
		return this.toArray(this._getPlayers());
	}

	private Set<OfflinePlayer> _getMembers() {
		this.validateState();
		return this.team.getPlayers();
	}

	/**
	 * Retrieve every team member and observer.
	 * @return Every team member and observer.
	 */
	public OfflinePlayer[] getMembers() {
		return this.toArray(this._getMembers());
	}

	/**
	 * Determine if the current player is currently a ghost or an observer.
	 * @param player - The player to check.
	 * @return True if player is a team member or observer.
	 */
	public boolean hasPlayer(Player player) {
		this.validateState();
		return this.team.hasPlayer(player);
	}

	/**
	 * Determine if the given player is tracked by this ghost manager and is a ghost.
	 * @param player - The player to check.
	 * @return True if the player is a ghost.
	 */
	public boolean isGhost(Player player) {
		return player != null && this.hasPlayer(player) && this.members.contains(player.getName());
	}

	/**
	 * Remove the given player from the ghost instance, turning them back into the living and unable to see ghosts.
	 * @param player - The player to remove.
	 */
	public void remove(Player player) {
		this.validateState();

		if (this.team.removePlayer(player)) {
			this.members.remove(player.getName());
			this.updatePlayer(player, false);
		}
	}

	/**
	 * Set whether or not a given player is a ghost.
	 * @param player - The player to set as a ghost.
	 */
	public void setGhost(Player player) {
		this.setGhost(player, true);
	}

	/**
	 * Set given player as a ghost.
	 * @param player - The player to set as a ghost.
	 * @param isGhost - True to make the given player into a ghost.
	 */
	public void setGhost(Player player, boolean isGhost) {
		this.validateState();

		if (!this.hasPlayer(player))
			this.team.addPlayer(player);

		if (isGhost)
			this.members.add(player.getName());
		else
			this.members.remove(player.getName());

		this.updatePlayer(player, isGhost);
	}

	private OfflinePlayer[] toArray(Set<OfflinePlayer> players) {
		if (players != null)
			return players.toArray(new OfflinePlayer[0]);
		else
			return EMPTY_PLAYERS;
	}

	private void updatePlayer(Player player, boolean isGhost) {
		if (isGhost)
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
		else
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
	}

	private void validateState() {
		if (this.isClosed())
			throw new IllegalStateException("Ghost instance is closed. Cannot reuse instances.");
	}

}