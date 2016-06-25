package net.netcoding.nifty.common.minecraft;

import com.google.common.base.CaseFormat;
import net.netcoding.nifty.core.util.StringUtil;

/**
 * Represents a countable statistic, which is tracked by the server.
 */
public enum Statistic {

	DAMAGE_DEALT,
	DAMAGE_TAKEN,
	DEATHS,
	MOB_KILLS,
	PLAYER_KILLS,
	FISH_CAUGHT,
	ANIMALS_BRED,
	TREASURE_FISHED,
	JUNK_FISHED,
	LEAVE_GAME,
	JUMP,
	DROP(Type.ITEM),
	PICKUP(Type.ITEM),
	PLAY_ONE_TICK("playOneMinute"),
	WALK_ONE_CM,
	SWIM_ONE_CM,
	FALL_ONE_CM,
	SNEAK_TIME,
	CLIMB_ONE_CM,
	FLY_ONE_CM,
	DIVE_ONE_CM,
	MINECART_ONE_CM,
	BOAT_ONE_CM,
	PIG_ONE_CM,
	HORSE_ONE_CM,
	SPRINT_ONE_CM,
	CROUCH_ONE_CM,
	AVIATE_ONE_CM,
	MINE_BLOCK(Type.BLOCK),
	USE_ITEM(Type.ITEM),
	BREAK_ITEM(Type.ITEM),
	CRAFT_ITEM(Type.ITEM),
	KILL_ENTITY(Type.ENTITY),
	ENTITY_KILLED_BY(Type.ENTITY),
	TIME_SINCE_DEATH,
	TALKED_TO_VILLAGER,
	TRADED_WITH_VILLAGER,
	CAKE_SLICES_EATEN,
	CAULDRON_FILLED,
	CAULDRON_USED,
	ARMOR_CLEANED,
	BANNER_CLEANED,
	BREWINGSTAND_INTERACTION,
	BEACON_INTERACTION,
	DROPPER_INSPECTED,
	HOPPER_INSPECTED,
	DISPENSER_INSPECTED,
	NOTEBLOCK_PLAYED,
	NOTEBLOCK_TUNED,
	FLOWER_POTTED,
	TRAPPED_CHEST_TRIGGERED,
	ENDERCHEST_OPENED,
	ITEM_ENCHANTED,
	RECORD_PLAYED,
	FURNACE_INTERACTION,
	CRAFTING_TABLE_INTERACTION,
	CHEST_OPENED,
	SLEEP_IN_BED;

	private final Type type;
	private final String name;

	Statistic() {
		this(Type.UNTYPED, null);
	}

	Statistic(Type type) {
		this(type, null);
	}

	Statistic(String name) {
		this(Type.UNTYPED, name);
	}

	Statistic(Type type, String name) {
		this.type = type;
		this.name = StringUtil.format("stat.{0}", (StringUtil.notEmpty(name) ? name : CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, this.name())));
	}

	/**
	 * Gets the nms name of this statistic.
	 *
	 * @return The nms name of this statistic.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the type of this statistic.
	 *
	 * @return the type of this statistic.
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Checks if this is a substatistic.
	 * <p>
	 * A substatistic exists en masse for each block, item, or entitytype, depending on
	 * {@link #getType()}.
	 * <p>
	 * This is a redundant method and equivalent to checking
	 * <code>getType() != Type.UNTYPED</code>
	 *
	 * @return true if this is a substatistic
	 */
	public boolean isSubstatistic() {
		return Type.UNTYPED != this.getType();
	}

	/**
	 * Checks if this is a substatistic dealing with blocks.
	 * <p>
	 * This is a redundant method and equivalent to checking
	 * <code>getType() == Type.BLOCK</code>
	 *
	 * @return true if this deals with blocks
	 */
	public boolean isBlock() {
		return Type.BLOCK == this.getType();
	}

	/**
	 * The type of statistic.
	 */
	public enum Type {

		/**
		 * Statistics of this type do not require a qualifier.
		 */
		UNTYPED,

		/**
		 * Statistics of this type require an Item Material qualifier.
		 */
		ITEM,

		/**
		 * Statistics of this type require a Block Material qualifier.
		 */
		BLOCK,

		/**
		 * Statistics of this type require an EntityType qualifier.
		 */
		ENTITY

	}

}