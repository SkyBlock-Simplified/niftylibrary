package net.netcoding.nifty.craftbukkit.minecraft.region;

import net.netcoding.nifty.common.minecraft.Effect;
import net.netcoding.nifty.common.minecraft.Particle;
import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.EntityType;
import net.netcoding.nifty.common.minecraft.entity.Item;
import net.netcoding.nifty.common.minecraft.entity.block.FallingBlock;
import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common.minecraft.entity.living.Player;
import net.netcoding.nifty.common.minecraft.entity.weather.LightningStrike;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.region.Biome;
import net.netcoding.nifty.common.minecraft.region.Chunk;
import net.netcoding.nifty.common.minecraft.region.Location;
import net.netcoding.nifty.common.minecraft.region.World;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.craftbukkit.minecraft.entity.CraftPlayer;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public final class CraftWorld implements World {

	private final org.bukkit.World world;
	private final Border border;

	public CraftWorld(org.bukkit.World world) {
		this.world = world;
		this.border = new Border(world.getWorldBorder());
	}

	@Override
	public boolean canGenerateStructures() {
		return this.getHandle().canGenerateStructures();
	}

	@Override
	public boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks) {
		return this.getHandle().createExplosion(x, y, z, power, setFire, breakBlocks);
	}

	@Override
	public Item dropItem(Location location, ItemStack itemStack) {
		return null; // TODO
	}

	@Override
	public Item dropItemNaturally(Location location, ItemStack itemStack) {
		return null; // TODO
	}

	@Override
	public boolean getAllowAnimals() {
		return this.getHandle().getAllowAnimals();
	}

	@Override
	public boolean getAllowMonsters() {
		return this.getHandle().getAllowMonsters();
	}

	@Override
	public int getAmbientSpawnLimit() {
		return this.getHandle().getAmbientSpawnLimit();
	}

	@Override
	public int getAnimalSpawnLimit() {
		return this.getHandle().getAnimalSpawnLimit();
	}

	@Override
	public Biome getBiome(int x, int z) {
		return Biome.valueOf(this.getHandle().getBiome(x, z).name());
	}

	@Override
	public Block getBlockAt(int x, int y, int z) {
		return null; // TODO
	}

	@Override
	public Chunk getChunkAt(int x, int z) {
		return null; // TODO
	}

	@Override
	public Difficulty getDifficulty() {
		return Difficulty.valueOf(this.getHandle().getDifficulty().name());
	}

	@Override
	public List<Entity> getEntities() {
		return null; // TODO
	}

	@Override
	public final <T extends Entity> Collection<Entity> getEntitiesByClass(Class<T> clazz) {
		return null; // TODO
	}

	@SafeVarargs
	@Override
	public final Collection<Entity> getEntitiesByClasses(Class<? extends Entity>... clazzes) {
		return null;
	}

	@Override
	public Environment getEnvironment() {
		return Environment.valueOf(this.getHandle().getEnvironment().name());
	}

	@Override
	public long getFullTime() {
		return this.getHandle().getFullTime();
	}

	@Override
	public String[] getGameRules() {
		return this.getHandle().getGameRules();
	}

	@Override
	public String getGameRuleValue(String rule) {
		return this.getHandle().getGameRuleValue(rule);
	}

	protected final org.bukkit.World getHandle() {
		return this.world;
	}

	@Override
	public Block getHighestBlockAt(int x, int z) {
		return null; // TODO
	}

	@Override
	public int getHighestBlockYAt(int x, int z) {
		return this.getHandle().getHighestBlockYAt(x, z);
	}

	@Override
	public double getHumidity(int x, int z) {
		return this.getHandle().getHumidity(x, z);
	}

	@Override
	public boolean getKeepSpawnInMemory() {
		return this.getHandle().getKeepSpawnInMemory();
	}

	@Override
	public List<LivingEntity> getLivingEntities() {
		return null; // TODO
	}

	@Override
	public Chunk[] getLoadedChunks() {
		return new Chunk[0]; // TODO
	}

	@Override
	public int getMaxHeight() {
		return this.getHandle().getMaxHeight();
	}

	@Override
	public int getMonsterSpawnLimit() {
		return this.getHandle().getMonsterSpawnLimit();
	}

	@Override
	public String getName() {
		return this.getHandle().getName();
	}

	@Override
	public Collection<Entity> getNearbyEntities(Location location, double x, double y, double z) {
		return null; // TODO
	}

	@Override
	public List<Player> getPlayers() {
		ConcurrentList<Player> players = new ConcurrentList<>();
		org.bukkit.Bukkit.getOnlinePlayers().stream().forEach(player -> players.add(new CraftPlayer(player)));
		return players;
	}

	@Override
	public Location getSpawnLocation() {
		return null; // TODO
	}

	@Override
	public int getSeaLevel() {
		return this.getHandle().getSeaLevel();
	}

	@Override
	public long getSeed() {
		return this.getHandle().getSeed();
	}

	@Override
	public double getTemperature(int x, int z) {
		return this.getHandle().getTemperature(x, z);
	}

	@Override
	public int getThunderDuration() {
		return this.getHandle().getThunderDuration();
	}

	@Override
	public long getTicksPerAnimalSpawns() {
		return this.getHandle().getTicksPerAnimalSpawns();
	}

	@Override
	public long getTicksPerMonsterSpawns() {
		return this.getHandle().getTicksPerMonsterSpawns();
	}

	@Override
	public long getTime() {
		return this.getHandle().getTime();
	}

	@Override
	public int getWaterAnimalSpawnLimit() {
		return this.getHandle().getWaterAnimalSpawnLimit();
	}

	@Override
	public int getWeatherDuration() {
		return this.getHandle().getWeatherDuration();
	}

	@Override
	public Border getWorldBorder() {
		return this.border;
	}

	@Override
	public File getWorldFolder() {
		return this.getHandle().getWorldFolder();
	}

	@Override
	public Type getWorldType() {
		return Type.valueOf(this.getHandle().getWorldType().name());
	}

	@Override
	public UUID getUniqueId() {
		return this.getHandle().getUID();
	}

	@Override
	public boolean hasStorm() {
		return this.getHandle().hasStorm();
	}

	@Override
	public boolean isAutoSave() {
		return this.getHandle().isAutoSave();
	}

	@Override
	public boolean isChunkInUse(int x, int z) {
		return this.getHandle().isChunkInUse(x, z);
	}

	@Override
	public boolean isGameRule(String rule) {
		return this.getHandle().isGameRule(rule);
	}

	@Override
	public boolean isPvP() {
		return this.getHandle().getPVP();
	}

	@Override
	public boolean isThundering() {
		return this.getHandle().isThundering();
	}

	@Override
	public boolean loadChunk(int x, int z, boolean generate) {
		return this.getHandle().loadChunk(x, z, generate);
	}

	@Override
	public void playEffect(Location location, Effect effect, int data) {
		// TODO
	}

	@Override
	public void playEffect(Location location, Effect effect, int data, int radius) {
		// TODO
	}

	@Override
	public <T> void playEffect(Location location, Effect effect, T data) {
		// TODO
	}

	@Override
	public <T> void playEffect(Location location, Effect effect, T data, int radius) {
		// TODO
	}

	@Override
	public void playSound(Location location, String sound, float volume, float pitch) {
		// TODO
	}

	@Override
	public boolean refreshChunk(int x, int z) {
		return this.getHandle().refreshChunk(x, z);
	}

	@Override
	public boolean regenerateChunk(int x, int z) {
		return this.getHandle().regenerateChunk(x, z);
	}

	@Override
	public void save() {
		this.getHandle().save();
	}

	@Override
	public void setAmbientSpawnLimit(int limit) {
		this.getHandle().setAmbientSpawnLimit(limit);
	}

	@Override
	public void setAnimalSpawnLimit(int limit) {
		this.getHandle().setAnimalSpawnLimit(limit);
	}

	@Override
	public void setAutoSave(boolean value) {
		this.getHandle().setAutoSave(value);
	}

	@Override
	public void setBiome(int x, int z, Biome biome) {
		this.getHandle().setBiome(x, z, org.bukkit.block.Biome.valueOf(biome.name()));
	}

	@Override
	public void setDifficulty(Difficulty difficulty) {
		this.getHandle().setDifficulty(org.bukkit.Difficulty.valueOf(difficulty.name()));
	}

	@Override
	public void setFullTime(long time) {
		this.getHandle().setFullTime(time);
	}

	@Override
	public boolean setGameRuleValue(String rule, String value) {
		return this.getHandle().setGameRuleValue(rule, value);
	}

	@Override
	public void setKeepSpawnInMemory(boolean value) {
		this.getHandle().setKeepSpawnInMemory(value);
	}

	@Override
	public void setMonsterSpawnLimit(int limit) {
		this.getHandle().setMonsterSpawnLimit(limit);
	}

	@Override
	public void setPvP(boolean value) {
		this.getHandle().setPVP(value);
	}

	@Override
	public void setSpawnFlags(boolean allowMonsters, boolean allowAnimals) {
		this.getHandle().setSpawnFlags(allowMonsters, allowAnimals);
	}

	@Override
	public boolean setSpawnLocation(int x, int y, int z) {
		return this.getHandle().setSpawnLocation(x, y, z);
	}

	@Override
	public void setStorm(boolean value) {
		this.getHandle().setStorm(value);
	}

	@Override
	public void setThunderDuration(int durationTicks) {
		this.getHandle().setThunderDuration(durationTicks);
	}

	@Override
	public void setThundering(boolean value) {
		this.getHandle().setThundering(value);
	}

	@Override
	public void setTicksPerAnimalSpawns(int ticks) {
		this.getHandle().setTicksPerAnimalSpawns(ticks);
	}

	@Override
	public void setTicksPerMonsterSpawns(int ticks) {
		this.getHandle().setTicksPerMonsterSpawns(ticks);
	}

	@Override
	public void setTime(long time) {
		this.getHandle().setTime(time);
	}

	@Override
	public void setWaterAnimalSpawnLimit(int limit) {
		this.getHandle().setWaterAnimalSpawnLimit(limit);
	}

	@Override
	public void setWeatherDuration(int value) {
		this.getHandle().setWeatherDuration(value);
	}

	@Override
	public <T extends Entity> T spawn(Location location, Class<T> entity) {
		return null; // TODO
	}

	@Override
	public Entity spawnEntity(Location location, EntityType type) {
		return null; // TODO
	}
	@Override
	public FallingBlock spawnFallingBlock(Location location, int id, byte data) {
		return null; // TODO
	}

	@Override
	public <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
		this.getHandle().spawnParticle(org.bukkit.Particle.valueOf(particle.name()), x, y, z, count, offsetX, offsetY, offsetZ, extra, data);
	}
	@Override
	public LightningStrike strikeLightning(Location location) {
		return null; // TODO
	}
	@Override
	public LightningStrike strikeLightningEffect(Location location) {
		return null; // TODO
	}

	@Override
	public boolean unloadChunk(int x, int z, boolean save, boolean safe) {
		return this.getHandle().unloadChunk(x, z, save, safe);
	}

	@Override
	public boolean unloadChunkRequest(int x, int z, boolean save) {
		return this.getHandle().unloadChunkRequest(x, z, save);
	}

	public final class Border implements World.Border {

		private final org.bukkit.WorldBorder border;

		public Border(org.bukkit.WorldBorder border) {
			this.border = border;
		}

		@Override
		public double getDamageAmount() {
			return this.getHandle().getDamageAmount();
		}

		@Override
		public Location getCenter() {
			return null; // TODO
		}

		@Override
		public double getDamageBuffer() {
			return this.getHandle().getDamageBuffer();
		}

		protected final org.bukkit.WorldBorder getHandle() {
			return this.border;
		}

		@Override
		public double getSize() {
			return this.getHandle().getSize();
		}

		@Override
		public int getWarningDistance() {
			return this.getHandle().getWarningDistance();
		}

		@Override
		public int getWarningTime() {
			return this.getHandle().getWarningTime();
		}

		@Override
		public void reset() {
			this.getHandle().reset();
		}

		@Override
		public void setCenter(double x, double z) {
			this.getHandle().setCenter(x, z);
		}

		@Override
		public void setDamageAmount(double damage) {
			this.getHandle().setDamageAmount(damage);
		}

		@Override
		public void setDamageBuffer(double buffer) {
			this.getHandle().setDamageBuffer(buffer);
		}

		@Override
		public void setSize(double newSize, long seconds) {
			this.getHandle().setSize(newSize, seconds);
		}

		@Override
		public void setWarningDistance(int distance) {
			this.getHandle().setWarningDistance(distance);
		}

		@Override
		public void setWarningTime(int time) {
			this.getHandle().setWarningTime(time);
		}

	}

}