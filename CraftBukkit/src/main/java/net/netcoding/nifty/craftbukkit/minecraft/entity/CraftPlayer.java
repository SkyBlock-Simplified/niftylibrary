package net.netcoding.nifty.craftbukkit.minecraft.entity;

import net.netcoding.nifty.common.minecraft.GameMode;
import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.EntityType;
import net.netcoding.nifty.common.minecraft.entity.living.Player;
import net.netcoding.nifty.common.minecraft.entity.projectile.Projectile;
import net.netcoding.nifty.common.minecraft.event.entity.EntityDamageEvent;
import net.netcoding.nifty.common.minecraft.event.player.PlayerTeleportEvent;
import net.netcoding.nifty.common.minecraft.inventory.EntityEquipment;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.InventoryView;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionEffect;
import net.netcoding.nifty.common.minecraft.inventory.item.potion.PotionEffectType;
import net.netcoding.nifty.common.minecraft.inventory.types.PlayerInventory;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.core.api.plugin.Plugin;
import net.netcoding.nifty.core.util.json.JsonMessage;
import net.netcoding.nifty.core.util.misc.Vector;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public final class CraftPlayer implements Player {

	private final org.bukkit.entity.Player player;

	public CraftPlayer(org.bukkit.entity.Player player) {
		this.player = player;
	}

	@Override
	public boolean canSee(Player player) {
		return false;
	}
	@Override
	public void chat(String message) {

	}
	@Override
	public InetSocketAddress getAddress() {
		return this.getHandle().getAddress();
	}
	@Override
	public boolean getAllowFlight() {
		return false;
	}

	@Override
	public Location getBedSpawnLocation() {
		return null; // TODO this.getHandle().getBedSpawnLocation();
	}
	@Override
	public String getDisplayName() {
		return null;
	}
	@Override
	public float getExhaustion() {
		return 0;
	}
	@Override
	public float getExperience() {
		return 0;
	}
	@Override
	public float getFlySpeed() {
		return 0;
	}
	@Override
	public int getFoodLevel() {
		return 0;
	}

	@Override
	public void closeInventory() {

	}
	@Override
	public Inventory getEnderChest() {
		return null;
	}
	@Override
	public GameMode getGameMode() {
		return GameMode.valueOf(this.getHandle().getGameMode().name());
	}
	@Override
	public PlayerInventory getInventory() {
		return null;
	}
	@Override
	public ItemStack getItemOnCursor() {
		return null;
	}
	@Override
	public int getExpToLevel() {
		return 0;
	}
	@Override
	public int getSleepTicks() {
		return 0;
	}
	@Override
	public boolean isBlocking() {
		return false;
	}
	@Override
	public boolean isSleeping() {
		return false;
	}
	@Override
	public InventoryView getOpenInventory() {
		return null;
	}
	@Override
	public InventoryView openEnchanting(Location var1, boolean var2) {
		return null;
	}
	@Override
	public InventoryView openInventory(Inventory inventory) {
		return null;
	}
	@Override
	public void openInventory(InventoryView view) {

	}
	@Override
	public InventoryView openWorkbench(Location var1, boolean var2) {
		return null;
	}
	@Override
	public int getLevel() {
		return 0;
	}
	@Override
	public String getPlayerListName() {
		return null;
	}
	@Override
	public long getPlayerTime() {
		return 0;
	}
	@Override
	public long getPlayerTimeOffset() {
		return 0;
	}
	@Override
	public World.Weather getPlayerWeather() {
		return null;
	}
	@Override
	public float getSaturation() {
		return 0;
	}
	@Override
	public int getTotalExperience() {
		return 0;
	}
	@Override
	public float getWalkSpeed() {
		return 0;
	}
	@Override
	public void giveExperience(int experience) {

	}
	@Override
	public void giveExperienceLevels(int levels) {

	}
	@Override
	public void hidePlayer(Player player) {

	}
	@Override
	public boolean isFlying() {
		return false;
	}
	@Override
	public boolean isOnGround() {
		return false;
	}
	@Override
	public boolean isValid() {
		return false;
	}
	@Override
	public boolean leaveVehicle() {
		return false;
	}
	@Override
	public void playEffect(Effect effect) {

	}
	@Override
	public void remove() {

	}
	@Override
	public void setCustomName(String name) {

	}
	@Override
	public void setCustomNameVisible(boolean value) {

	}
	@Override
	public void setFallDistance(float distance) {

	}
	@Override
	public void setFireTicks(int ticks) {

	}
	@Override
	public void setGlowing(boolean value) {

	}
	@Override
	public void setInvulnerable(boolean value) {

	}
	@Override
	public void setLastDamageCause(EntityDamageEvent event) {

	}
	@Override
	public boolean setPassenger(Entity passenger) {
		return false;
	}
	@Override
	public void setTicksLived(int ticks) {

	}
	@Override
	public void setVelocity(Vector velocity) {

	}
	@Override
	public boolean teleport(Location location) {
		return false;
	}
	@Override
	public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause var2) {
		return false;
	}
	@Override
	public boolean teleport(Entity entity) {
		return false;
	}
	@Override
	public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause var2) {
		return false;
	}
	@Override
	public boolean isPlayerTimeRelative() {
		return false;
	}
	@Override
	public boolean isSleepingIgnored() {
		return false;
	}
	@Override
	public boolean isSneaking() {
		return false;
	}
	@Override
	public boolean isSprinting() {
		return false;
	}
	@Override
	public void kick(String message) {

	}
	@Override
	public void loadData() {

	}
	@Override
	public boolean performCommand(String command) {
		return false;
	}
	@Override
	public void resetPlayerTime() {

	}
	@Override
	public void resetPlayerWeather() {

	}
	@Override
	public void saveData() {

	}
	@Override
	public void setAllowFlight(boolean value) {

	}
	@Override
	public void setDisplayName(String displayName) {

	}
	@Override
	public void setExhaustion(float exhaustion) {

	}
	@Override
	public void setExperience(float experience) {

	}
	@Override
	public void setFlying(boolean value) {

	}
	@Override
	public void setFlySpeed(float speed) throws IllegalArgumentException {

	}
	@Override
	public void setFoodLevel(int level) {

	}

	@Override
	public long getFirstPlayed() {
		return this.getHandle().getFirstPlayed();
	}

	@Override
	public long getLastPlayed() {
		return this.getHandle().getLastPlayed();
	}

	protected org.bukkit.entity.Player getHandle() {
		return this.player;
	}

	@Override
	public String getName() {
		return this.getHandle().getName();
	}

	@Override
	public Player getPlayer() {
		return this;
	}

	@Override
	public boolean eject() {
		return false;
	}
	@Override
	public String getCustomName() {
		return null;
	}
	@Override
	public int getEntityId() {
		return 0;
	}
	@Override
	public float getFallDistance() {
		return 0;
	}
	@Override
	public int getFireTicks() {
		return 0;
	}
	@Override
	public EntityDamageEvent getLastDamageCause() {
		return null;
	}
	@Override
	public Location getLocation() {
		return null;
	}
	@Override
	public int getMaxFireTicks() {
		return 0;
	}
	@Override
	public List<Entity> getNearbyEntities(double x, double y, double z) {
		return null;
	}
	@Override
	public Entity getPassenger() {
		return null;
	}
	@Override
	public int getTicksLived() {
		return 0;
	}
	@Override
	public EntityType getType() {
		return null;
	}
	@Override
	public UUID getUniqueId() {
		return this.getHandle().getUniqueId();
	}
	@Override
	public Entity getVehicle() {
		return null;
	}
	@Override
	public Vector getVelocity() {
		return null;
	}
	@Override
	public World getWorld() {
		return null;
	}
	@Override
	public boolean isCustomNameVisible() {
		return false;
	}
	@Override
	public boolean isDead() {
		return false;
	}
	@Override
	public boolean isEmpty() {
		return false;
	}
	@Override
	public boolean isGlowing() {
		return false;
	}
	@Override
	public boolean isInsideVehicle() {
		return false;
	}
	@Override
	public boolean isInvulnerable() {
		return false;
	}

	@Override
	public boolean hasPlayedBefore() {
		return this.getHandle().hasPlayedBefore();
	}

	@Override
	public boolean isBanned() {
		return this.getHandle().isBanned();
	}

	@Override
	public boolean isOnline() {
		return this.getHandle().isOnline();
	}

	@Override
	public boolean isOp() {
		return this.getHandle().isOp();
	}

	@Override
	public boolean isWhitelisted() {
		return this.getHandle().isWhitelisted();
	}

	@Override
	public void sendMessage(JsonMessage message) throws Exception {
		// TODO
	}

	@Override
	public void sendMessage(String message) {
		this.getHandle().sendMessage(message);
	}

	@Override
	public void setBanned(boolean value) {
		this.getHandle().setBanned(value);
	}

	@Override
	public void setGameMode(GameMode gameMode) {
		this.getHandle().setGameMode(org.bukkit.GameMode.valueOf(gameMode.name()));
	}
	@Override
	public void setItemOnCursor(ItemStack itemStack) {

	}
	@Override
	public boolean setWindowProperty(InventoryView.Property var1, int var2) {
		return false;
	}
	@Override
	public void setLevel(int level) {

	}
	@Override
	public void setSleepingIgnored(boolean value) {

	}
	@Override
	public void setPlayerListName(String playerListName) {

	}
	@Override
	public void setPlayerWeather(World.Weather weather) {

	}
	@Override
	public void setSaturation(float saturation) {

	}
	@Override
	public void setSneaking(boolean value) {

	}
	@Override
	public void setSprinting(boolean value) {

	}
	@Override
	public void setTotalExperience(int experience) {

	}
	@Override
	public void setWalkSpeed(float speed) throws IllegalArgumentException {

	}
	@Override
	public void showPlayer(Player player) {

	}
	@Override
	public void updateInventory() {

	}
	@Override
	public void setPlayerTime(long var1, boolean var3) {

	}
	@Override
	public void setBedSpawnLocation(Location location) {

	}
	@Override
	public void setBedSpawnLocation(Location location, boolean var2) {

	}

	@Override
	public void setOp(boolean value) {
		this.getHandle().setOp(value);
	}

	@Override
	public void setWhitelisted(boolean value) {
		this.getHandle().setWhitelisted(value);
	}

	@Override
	public Set<String> getListeningPluginChannels() {
		return null;
	}
	@Override
	public void sendPluginMessage(Plugin plugin, String channel, byte[] message) {

	}
	@Override
	public double getEyeHeight() {
		return 0;
	}
	@Override
	public double getEyeHeight(boolean var1) {
		return 0;
	}
	@Override
	public Location getEyeLocation() {
		return null;
	}
	@Override
	public List<Block> getLineOfSight(HashSet<Byte> var1, int var2) {
		return null;
	}
	@Override
	public List<Block> getLineOfSight(Set<Material> var1, int var2) {
		return null;
	}
	@Override
	public Block getTargetBlock(HashSet<Byte> var1, int var2) {
		return null;
	}
	@Override
	public Block getTargetBlock(Set<Material> var1, int var2) {
		return null;
	}
	@Override
	public List<Block> getLastTwoTargetBlocks(HashSet<Byte> var1, int var2) {
		return null;
	}
	@Override
	public List<Block> getLastTwoTargetBlocks(Set<Material> var1, int var2) {
		return null;
	}
	@Override
	public int getRemainingAir() {
		return 0;
	}
	@Override
	public void setRemainingAir(int var1) {

	}
	@Override
	public int getMaximumAir() {
		return 0;
	}
	@Override
	public void setMaximumAir(int var1) {

	}
	@Override
	public int getMaximumNoDamageTicks() {
		return 0;
	}
	@Override
	public void setMaximumNoDamageTicks(int var1) {

	}
	@Override
	public double getLastDamage() {
		return 0;
	}
	@Override
	public int _INVALID_getLastDamage() {
		return 0;
	}
	@Override
	public void setLastDamage(double var1) {

	}
	@Override
	public void _INVALID_setLastDamage(int var1) {

	}
	@Override
	public int getNoDamageTicks() {
		return 0;
	}
	@Override
	public void setNoDamageTicks(int var1) {

	}
	@Override
	public Player getKiller() {
		return null;
	}
	@Override
	public boolean addPotionEffect(PotionEffect var1) {
		return false;
	}
	@Override
	public boolean addPotionEffect(PotionEffect var1, boolean var2) {
		return false;
	}
	@Override
	public boolean addPotionEffects(Collection<PotionEffect> var1) {
		return false;
	}
	@Override
	public boolean hasPotionEffect(PotionEffectType var1) {
		return false;
	}
	@Override
	public void removePotionEffect(PotionEffectType var1) {

	}
	@Override
	public Collection<PotionEffect> getActivePotionEffects() {
		return null;
	}
	@Override
	public boolean hasLineOfSight(Entity var1) {
		return false;
	}
	@Override
	public boolean getRemoveWhenFarAway() {
		return false;
	}
	@Override
	public void setRemoveWhenFarAway(boolean var1) {

	}
	@Override
	public EntityEquipment getEquipment() {
		return null;
	}
	@Override
	public void setCanPickupItems(boolean var1) {

	}
	@Override
	public boolean getCanPickupItems() {
		return false;
	}
	@Override
	public boolean isLeashed() {
		return false;
	}
	@Override
	public Entity getLeashHolder() throws IllegalStateException {
		return null;
	}
	@Override
	public boolean setLeashHolder(Entity var1) {
		return false;
	}
	@Override
	public boolean isGliding() {
		return false;
	}
	@Override
	public void setGliding(boolean var1) {

	}
	@Override
	public void setAI(boolean var1) {

	}
	@Override
	public boolean hasAI() {
		return false;
	}
	@Override
	public void setCollidable(boolean var1) {

	}
	@Override
	public boolean isCollidable() {
		return false;
	}
	@Override
	public void damage(double amount) {

	}
	@Override
	public void damage(double amount, Entity source) {

	}
	@Override
	public double getHealth() {
		return 0;
	}
	@Override
	public double getMaxHealth() {
		return 0;
	}
	@Override
	public void resetMaxHealth() {

	}
	@Override
	public void setHealth(double health) {

	}
	@Override
	public void setMaxHealth(double maxHealth) {

	}
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile) {
		return null;
	}
	@Override
	public <T extends Projectile> T launchProjectile(Class<? extends T> projectile, Vector velocity) {
		return null;
	}
	@Override
	public boolean isPermissionSet(String permission) {
		return false;
	}
	@Override
	public boolean hasPermission(String permission) {
		return false;
	}
}