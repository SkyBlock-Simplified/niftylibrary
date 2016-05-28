package net.netcoding.niftybukkit.util.json;

import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.minecraft.nbt.NbtFactory;
import net.netcoding.niftybukkit.minecraft.nbt.NbtItemCompound;
import net.netcoding.niftybukkit.reflection.BukkitReflection;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.util.json.JsonMessage;
import net.netcoding.niftycore.util.json.JsonString;
import net.netcoding.niftycore.util.json.TextualComponent;
import net.netcoding.niftycore.util.json.events.HoverEvent;
import org.bukkit.Achievement;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class BukkitJsonMessage extends JsonMessage<BukkitJsonMessage> {

	private static final BukkitReflection NMS_ACHIEVEMENT = new BukkitReflection("Achievement", MinecraftPackage.MINECRAFT_SERVER);
	private static final BukkitReflection NMS_STATISTIC = new BukkitReflection("Statistic", MinecraftPackage.MINECRAFT_SERVER);
	private static final BukkitReflection CRAFT_STATISTIC = new BukkitReflection("CraftStatistic", MinecraftPackage.CRAFTBUKKIT);

	/**
	 * Creates a JSON message without text.
	 */
	public BukkitJsonMessage() {
		this((TextualComponent)null);
	}

	/**
	 * Creates a JSON message with text.
	 *
	 * @param firstPartText The existing text in the message.
	 */
	public BukkitJsonMessage(String firstPartText) {
		this(TextualComponent.rawText(firstPartText));
	}

	public BukkitJsonMessage(TextualComponent firstPartText) {
		super(firstPartText);
	}

	/**
	 * Set the behavior of the current editing component to display information about an achievement when the client hovers over the text.
	 * <p>
	 * Tooltips do not inherit display characteristics, such as color and styles, from the message component on which they are applied.
	 *
	 * @param name The name of the achievement to display, excluding the "achievement." prefix.
	 * @return This builder instance.
	 */
	public BukkitJsonMessage achievementTooltip(final String name) {
		onHover(HoverEvent.Type.ACHIEVEMENT, new JsonString("achievement." + name));
		return this;
	}

	/**
	 * Set the behavior of the current editing component to display information about an achievement when the client hovers over the text.
	 * <p>
	 * Tooltips do not inherit display characteristics, such as color and styles, from the message component on which they are applied.
	 *
	 * @param achievement The achievement to display.
	 * @return This builder instance.
	 */
	public BukkitJsonMessage achievementTooltip(final Achievement achievement) {
		Object achievementObj = CRAFT_STATISTIC.invokeMethod(NMS_ACHIEVEMENT.getClazz(), null, achievement);
		return achievementTooltip((String)NMS_ACHIEVEMENT.getValue(Achievement.class, achievementObj));
	}

	/**
	 * Set the behavior of the current editing component to display information about a parameterless statistic when the client hovers over the text.
	 * <p>
	 * Tooltips do not inherit display characteristics, such as color and styles, from the message component on which they are applied.
	 *
	 * @param statistic The statistic to display.
	 * @return This builder instance.
	 * @exception IllegalArgumentException If the statistic requires a parameter which was not supplied.
	 */
	public BukkitJsonMessage statisticTooltip(final Statistic statistic) {
		Statistic.Type type = statistic.getType();

		if (type != Statistic.Type.UNTYPED)
			throw new IllegalArgumentException("That statistic requires an additional type parameter!");

		Object statisticObj = CRAFT_STATISTIC.invokeMethod(NMS_STATISTIC.getClazz(), null, statistic);
		return achievementTooltip((String)NMS_ACHIEVEMENT.getValue("name", statisticObj));
	}

	/**
	 * Set the behavior of the current editing component to display information about a statistic parameter with a material when the client hovers over the text.
	 * <p>
	 * Tooltips do not inherit display characteristics, such as color and styles, from the message component on which they are applied.
	 *
	 * @param statistic The statistic to display.
	 * @param material The sole material parameter to the statistic.
	 * @return This builder instance.
	 * @exception IllegalArgumentException If the statistic requires a parameter which was not supplied, or was supplied a parameter that was not required.
	 */
	public BukkitJsonMessage statisticTooltip(final Statistic statistic, Material material) {
		Statistic.Type type = statistic.getType();

		if (type == Statistic.Type.UNTYPED)
			throw new IllegalArgumentException("That statistic needs no additional parameter!");

		if ((type == Statistic.Type.BLOCK && material.isBlock()) || type == Statistic.Type.ENTITY)
			throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");

		Object statisticObj = CRAFT_STATISTIC.invokeMethod(NMS_STATISTIC.getClazz(), null, statistic, material);
		return achievementTooltip((String)NMS_ACHIEVEMENT.getValue("name", statisticObj));
	}

	/**
	 * Set the behavior of the current editing component to display information about a statistic parameter with an entity type when the client hovers over the text.
	 * <p>
	 * Tooltips do not inherit display characteristics, such as color and styles, from the message component on which they are applied.
	 *
	 * @param statistic The statistic to display.
	 * @param entity The sole entity type parameter to the statistic.
	 * @return This builder instance.
	 * @exception IllegalArgumentException If the statistic requires a parameter which was not supplied, or was supplied a parameter that was not required.
	 */
	public BukkitJsonMessage statisticTooltip(final Statistic statistic, EntityType entity) {
		Statistic.Type type = statistic.getType();

		if (type == Statistic.Type.UNTYPED)
			throw new IllegalArgumentException("That statistic needs no additional parameter!");

		if (type != Statistic.Type.ENTITY)
			throw new IllegalArgumentException("Wrong parameter type for that statistic - needs " + type + "!");

		Object statisticObj = CRAFT_STATISTIC.invokeMethod(NMS_STATISTIC.getClazz(), null, statistic, entity);
		return achievementTooltip((String)NMS_ACHIEVEMENT.getValue("name", statisticObj));
	}

	/**
	 * Set the behavior of the current editing component to display information about an item when the client hovers over the text.
	 * <p>
	 * Tooltips do not inherit display characteristics, such as color and styles, from the message component on which they are applied.
	 *
	 * @param itemJson A string representing the JSON-serialized NBT data tag of an {@link ItemStack}.
	 * @return This builder instance.
	 */
	public BukkitJsonMessage itemTooltip(final String itemJson) {
		onHover(HoverEvent.Type.ITEM, new JsonString(itemJson)); // Seems a bit hacky, considering we have a JSON object as a parameter
		return this;
	}

	/**
	 * Set the behavior of the current editing component to display information about an item when the client hovers over the text.
	 * <p>
	 * Tooltips do not inherit display characteristics, such as color and styles, from the message component on which they are applied.
	 *
	 * @param itemStack The stack for which to display information.
	 * @return This builder instance.
	 */
	public BukkitJsonMessage itemTooltip(final ItemStack itemStack) {
		NbtItemCompound itemCompound = (NbtItemCompound)new ItemData(itemStack).getNbt();
		return itemTooltip(NbtFactory.NBT_TAG_COMPOUND.invokeMethod(NbtFactory.NBT_TAG_COMPOUND.getClazz(), itemCompound.getHandle(), itemCompound).toString());
	}

}