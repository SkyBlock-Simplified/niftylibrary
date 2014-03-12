package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;

import net.netcoding.niftybukkit.yaml.InternalConverter;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class Array implements Converter {

	private InternalConverter internalConverter;

	public Array(InternalConverter internalConverter) {
		this.internalConverter = internalConverter;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		return obj;
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		java.util.List values = (java.util.List) section;
		return getArray(type, values);
	}

	private static <T> T[] getArray(Class<T> type, java.util.List list) {
		T[] array = (T[]) java.lang.reflect.Array.newInstance(type, list.size());
		return (T[]) list.toArray(array);
	}

	@Override
	public boolean supports(Class<?> type) {
		return type.isArray();
	}

}