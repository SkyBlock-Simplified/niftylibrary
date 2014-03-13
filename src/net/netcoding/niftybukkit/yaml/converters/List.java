package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import net.netcoding.niftybukkit.yaml.InternalConverter;

@SuppressWarnings("unchecked")
public class List extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		java.util.List<Object> values = (java.util.List<Object>)obj;
		java.util.List<Object> newList = new ArrayList<>();

		try {
			newList = ((java.util.List<Object>)type.newInstance());
		} catch (Exception e) { }

		if (genericType.getActualTypeArguments()[0] instanceof Class && net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom((Class<?>)genericType.getActualTypeArguments()[0])) {
			Converter converter = InternalConverter.getConverter(net.netcoding.niftybukkit.yaml.Config.class);

			for (int i = 0; i < values.size(); i++)
				newList.add(converter.toConfig((Class<?>)genericType.getActualTypeArguments()[0], values.get(i), null));
		} else
			newList = values;

		return newList;
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		java.util.List<Object> values = (java.util.List<Object>)section;
		java.util.List<Object> newList = new ArrayList<>();

		try {
			newList = (java.util.List<Object>)type.newInstance();
		} catch (Exception e) { }

		if (genericType.getActualTypeArguments()[0] instanceof Class && net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom((Class<?>)genericType.getActualTypeArguments()[0])) {
			Converter converter = InternalConverter.getConverter(net.netcoding.niftybukkit.yaml.Config.class);

			for (int i = 0; i < values.size(); i++)
				newList.add(converter.fromConfig((Class<?>)genericType.getActualTypeArguments()[0], values.get(i), null));
		} else
			newList = values;

		return newList;
	}

	@Override
	public boolean supports(Class<?> type) {
		return java.util.List.class.isAssignableFrom(type);
	}

}