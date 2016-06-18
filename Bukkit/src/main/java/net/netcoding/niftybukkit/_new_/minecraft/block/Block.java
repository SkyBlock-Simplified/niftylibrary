package net.netcoding.niftybukkit._new_.minecraft.block;

import net.netcoding.niftybukkit._new_.minecraft.inventory.item.ItemStack;
import net.netcoding.niftybukkit._new_.minecraft.material.Material;
import net.netcoding.niftybukkit._new_.minecraft.region.Biome;
import net.netcoding.niftybukkit._new_.minecraft.region.Chunk;
import net.netcoding.niftybukkit._new_.minecraft.region.Location;
import net.netcoding.niftybukkit._new_.minecraft.region.World;

import java.util.Collection;

public interface Block {

	/**
	 * Breaks the block and spawns items as if a player had digged it
	 *
	 * @return True if the block was destroyed.
	 */
	boolean breakNaturally();

	/**
	 * Breaks the block and spawns items as if a player had digged it with a specific tool.
	 *
	 * @param tool The tool or item in hand used for digging.
	 * @return True of the block was destroyed.
	 */
	boolean breakNaturally(ItemStack tool);

	/**
	 * Returns the biome that this block resides in.
	 *
	 * @return Biome type containing this block.
	 */
	Biome getBiome();

	/**
	 * Returns the redstone power being provided to this block.
	 *
	 * @return The power level.
	 */
	int getBlockPower();

	/**
	 * Returns the redstone power being provided to this block face.
	 *
	 * @param face The face of the block to query or BlockFace.SELF for the block itself.
	 * @return The power level.
	 */
	int getBlockPower(BlockFace face);

	/**
	 * Gets the chunk which contains this block.
	 *
	 * @return Chunk containing this block.
	 */
	Chunk getChunk();

	/**
	 * Gets the data value for this block.
	 *
	 * @return Block-specific data value.
	 */
	byte getData();

	/**
	 * Returns a collection of items which would drop by destroying this block.
	 *
	 * @return A collection of dropped items for this type of block.
	 */
	Collection<ItemStack> getDrops();

	/**
	 * Returns a collection of items which would drop by destroying this block with a specific tool.
	 *
	 * @param tool The tool or item in hand used for digging.
	 * @return A collection of dropped items for this type of block.
	 */
	Collection<ItemStack> getDrops(ItemStack tool);

	/**
	 * Gets the face relation of this block compared to the given block.
	 * <p>
	 * Example:
	 * <p>
	 * {@link Block} current = world.getBlockAt(100, 100, 100);
	 * <p>
	 * {@link Block} target = world.getBlockAt(100, 101, 100);
	 * <p>
	 * {@link BlockFace} face = current.getFace(target);
	 * <p>
	 * face == {@link BlockFace#UP}
	 *
	 * @param block Block to compare against this block.
	 * @return BlockFace of this block which has the requred block, or null.
	 */
	BlockFace getFace(Block block);

	/**
	 * Gets the humidity of the biome of this block.
	 *
	 * @return Humidity of this block.
	 */
	double getHumidity();

	/**
	 * Gets the amount of light at this block between 0-15.
	 *
	 * @return Light level.
	 */
	byte getLightLevel();

	/**
	 * Gets the amount of light at this block from nearby blocks.
	 * <p>
	 * Any light given from other sources (such as the sun) will be ignored.
	 *
	 * @return Block light level.
	 */
	byte getLightFromBlocks();

	/**
	 * Gets the amount of light at this block from the sky.
	 * <p>
	 * Any light given from other sources (such as blocks/torches) will be ignored.
	 *
	 * @return Sky light level.
	 */
	byte getLightFromSky();

	/**
	 * Gets the location of this block.
	 *
	 * @return Location of this block.
	 */
	Location getLocation();

	/**
	 * Stores the location of the block in the provided Location object.
	 * <p>
	 * If the provided location is null, this method does nothing and returns null.
	 *
	 * @param location The location to copy this blocks location to.
	 * @return The previous value of the passed location object.
	 */
	default Location getLocation(Location location) {
		if (location == null)
			return null;

		Location old = this.getLocation();
		location.setPitch(old.getPitch());
		location.setX(old.getX());
		location.setY(old.getY());
		location.setYaw(old.getYaw());
		location.setZ(old.getZ());
		location.setWorld(old.getWorld());
		return old;
	}

	/**
	 * Returns the reaction of the block when moved by a piston.
	 *
	 * @return Piston reaction.
	 */
	PistonMoveReaction getPistonMoveReaction();

	/**
	 * Gets the block at the given offsets.
	 * @param modX X-coordinate offset
	 * @param modY Y-coordinate offset
	 * @param modZ Z-coordinate offset
	 * @return Block at the given offsets.
	 */
	Block getRelative(int modX, int modY, int modZ);

	/**
	 * Gets the block at the given face.
	 * <p>
	 * This method is equivalent to getRelative({@link BlockFace}, 1).
	 *
	 * @param face The facing block to return.
	 * @return Block at the given face.
	 * @see Block#getRelative(BlockFace, int)
	 */
	default Block getRelative(BlockFace face) {
		return getRelative(face, 1);
	}

	/**
	 * Gets the block at the given distance of the given face.
	 * <p>
	 * Example, The following code places water 2 blocks above the
	 * current block:
	 * <p>
	 * {@link Block} block = world.getBlockAt(100, 100, 100);
	 * <p>
	 * {@link Block} shower = block.getRelative({@link BlockFace#UP}, 2);
	 * <p>
	 * shower.setType({@link Material#WATER});
	 *
	 * @param face Direction of this block to return.
	 * @param distance How many blocks away.
	 * @return Block at the given distance and face.
	 */
	Block getRelative(BlockFace face, int distance);

	/**
	 * Captures the current state of this block. You may then cast that state
	 * into any accepted type, such as Furnace or Sign.
	 * <p>
	 * The returned object will never be updated, and you are not guaranteed that
	 * (for example) a sign is still a sign after you capture its state.
	 *
	 * @return Current state of this block.
	 */
	BlockState getState();

	/**
	 * Gets the temperature of the biome of this block.
	 *
	 * @return Temperature of this block.
	 */
	double getTemperature();

	/**
	 * Gets the type of this block.
	 *
	 * @return Block type.
	 */
	Material getType();

	/**
	 * Gets the type id of this block.
	 *
	 * @return Block type id.
	 */
	int getTypeId();

	/**
	 * Gets the world which contains this block.
	 *
	 * @return World containing this block.
	 */
	World getWorld();

	/**
	 * Gets the x-coordinate of this block.
	 *
	 * @return X-coordinate of this block.
	 */
	int getX();

	/**
	 * Gets the y-coordinate of this block.
	 *
	 * @return Y-coordinate of this block.
	 */
	int getY();

	/**
	 * Gets the z-coordinate of this block.
	 *
	 * @return Z-coordinate of this block.
	 */
	int getZ();

	/**
	 * Returns true if the block face is being indirectly powered by redstone.
	 *
	 * @param face The block face.
	 * @return True if the block face is indirectly powered.
	 */
	boolean isBlockFaceIndirectlyPowered(BlockFace face);

	/**
	 * Returns true if the block face is being powered by redstone.
	 *
	 * @param face The block face.
	 * @return True if the block face is powered.
	 */
	boolean isBlockFacePowered(BlockFace face);

	/**
	 * Returns true if the block is being indirectly powered by redstone.
	 *
	 * @return True if the block is indirectly powered.
	 */
	boolean isBlockIndirectlyPowered();

	/**
	 * Returns true if the block is being powered by redstone.
	 *
	 * @return True if the block is powered.
	 */
	boolean isBlockPowered();

	/**
	 * Checks if this block is empty.
	 * <p>
	 * A block is considered empty when {@link Block#getType()} returns {@link Material#AIR}
	 *
	 * @return True if this block is empty.
	 */
	boolean isEmpty();

	/**
	 * Checks if this block is liquid.
	 * <p>
	 * A block is considered liquid when {@link Block#getType()} returns {@link Material#WATER},
	 * {@link Material#STATIONARY_WATER}, {@link Material#LAVA} or {@link Material#STATIONARY_LAVA}
	 *
	 * @return True if this block is liquid.
	 */
	boolean isLiquid();

	/**
	 * Sets the biome that this block resides in.
	 *
	 * @param biome New biome type for this block
	 */
	void setBiome(Biome biome);

	/**
	 * Sets the data value of this block.
	 *
	 * @param data New block-specific data value.
	 */
	default void setData(byte data) {
		this.setData(data, true);
	}

	/**
	 * Sets the data value of this block.
	 *
	 * @param data New block-specific data value.
	 * @param applyPhysics False to cancel physics on the changed block.
	 */
	void setData(byte data, boolean applyPhysics);

	/**
	 * Sets the type of this block.
	 *
	 * @param material Type to change this block to.
	 * @return True if the block was changed.
	 */
	default boolean setType(Material material) {
		return this.setTypeId(material.getId());
	}

	/**
	 * Sets the type of this block.
	 *
	 * @param material Type to change this block to.
	 * @param applyPhysics False to cancel physics on the changed block.
	 * @return True if the block was changed.
	 */
	default boolean setType(Material material, boolean applyPhysics) {
		return this.setTypeId(material.getId(), applyPhysics);
	}

	/**
	 * Sets the type and data of this block.
	 *
	 * @param material Type to change this block to.
	 * @param data The data value to change this block to.
	 * @return True if the block was changed.
	 */
	default boolean setTypeAndData(Material material, byte data) {
		return this.setTypeIdAndData(material.getId(), data);
	}

	/**
	 * Sets the type and data of this block.
	 *
	 * @param material Type to change this block to.
	 * @param data The data value to change this block to.
	 * @param applyPhysics False to cancel physics on the changed block.
	 * @return True if the block was changed.
	 */
	default boolean setTypeAndData(Material material, byte data, boolean applyPhysics) {
		return this.setTypeIdAndData(material.getId(), data, applyPhysics);
	}

	/**
	 * Sets the type of this block.
	 *
	 * @param type Type to change this block to.
	 * @return True if the block was changed.
	 */
	default boolean setTypeId(int type) {
		return this.setTypeId(type, true);
	}

	/**
	 * Sets the type of this block.
	 *
	 * @param type Type to change this block to.
	 * @param applyPhysics False to cancel physics on the changed block.
	 * @return True if the block was changed.
	 */
	boolean setTypeId(int type, boolean applyPhysics);

	/**
	 * Sets the type and data of this block.
	 *
	 * @param type Type to change this block to.
	 * @param data The data value to change this block to.
	 * @return True if the block was changed.
	 */
	default boolean setTypeIdAndData(int type, byte data) {
		return this.setTypeIdAndData(type, data, true);
	}

	/**
	 * Sets the type and data of this block.
	 *
	 * @param type Type to change this block to.
	 * @param data The data value to change this block to.
	 * @param applyPhysics False to cancel physics on the changed block.
	 * @return True if the block was changed.
	 */
	boolean setTypeIdAndData(int type, byte data, boolean applyPhysics);

}