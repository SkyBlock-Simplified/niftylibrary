package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;

import net.netcoding.niftybukkit.yaml.InternalConverter;

@SuppressWarnings("unchecked")
public class Array extends Converter {

	public Array(InternalConverter internalConverter) {
		super(internalConverter);
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		return obj;
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		java.util.List<Object> values = (java.util.List<Object>)section;
		return getArray(type, values);
	}

	private static <T> T[] getArray(Class<T> type, java.util.List<Object> list) {
		T[] array = (T[]) java.lang.reflect.Array.newInstance(type, list.size());
		return (T[]) list.toArray(array);
	}

	@Override
	public boolean supports(Class<?> type) {
		return type.isArray();
	}

}