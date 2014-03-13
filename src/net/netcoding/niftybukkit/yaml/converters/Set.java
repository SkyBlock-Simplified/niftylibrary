package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.HashSet;

import net.netcoding.niftybukkit.yaml.InternalConverter;

@SuppressWarnings("unchecked")
public class Set extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		java.util.Set<Object> values = (java.util.Set<Object>)obj;
		java.util.Set<Object> newList = new HashSet<Object>();

		try {
			newList = ((java.util.Set<Object>)type.newInstance());
		} catch (Exception e) { }

		if (genericType.getActualTypeArguments()[0] instanceof Class && net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom((Class<?>)genericType.getActualTypeArguments()[0])) {
			Converter converter = InternalConverter.getConverter(net.netcoding.niftybukkit.yaml.Config.class);

			for (Object valObj : values)
				newList.add(converter.toConfig((Class<?>)genericType.getActualTypeArguments()[0], valObj, null));
		} else
			newList = values;

		return newList;
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		java.util.Set<Object> values = (java.util.Set<Object>)section;
		java.util.Set<Object> newList = new HashSet<Object>();

		try {
			newList = (java.util.Set<Object>)type.newInstance();
		} catch (Exception e) { }

		if (genericType.getActualTypeArguments()[0] instanceof Class && net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom((Class<?>)genericType.getActualTypeArguments()[0])) {
			Converter converter = InternalConverter.getConverter(net.netcoding.niftybukkit.yaml.Config.class);

			for (Object valObj : values)
				newList.add(converter.fromConfig((Class<?>)genericType.getActualTypeArguments()[0], valObj, null));
		} else
			newList = values;

		return newList;
	}

	@Override
	public boolean supports(Class<?> type) {
		return java.util.Set.class.isAssignableFrom(type);
	}

}