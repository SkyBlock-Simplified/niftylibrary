package net.netcoding.nifty.common._new_.minecraft.material.types;

import net.netcoding.nifty.common._new_.minecraft.block.BlockFace;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public enum MushroomBlockTexture {

	/**
	 * Pores on all faces.
	 */
	ALL_PORES(0, null),
	/**
	 * Cap texture on the top, north and west faces, pores on remaining sides.
	 */
	CAP_NORTH_WEST(1, BlockFace.NORTH_WEST),
	/**
	 * Cap texture on the top and north faces, pores on remaining sides.
	 */
	CAP_NORTH(2, BlockFace.NORTH),
	/**
	 * Cap texture on the top, north and east faces, pores on remaining sides.
	 */
	CAP_NORTH_EAST(3, BlockFace.NORTH_EAST),
	/**
	 * Cap texture on the top and west faces, pores on remaining sides.
	 */
	CAP_WEST(4, BlockFace.WEST),
	/**
	 * Cap texture on the top face, pores on remaining sides.
	 */
	CAP_TOP(5, BlockFace.UP),
	/**
	 * Cap texture on the top and east faces, pores on remaining sides.
	 */
	CAP_EAST(6, BlockFace.EAST),
	/**
	 * Cap texture on the top, south and west faces, pores on remaining sides.
	 */
	CAP_SOUTH_WEST(7, BlockFace.SOUTH_WEST),
	/**
	 * Cap texture on the top and south faces, pores on remaining sides.
	 */
	CAP_SOUTH(8, BlockFace.SOUTH),
	/**
	 * Cap texture on the top, south and east faces, pores on remaining sides.
	 */
	CAP_SOUTH_EAST(9, BlockFace.SOUTH_EAST),
	/**
	 * Stem texture on the north, east, south and west faces, pores on top and
	 * bottom.
	 */
	STEM_SIDES(10, null),
	/**
	 * Cap texture on all faces.
	 */
	ALL_CAP(14, BlockFace.SELF),
	/**
	 * Stem texture on all faces.
	 */
	ALL_STEM(15, null);

	private static final ConcurrentMap<Byte, MushroomBlockTexture> BY_DATA = Concurrent.newMap();
	private static final ConcurrentMap<BlockFace, MushroomBlockTexture> BY_BLOCKFACE = Concurrent.newMap();

	private final Byte data;
	private final BlockFace capFace;

	MushroomBlockTexture(int data, BlockFace capFace) {
		this.data = (byte)data;
		this.capFace = capFace;
	}

	public byte getData() {
		return this.data;
	}

	public BlockFace getCapFace() {
		return this.capFace;
	}

	public static MushroomBlockTexture getByData(byte data) {
		return BY_DATA.get(data);
	}

	public static MushroomBlockTexture getCapByFace(BlockFace face) {
		return BY_BLOCKFACE.get(face);
	}

	static {
		for (MushroomBlockTexture type : values()) {
			BY_DATA.put(type.getData(), type);
			BY_BLOCKFACE.put(type.getCapFace(), type);
		}
	}

}