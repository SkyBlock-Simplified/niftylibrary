package net.netcoding.nifty.common.minecraft.region;

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
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.minecraft.sound.Sound;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

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

	Item dropItem(Location location, ItemStack itemStack);

	Item dropItemNaturally(Location location, ItemStack itemStack);

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

	Chunk getChunkAt(int x, int z);

	default Chunk getChunkAt(Location location) {
		return this.getChunkAt(location.getBlockX(), location.getBlockZ());
	}

	default Chunk getChunkAt(Block block) {
		return this.getChunkAt(block.getLocation());
	}

	Difficulty getDifficulty();

	List<Entity> getEntities();

	@SuppressWarnings("unchecked")
	<T extends Entity> Collection<Entity> getEntitiesByClass(Class<T> clazz);

	@SuppressWarnings("unchecked")
	Collection<Entity> getEntitiesByClasses(Class<? extends Entity>... clazzes);

	Environment getEnvironment();

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

	Chunk[] getLoadedChunks();

	int getMaxHeight();

	int getMonsterSpawnLimit();

	String getName();

	Collection<Entity> getNearbyEntities(Location location, double x, double y, double z);

	List<Player> getPlayers();

	Location getSpawnLocation();

	int getSeaLevel();

	long getSeed();

	double getTemperature(int x, int z);

	int getThunderDuration();

	long getTicksPerAnimalSpawns();

	long getTicksPerMonsterSpawns();

	long getTime();

	int getWaterAnimalSpawnLimit();

	int getWeatherDuration();

	Border getWorldBorder();

	File getWorldFolder();

	Type getWorldType();

	UUID getUniqueId();

	boolean hasStorm();

	boolean isAutoSave();

	default boolean isChunkLoaded(Chunk chunk) {
		return this.isChunkLoaded(chunk.getX(), chunk.getZ());
	}

	default boolean isChunkLoaded(int x, int z) {
		return Arrays.stream(this.getLoadedChunks()).anyMatch(chunk -> chunk.getX() == x && chunk.getZ() == z);
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

	void playEffect(Location location, Effect effect, int data);

	void playEffect(Location location, Effect effect, int data, int radius);

	<T> void playEffect(Location location, Effect effect, T data);

	<T> void playEffect(Location location, Effect effect, T data, int radius);

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


	//Arrow spawnArrow(Location var1, Vector var2, float var3, float var4);

	//<T extends Arrow> T spawnArrow(Location var1, Vector var2, float var3, float var4, Class<T> var5);

	//boolean generateTree(Location var1, TreeType var2);

	//boolean generateTree(Location var1, TreeType var2, BlockChangeDelegate var3);

	//ChunkGenerator getGenerator();

	//List<BlockPopulator> getPopulators();

	//ChunkSnapshot getEmptyChunkSnapshot(int var1, int var2, boolean var3, boolean var4);


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

		private static final ConcurrentMap<Integer, Difficulty> BY_ID = new ConcurrentMap<>();
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

		private static final ConcurrentMap<Integer, Environment> BY_ID = new ConcurrentMap<>();
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

		private static final ConcurrentMap<String, Type> BY_NAME = new ConcurrentMap<>();
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