package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;

@SuppressWarnings("unchecked")
public class Array extends Converter {

	private static <T> T[] getArray(Class<T> type, java.util.List<Object> list) {
		T[] array = (T[])java.lang.reflect.Array.newInstance(type, list.size());
		return (T[]) list.toArray(array);
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		java.util.List<Object> values = (java.util.List<Object>)obj;
		return getArray(type, values);
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		return obj;
	}

	@Override
	public boolean supports(Class<?> type) {
		return type.isArray();
	}

}