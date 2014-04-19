package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.netcoding.niftybukkit.yaml.InternalConverter;

public class Primitive extends Converter {

	private static final transient Set<String> types = new HashSet<>();

	static {
		types.addAll(Arrays.asList("boolean", "char", "byte", "short", "integer", "long", "float", "double", "string"));
	}

	public Primitive(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		switch(type.getSimpleName().toLowerCase()) {
		case "boolean":
			return (obj instanceof Boolean) ? obj : new Boolean((boolean)obj).booleanValue();
		case "integer":
			return (obj instanceof Integer) ? obj : new Integer((int)obj).intValue();
		case "long":
			return (obj instanceof Long) ? obj : new Long((int)obj).longValue();
		case "short":
			return (obj instanceof Short) ? obj : new Integer((int)obj).shortValue();
		case "byte":
			return (obj instanceof Byte) ? obj : new Integer((int)obj).byteValue();
		case "double":
			return (obj instanceof Double) ? obj : new Double((double)obj).doubleValue();
		case "float":
			return (obj instanceof Float) ? obj : new Double((double)obj).floatValue();
		case "char":
			return (obj instanceof Character) ? obj : ((String)obj).charAt(0);
		case "string":
			return (obj instanceof String) ? obj : String.valueOf(obj);
		default:
			return obj;
		}
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		return obj;
	}

	@Override
	public boolean supports(Class<?> type) {
		return types.contains(type.getSimpleName().toLowerCase());
	}

}