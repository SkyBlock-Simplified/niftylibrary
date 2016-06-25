package net.netcoding.nifty.common._new_.minecraft;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

public enum Art {

	KEBAB(0, 1, 1),
	AZTEC(1, 1, 1),
	ALBAN(2, 1, 1),
	AZTEC2(3, 1, 1),
	BOMB(4, 1, 1),
	PLANT(5, 1, 1),
	WASTELAND(6, 1, 1),
	POOL(7, 2, 1),
	COURBET(8, 2, 1),
	SEA(9, 2, 1),
	SUNSET(10, 2, 1),
	CREEBET(11, 2, 1),
	WANDERER(12, 1, 2),
	GRAHAM(13, 1, 2),
	MATCH(14, 2, 2),
	BUST(15, 2, 2),
	STAGE(16, 2, 2),
	VOID(17, 2, 2),
	SKULL_AND_ROSES(18, 2, 2),
	WITHER(19, 2, 2),
	FIGHTERS(20, 4, 2),
	POINTER(21, 4, 4),
	PIGSCENE(22, 4, 4),
	BURNINGSKULL(23, 4, 4),
	SKELETON(24, 4, 3),
	DONKEYKONG(25, 4, 3);

	private static final ConcurrentMap<String, Art> BY_NAME = Concurrent.newMap();
	private static final ConcurrentMap<Integer, Art> BY_ID = Concurrent.newMap();
	private final int id;
	private final int width;
	private final int height;

	static {
		for (Art art : values()) {
			BY_ID.put(art.getId(), art);
			BY_NAME.put(art.name().toLowerCase().replaceAll("_", ""), art);
		}
	}

	Art(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}

	/**
	 * Gets the height of the painting, in blocks.
	 *
	 * @return The height of the painting, in blocks.
	 */
	public int getBlockHeight() {
		return this.height;
	}

	/**
	 * Gets the width of the painting, in blocks.
	 *
	 * @return The width of the painting, in blocks.
	 */
	public int getBlockWidth() {
		return this.width;
	}

	/**
	 * Get the ID of this painting.
	 *
	 * @return The ID of this painting.
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * Get a painting by its numeric ID.
	 *
	 * @param id The ID.
	 * @return The painting.
	 */
	public static Art getById(int id) {
		return BY_ID.get(id);
	}

	/**
	 * Get a painting by its unique name
	 * <p>
	 * This ignores underscores and capitalization
	 *
	 * @param name The name
	 * @return The painting
	 */
	public static Art getByName(String name) {
		Preconditions.checkArgument(StringUtil.notEmpty(name), "Name cannot be NULL!");
		return BY_NAME.get(name.toLowerCase().replaceAll("_", ""));
	}

}