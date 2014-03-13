package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Primitive extends Converter {

	private static final transient Set<String> types = new HashSet<>();

	static {
		types.addAll(Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double"));
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		return obj;
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		switch(type.getSimpleName()) {
		case "boolean":
			return (section instanceof Boolean) ? section : new Boolean((boolean)section).booleanValue();
		case "int":
			return (section instanceof Integer) ? section : new Integer((int)section).intValue();
		case "long":
			return (section instanceof Long) ? section : new Long((int)section).longValue();
		case "short":
			return (section instanceof Short) ? section : new Integer((int)section).shortValue();
		case "byte":
			return (section instanceof Byte) ? section : new Integer((int)section).byteValue();
		case "double":
			return (section instanceof Double) ? section : new Double((double)section).doubleValue();
		case "float":
			return (section instanceof Float) ? section : new Double((double)section).floatValue();
		case "char":
			return (section instanceof Character) ? section : ((String)section).charAt(0);
		default:
			return section;
		}
	}

	@Override
	public boolean supports(Class<?> type) {
		return types.contains(type.getName());
	}

}