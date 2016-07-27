package net.netcoding.nifty.common.minecraft.region;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.Effect;
import net.netcoding.nifty.common.minecraft.Particle;
import net.netcoding.nifty.common.minecraft.block.Block;
import net.netcoding.nifty.common.minecraft.entity.Entity;
import net.netcoding.nifty.common.minecraft.entity.EntityType;
import net.netcoding.nifty.common.minecraft.entity.Item;
import net.netcoding.nifty.common.minecraft.entity.block.FallingBlock;
import net.netcoding.nifty.common.minecraft.entity.living.LivingEntity;
import net.netcoding.nifty.common.minecraft.entity.living.human.Player;
import net.netcoding.nifty.common.minecraft.entity.projectile.arrow.Arrow;
import net.netcoding.nifty.common.minecraft.entity.weather.LightningStrike;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.material.types.TreeType;
import net.netcoding.nifty.common.minecraft.sound.Sound;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.misc.Vector;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("deprecation")
public interface World {

	boolean canGenerateStructures();

	default boolean createExplosion(Location location, float power) {
		return this.createExplosion(location, power, false);
	}

	default boolean createExplosion(Location location, float power, boolean setFire) {
		return this.createExplosion(location, power, setFire, false);
	}

	default boolean createExplosion(Location location, float power, boolean setFire, boolean breakBlocks) {
		return this.createExplosion(location.getX(), location.getY(), location.getZ(), power, setFire, breakBlocks);
	}

	default boolean createExplosion(double x, double y, double z, float power) {
		return this.createExplosion(x, y, z, power, false);
	}

	default boolean createExplosion(double x, double y, double z, float power, boolean setFire) {
		return this.createExplosion(x, y, z, power, setFire, false);
	}

	boolean createExplosion(double x, double y, double z, float power, boolean setFire, boolean breakBlocks);

	Item dropItem(Location location, ItemStack item);

	Item dropItemNaturally(Location location, ItemStack item);

	boolean generateTree(Location location, TreeType type);

	boolean getAllowAnimals();

	boolean getAllowMonsters();

	int getAmbientSpawnLimit();

	int getAnimalSpawnLimit();

	Biome getBiome(int x, int z);

	default Block getBlockAt(Location location) {
		return this.getBlockAt(location.getBlockX(), location.getBlockX(), location.getBlockX());
	}

	Block getBlockAt(int x, int y, int z);

	default Material getBlockTypeAt(int x, int y, int z) {
		return this.getBlockAt(x, y, z).getType();
	}

	default Material getBlockTypeAt(Location location) {
		return this.getBlockAt(location).getType();
	}

	default int getBlockTypeIdAt(int x, int y, int z) {
		return this.getBlockAt(x, y, z).getTypeId();
	}

	default int getBlockTypeIdAt(Location location) {
		return this.getBlockAt(location).getTypeId();
	}

	Border getBorder();

	Chunk getChunkAt(int x, int z);

	default Chunk getChunkAt(Location location) {
		return this.getChunkAt(location.getBlockX(), location.getBlockZ());
	}

	default Chunk getChunkAt(Block block) {
		return this.getChunkAt(block.getLocation());
	}

	Difficulty getDifficulty();

	List<Entity> getEntities();

	<T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz);

	@SuppressWarnings("unchecked")
	Collection<Entity> getEntitiesByClasses(Class<? extends Entity>... clazzes);

	Environment getEnvironment();

	File getFolder();

	long getFullTime();

	String[] getGameRules();

	String getGameRuleValue(String rule);

	default Block getHighestBlockAt(Location location) {
		return this.getHighestBlockAt(location.getBlockX(), location.getBlockZ());
	}

	Block getHighestBlockAt(int x, int z);

	default int getHighestBlockYAt(Location location) {
		return this.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
	}

	int getHighestBlockYAt(int x, int z);

	double getHumidity(int x, int z);

	boolean getKeepSpawnInMemory();

	List<LivingEntity> getLivingEntities();

	Collection<Chunk> getLoadedChunks();

	int getMaxHeight();

	int getMonsterSpawnLimit();

	String getName();

	List<Entity> getNearbyEntities(Location location, double x, double y, double z);

	default List<Player> getPlayers() {
		return Nifty.getServer().getPlayerList().stream().filter(player -> player.getWorld().equals(this)).collect(Concurrent.toList());
	}

	Location getSpawnLocation();

	int getSeaLevel();

	long getSeed();

	double getTemperature(int x, int z);

	int getThunderDuration();

	long getTicksPerAnimalSpawns();

	long getTicksPerMonsterSpawns();

	long getTime();

	Type getType();

	int getWaterAnimalSpawnLimit();

	int getWeatherDuration();

	UUID getUniqueId();

	boolean hasStorm();

	boolean isAutoSave();

	default boolean isChunkLoaded(Chunk chunk) {
		return this.isChunkLoaded(chunk.getX(), chunk.getZ());
	}

	default boolean isChunkLoaded(int x, int z) {
		return this.getLoadedChunks().stream().anyMatch(chunk -> chunk.getX() == x && chunk.getZ() == z);
	}

	boolean isChunkInUse(int x, int z);

	boolean isGameRule(String rule);

	boolean isPvP();

	boolean isThundering();

	default void loadChunk(Chunk chunk) {
		this.loadChunk(chunk.getX(), chunk.getZ());
	}

	default void loadChunk(int x, int z) {
		this.loadChunk(x, z, true);
	}

	boolean loadChunk(int x, int z, boolean generate);

	default void playEffect(Location location, Effect effect, int data) {
		this.playEffect(location, effect, data, 64);
	}

	void playEffect(Location location, Effect effect, int data, int radius);

	default <T> void playEffect(Location location, Effect effect, T data) {
		this.playEffect(location, effect, data, 64);
	}

	<T> void playEffect(Location location, Effect effect, T data, int radius);

	default void playSound(Sound sound, float volume, float pitch) {
		this.playSound(sound.name(), volume, pitch);
	}

	default void playSound(String sound, float volume, float pitch) {
		this.getPlayers().forEach(player -> player.playSound(sound, volume, pitch));
	}

	default void playSound(Location location, Sound sound, float volume, float pitch) {
		this.playSound(location, sound.name(), volume, pitch); // TODO: Check
	}

	void playSound(Location location, String sound, float volume, float pitch);

	@Deprecated
	boolean refreshChunk(int x, int z);

	boolean regenerateChunk(int x, int z);

	void save();

	void setAmbientSpawnLimit(int limit);

	void setAnimalSpawnLimit(int limit);

	void setAutoSave(boolean value);

	void setBiome(int x, int z, Biome biome);

	void setDifficulty(Difficulty difficulty);

	void setFullTime(long time);

	boolean setGameRuleValue(String rule, String value);

	void setKeepSpawnInMemory(boolean value);

	void setMonsterSpawnLimit(int limit);

	void setPvP(boolean value);

	void setSpawnFlags(boolean allowMonsters, boolean allowAnimals);

	default boolean setSpawnLocation(Location location) {
		return this.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}

	boolean setSpawnLocation(int x, int y, int z);

	void setStorm(boolean value);

	void setThunderDuration(int durationTicks);

	void setThundering(boolean value);

	void setTicksPerAnimalSpawns(int ticks);

	void setTicksPerMonsterSpawns(int ticks);

	void setTime(long time);

	void setWaterAnimalSpawnLimit(int limit);

	void setWeatherDuration(int value);

	<T extends Entity> T spawn(Location location, Class<T> entity);

	default Arrow spawnArrow(Location location, Vector direction, float speed, float spread) {
		return this.spawnArrow(location, direction, speed, spread, Arrow.class);
	}

	<T extends Arrow> T spawnArrow(Location location, Vector direction, float speed, float spread, Class<T> type);

	Entity spawnEntity(Location location, EntityType type);

	default FallingBlock spawnFallingBlock(Location location, Material material, byte data) {
		return this.spawnFallingBlock(location, material.getId(), data);
	}

	FallingBlock spawnFallingBlock(Location location, int id, byte data);

	default void spawnParticle(Particle particle, Location location, int count) {
		this.spawnParticle(particle, location, count, 0, 0, 0);
	}

	default void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ) {
		this.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, 0);
	}

	default void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra) {
		this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra);
	}

	default void spawnParticle(Particle particle, double x, double y, double z, int count) {
		this.spawnParticle(particle, x, y, z, count, 0, 0, 0);
	}

	default void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ) {
		this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 0);
	}

	default void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra) {
		this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, null);
	}

	default <T> void spawnParticle(Particle particle, Location location, int count, T data) {
		this.spawnParticle(particle, location, count, 0, 0, 0, data);
	}

	default <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, T data) {
		this.spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, 0, data);
	}

	default <T> void spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
		this.spawnParticle(particle, location.getX(), location.getY(), location.getZ(), count, offsetX, offsetY, offsetZ, extra, data);
	}

	default <T> void spawnParticle(Particle particle, double x, double y, double z, int count, T data) {
		this.spawnParticle(particle, x, y, z, count, 0, 0, 0, data);
	}

	default <T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
		this.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, 0, data);
	}

	<T> void spawnParticle(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data);

	LightningStrike strikeLightning(Location location);

	LightningStrike strikeLightningEffect(Location location);

	default boolean unloadChunk(Chunk chunk) {
		return this.unloadChunk(chunk.getX(), chunk.getZ());
	}

	default boolean unloadChunk(int x, int z) {
		return this.unloadChunk(x, z, true);
	}

	default boolean unloadChunk(int x, int z, boolean save) {
		return this.unloadChunk(x, z, save, true);
	}

	@Deprecated
	boolean unloadChunk(int x, int z, boolean save, boolean safe);

	default boolean unloadChunkRequest(int x, int z) {
		return this.unloadChunkRequest(x, z, true);
	}

	boolean unloadChunkRequest(int x, int z, boolean save);


	//boolean generateTree(Location location, TreeType type, BlockChangeDelegate delegate);

	//ChunkGenerator getGenerator();

	//List<BlockPopulator> getPopulators();

	//ChunkSnapshot getEmptyChunkSnapshot(int x, int z, boolean includeBiome, boolean includeBiomeTempRain);


	interface Border {

		double getDamageAmount();

		Location getCenter();

		double getDamageBuffer();

		double getSize();

		int getWarningDistance();

		int getWarningTime();

		void reset();

		default void setCenter(Location location) {
			this.setCenter(location.getX(), location.getY());
		}

		void setCenter(double x, double z);

		void setDamageAmount(double damage);

		void setDamageBuffer(double buffer);

		default void setSize(double newSize) {
			this.setSize(newSize, 0);
		}

		void setSize(double newSize, long seconds);

		void setWarningDistance(int distance);

		void setWarningTime(int time);

	}

	enum Difficulty {

		PEACEFUL(0),
		EASY(1),
		NORMAL(2),
		HARD(3);

		private static final ConcurrentMap<Integer, Difficulty> BY_ID = Concurrent.newMap();
		private final int value;

		static {
			Arrays.stream(values()).forEach(difficulty -> BY_ID.put(difficulty.getValue(), difficulty));
		}

		Difficulty(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		public static Difficulty getDifficulty(int value) {
			return BY_ID.get(value);
		}

	}

	enum Environment {

		NORMAL(0),
		NETHER(-1),
		THE_END(1);

		private static final ConcurrentMap<Integer, Environment> BY_ID = Concurrent.newMap();
		private final int id;

		static {
			Arrays.stream(values()).forEach(environment -> BY_ID.put(environment.getId(), environment));
		}

		Environment(int id) {
			this.id = id;
		}

		public int getId() {
			return this.id;
		}

		public static Environment getEnvironment(int id) {
			return BY_ID.get(id);
		}
	}

	enum Type {

		NORMAL("DEFAULT"),
		FLAT("FLAT"),
		VERSION_1_1("DEFAULT_1_1"),
		LARGE_BIOMES("LARGEBIOMES"),
		AMPLIFIED("AMPLIFIED"),
		CUSTOMIZED("CUSTOMIZED");

		private static final ConcurrentMap<String, Type> BY_NAME = Concurrent.newMap();
		private final String name;

		static {
			Arrays.stream(values()).forEach(worldType -> BY_NAME.put(worldType.getName(), worldType));
		}

		Type(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public static Type getType(String name) {
			return BY_NAME.get(name.toUpperCase());
		}

	}

	enum Weather {

		CLEAR,
		DOWNFALL

	}

}