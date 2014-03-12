package net.netcoding.niftybukkit.yaml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashSet;

import org.bukkit.plugin.java.JavaPlugin;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.yaml.converters.Converter;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConverterException;

public class InternalConverter extends BukkitHelper {

	private static transient LinkedHashSet<Converter> converters = new LinkedHashSet<>();

	static {
		try {
			addInternalConverter(net.netcoding.niftybukkit.yaml.converters.Primitive.class);
			addInternalConverter(net.netcoding.niftybukkit.yaml.converters.Config.class);
			addInternalConverter(net.netcoding.niftybukkit.yaml.converters.List.class);
			addInternalConverter(net.netcoding.niftybukkit.yaml.converters.Map.class);
			addInternalConverter(net.netcoding.niftybukkit.yaml.converters.Array.class);
		} catch (InvalidConverterException ex) {
			throw new IllegalStateException(ex);
		}
	}

	protected InternalConverter(JavaPlugin plugin) {
		super(plugin);
	}

	/**
	 * Add a Custom Converter. A Converter can take Objects and return a pretty Object which gets saved/loaded from
	 * the Converter. How a Converter must be build can be looked up in the Converter Interface.
	 *
	 * @param addConverter Converter to be added
	 * @throws InvalidConverterException If the Converter has any errors this Exception tells you what
	 */
	public void addConverter(Class<? extends Converter> converter) throws InvalidConverterException {
		addInternalConverter(converter);
	}

	private static void addInternalConverter(Class<? extends Converter> converter) throws InvalidConverterException {
		try {
			converters.add((Converter)converter.getConstructor(InternalConverter.class).newInstance(InternalConverter.class));
		} catch (NoSuchMethodException e) {
			throw new InvalidConverterException("Converter does not implement a Constructor which takes the InternalConverter instance", e);
		} catch (InvocationTargetException e) {
			throw new InvalidConverterException("Converter could not be invoked", e);
		} catch (InstantiationException e) {
			throw new InvalidConverterException("Converter could not be instantiated", e);
		} catch (IllegalAccessException e) {
			throw new InvalidConverterException("Converter does not implement a public Constructor which takes the InternalConverter instance", e);
		}
	}

	public Converter getConverter(Class<?> type) {
		for (Converter converter : converters) {
			if (converter.supports(type))
				return converter;
		}

		return null;
	}

	public void fromConfig(Config config, Field field, ConfigSection root, String path) throws Exception {
		Object obj = field.get(config);
		Converter converter;

		if (obj != null) {
			converter = getConverter(obj.getClass());

			if (converter != null) {
				field.set(config, converter.fromConfig(obj.getClass(), root.get(path), (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
				return;
			} else {
				converter = getConverter(field.getType());

				if (converter != null) {
					field.set(config, converter.fromConfig(field.getType(), root.get(path), (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
					return;
				}
			}
		} else {
			converter = getConverter(field.getType());

			if (converter != null) {
				field.set(config, converter.fromConfig(field.getType(), root.get(path), (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
				return;
			}
		}

		field.set(config, root.get(path));
	}

	public void toConfig(Config config, Field field, ConfigSection root, String path) throws Exception {
		Object obj = field.get(config);
		Converter converter;

		if (obj != null) {
			converter = getConverter(obj.getClass());

			if (converter != null) {
				root.set(path, converter.toConfig(obj.getClass(), obj, (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType)field.getGenericType() : null));
				return;
			} else {
				converter = getConverter(field.getType());

				if (converter != null) {
					root.set(path, converter.toConfig(field.getType(), obj, (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType)field.getGenericType() : null));
					return;
				}
			}
		} else {
			converter = getConverter(field.getType());

			if (converter != null) {
				root.set(path, converter.toConfig(field.getType(), obj, (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType)field.getGenericType() : null));
				return;
			}
		}

		root.set(path, obj);
	}

}