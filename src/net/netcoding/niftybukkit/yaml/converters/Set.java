package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import net.netcoding.niftybukkit.yaml.InternalConverter;

@SuppressWarnings("unchecked")
public class Set extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		java.util.Set<Object> values = (java.util.Set<Object>)obj;
		java.util.List<Object> newList = new ArrayList<Object>();
		Iterator<Object> iterator = values.iterator();

		while (iterator.hasNext()) {
			Object value = iterator.next();
			Converter converter = InternalConverter.getConverter(value.getClass());
			newList.add(converter != null ? converter.toConfig(value.getClass(), value, null) : value);
		}

		return newList;
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		java.util.List<Object> values = (java.util.List<Object>)obj;
		java.util.Set<Object> newList = new HashSet<Object>();

		try {
			newList = (java.util.Set<Object>)type.newInstance();
		} catch (Exception e) { }

		for (Object value : values) {
			Converter converter = InternalConverter.getConverter(value.getClass());
			newList.add(converter != null ? converter.toConfig(value.getClass(), value, null) : value);
		}
		return newList;
	}

	@Override
	public boolean supports(Class<?> type) {
		return java.util.Set.class.isAssignableFrom(type);
	}

}