package net.netcoding.niftybukkit.yaml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashSet;

import net.netcoding.niftybukkit.yaml.converters.Converter;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConverterException;

public class InternalConverter {

	private final transient LinkedHashSet<Converter> converters = new LinkedHashSet<>();

	public InternalConverter() {
		try {
			addConverter(net.netcoding.niftybukkit.yaml.converters.Array.class);
			addConverter(net.netcoding.niftybukkit.yaml.converters.Block.class);
			addConverter(net.netcoding.niftybukkit.yaml.converters.Config.class);
			addConverter(net.netcoding.niftybukkit.yaml.converters.ItemStack.class);
			addConverter(net.netcoding.niftybukkit.yaml.converters.List.class);
			addConverter(net.netcoding.niftybukkit.yaml.converters.Location.class);
			addConverter(net.netcoding.niftybukkit.yaml.converters.Map.class);
			addConverter(net.netcoding.niftybukkit.yaml.converters.Primitive.class);
			addConverter(net.netcoding.niftybukkit.yaml.converters.Set.class);
		} catch (InvalidConverterException e) {
			throw new IllegalStateException(e);
		}
	}

	public void addConverter(Class<? extends Converter> converter) throws InvalidConverterException {
		try {
			converters.add(converter.getConstructor(InternalConverter.class).newInstance(this));
		} catch (NoSuchMethodException e) {
			throw new InvalidConverterException("Converter does not implement a constructor which takes the InternalConverter instance!", e);
		} catch (InvocationTargetException e) {
			throw new InvalidConverterException("Converter could not be invoked!", e);
		} catch (InstantiationException ex) {
			throw new InvalidConverterException("Converter could not be instantiated!", ex);
		} catch (IllegalAccessException ex) {
			throw new InvalidConverterException("Converter does not implement a public Constructor which takes the InternalConverter instance!", ex);
		}
	}

	public Converter getConverter(Class<?> type) {
		for(Converter converter : this.converters) {
			if (converter.supports(type))
				return converter;
		}

		return null;
	}

	public void fromConfig(Config config, Field field, ConfigSection root, String path) throws Exception {
		Object obj = field.get(config);
		Converter converter;

		if (obj != null) {
			converter = this.getConverter(obj.getClass());

			if (converter != null) {
				field.set(config, converter.fromConfig(obj.getClass(), root.get(path), (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
				return;
			} else {
				converter = this.getConverter(field.getType());

				if (converter != null) {
					field.set(config, converter.fromConfig(field.getType(), root.get(path), (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
					return;
				}
			}
		} else {
			converter = this.getConverter(field.getType());

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
			converter = this.getConverter(obj.getClass());

			if (converter != null) {
				root.set(path, converter.toConfig(obj.getClass(), obj, (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
				return;
			} else {
				converter = this.getConverter(field.getType());

				if (converter != null) {
					root.set(path, converter.toConfig(field.getType(), obj, (field.getGenericType() instanceof ParameterizedType) ? (ParameterizedType) field.getGenericType() : null));
					return;
				}
			}
		}

		root.set(path, obj);
	}

}