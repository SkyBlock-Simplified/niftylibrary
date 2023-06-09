package net.netcoding.nifty.common.util.json;

import net.netcoding.nifty.common.api.nbt.NbtFactory;
import net.netcoding.nifty.common.api.nbt.NbtItemCompound;
import net.netcoding.nifty.common.minecraft.Achievement;
import net.netcoding.nifty.common.minecraft.Statistic;
import net.netcoding.nifty.common.minecraft.entity.EntityType;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.common.reflection.MinecraftReflection;
import net.netcoding.nifty.common.reflection.MinecraftPackage;
import net.netcoding.nifty.core.reflection.exceptions.ReflectionException;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;
import net.netcoding.nifty.core.util.json.JsonMessage;
import net.netcoding.nifty.core.util.json.JsonString;
import net.netcoding.nifty.core.util.json.TextualComponent;
import net.netcoding.nifty.core.util.json.events.HoverEvent;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class MinecraftJsonMessage extends JsonMessage<MinecraftJsonMessage> {

	private static final MinecraftReflection NMS_ENTITY_TYPES = new MinecraftReflection("EntityTypes", MinecraftPackage.MINECRAFT_SERVER);
	private static final MinecraftReflection NMS_MONSTER_EGG_INFO = new MinecraftReflection("EntityTypes$MonsterEggInfo", MinecraftPackage.MINECRAFT_SERVER);
	private static final MinecraftReflection NMS_STATISTIC = new MinecraftReflection("Statistic", MinecraftPackage.MINECRAFT_SERVER);
	private static final MinecraftReflection NMS_STATISTIC_LIST = new MinecraftReflection("StatisticList", MinecraftPackage.MINECRAFT_SERVER);
	private static final ConcurrentSet<Statistic> STATISTIC_MATERIALS = Concurrent.newSet();
	private static final ConcurrentMap<Statistic, String> NMS_MAP = Concurrent.newMap();
	private static final String NMS_EGGINFO_FIELD_NAME;
	private static final String NMS_MONSTEREGG_KILL_FIELD_NAME;
	private static final String NMS_MONSTEREGG_KILLEDBY_FIELD_NAME;

	static {
		STATISTIC_MATERIALS.add(Statistic.CRAFT_ITEM);
		STATISTIC_MATERIALS.add(Statistic.MINE_BLOCK);
		STATISTIC_MATERIALS.add(Statistic.USE_ITEM);
		STATISTIC_MATERIALS.add(Statistic.BREAK_ITEM);
		STATISTIC_MATERIALS.add(Statistic.PICKUP);
		STATISTIC_MATERIALS.add(Statistic.DROP);
		String eggInfoFieldName = "";
		String killFieldName = "";
		String killedbyFieldName = "";

		for (Field field : NMS_STATISTIC_LIST.getClazz().getDeclaredFields()) {
			if (field.getClass().isArray()) {
				try {
					Object stat = ((Object[]) field.get(null))[0];
					String name = NMS_STATISTIC.getValue(String.class, stat);
					STATISTIC_MATERIALS.stream().filter(statistic -> name.startsWith(statistic.getName())).forEach(statistic -> NMS_MAP.put(statistic, field.getName()));
				} catch (Exception ex) {
					throw new ReflectionException("Failed to load nms mapping for statistics!", ex);
				}
			}
		}

		for (Field field : NMS_ENTITY_TYPES.getClazz().getDeclaredFields()) {
			if (Map.class.isAssignableFrom(field.getType())) {
				ParameterizedType type = (ParameterizedType)field.getGenericType();

				if (type.getActualTypeArguments().length == 2) {
					if (type.getActualTypeArguments()[1].equals(NMS_MONSTER_EGG_INFO.getClazz())) {
						eggInfoFieldName = field.getName();
						Map<?, ?> eggInfo = (Map<?, ?>)NMS_ENTITY_TYPES.getValue(eggInfoFieldName, null);
						Object eggSample = eggInfo.get(EntityType.SPIDER.getName());

						for (Field eggField : NMS_MONSTER_EGG_INFO.getClazz().getDeclaredFields()) {
							if (eggField.getType().equals(NMS_STATISTIC.getClazz())) {
								try {
									Object statisticObj = NMS_MONSTER_EGG_INFO.getValue(field.getName(), eggSample);
									String name = NMS_STATISTIC.getValue(String.class, statisticObj);

									if (name.startsWith(Statistic.KILL_ENTITY.getName()))
										killFieldName = field.getName();
									else if (name.startsWith(Statistic.ENTITY_KILLED_BY.getName()))
										killedbyFieldName = field.getName();
								} catch (Exception ignore) { }
							}
						}
					}
				}
			}
		}

		if (StringUtil.isEmpty(eggInfoFieldName))
			throw new ReflectionException("Failed to locate eggInfo field in EntityTypes!");

		if (StringUtil.isEmpty(killFieldName) || StringUtil.isEmpty(killedbyFieldName))
			throw new ReflectionException("Failed to locate MonsterEggInfo Statistic field names!");

		NMS_EGGINFO_FIELD_NAME = eggInfoFieldName;
		NMS_MONSTEREGG_KILL_FIELD_NAME = killFieldName;
		NMS_MONSTEREGG_KILLEDBY_FIELD_NAME = killedbyFieldName;
	}

	/**
	 * Creates a JSON message without text.
	 */
	public MinecraftJsonMessage() {
		this((TextualComponent)null);
	}

	/**
	 * Creates a JSON message with text.
	 *
	 * @param firstPartText The existing text in the message.
	 */
	public MinecraftJsonMessage(String firstPartText) {
		this(TextualComponent.rawText(firstPartText));
	}

	/**
	 * Creates a JSON message with component.
	 *
	 * @param firstPartText The existing text component in the message.
	 */
	public MinecraftJsonMessage(TextualComponent firstPartText) {
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
	public MinecraftJsonMessage achievementTooltip(final String name) {
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
	public MinecraftJsonMessage achievementTooltip(final Achievement achievement) {
		return achievementTooltip(achievement.getName());
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
	public MinecraftJsonMessage statisticTooltip(final Statistic statistic) {
		Statistic.Type type = statistic.getType();

		if (type != Statistic.Type.UNTYPED)
			throw new IllegalArgumentException(StringUtil.format("That statistic requires an additional ''{0}'' parameter!", type.name()));

		Object statisticObj = NMS_STATISTIC_LIST.invokeMethod(NMS_STATISTIC.getClazz(), null, statistic.getName());
		return achievementTooltip(NMS_STATISTIC.getValue(String.class, statisticObj));
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
	public MinecraftJsonMessage statisticTooltip(final Statistic statistic, Material material) {
		Statistic.Type type = statistic.getType();

		if (type == Statistic.Type.UNTYPED)
			throw new IllegalArgumentException("That statistic needs no additional parameter!");

		if ((type == Statistic.Type.BLOCK && material.isBlock()) || type == Statistic.Type.ENTITY)
			throw new IllegalArgumentException(StringUtil.format("Wrong parameter type for that statistic (Requires ''{0}'')!", type.name()));

		if (NMS_MAP.containsKey(statistic)) {
			String fieldName = NMS_MAP.get(statistic);
			Object[] statistics = (Object[])NMS_STATISTIC_LIST.getValue(fieldName, null);
			Object statisticObj = statistics[material.getId()];
			return achievementTooltip(NMS_STATISTIC.getValue(String.class, statisticObj));
		}

		return this;
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
	public MinecraftJsonMessage statisticTooltip(final Statistic statistic, EntityType entity) {
		Statistic.Type type = statistic.getType();

		if (type == Statistic.Type.UNTYPED)
			throw new IllegalArgumentException("That statistic needs no additional parameter!");

		if (type != Statistic.Type.ENTITY)
			throw new IllegalArgumentException(StringUtil.format("Wrong parameter type for that statistic (Requires ''{0}'')!", type.name()));

		Map<?, ?> eggInfo = (Map<?, ?>)NMS_ENTITY_TYPES.getValue(NMS_EGGINFO_FIELD_NAME, null);
		Object eggObj = eggInfo.get(entity.getName());

		if (eggObj != null) {
			String fieldName = (Statistic.KILL_ENTITY == statistic ? NMS_MONSTEREGG_KILL_FIELD_NAME : (Statistic.ENTITY_KILLED_BY == statistic ? NMS_MONSTEREGG_KILLEDBY_FIELD_NAME : ""));

			if (StringUtil.notEmpty(fieldName)) {
				Object statisticObj = NMS_MONSTER_EGG_INFO.getValue(fieldName, eggObj);
				return achievementTooltip(NMS_STATISTIC.getValue(String.class, statisticObj));
			}
		}

		return this;
	}

	/**
	 * Set the behavior of the current editing component to display information about an item when the client hovers over the text.
	 * <p>
	 * Tooltips do not inherit display characteristics, such as color and styles, from the message component on which they are applied.
	 *
	 * @param itemJson A string representing the JSON-serialized NBT data tag of an {@link ItemStack}.
	 * @return This builder instance.
	 */
	public MinecraftJsonMessage itemTooltip(final String itemJson) {
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
	public MinecraftJsonMessage itemTooltip(final ItemStack itemStack) {
		NbtItemCompound itemCompound = (NbtItemCompound)itemStack.getNbt();
		return itemTooltip(NbtFactory.NBT_TAG_COMPOUND.invokeMethod(NbtFactory.NBT_TAG_COMPOUND.getClazz(), itemCompound.getHandle(), itemCompound).toString());
	}

}