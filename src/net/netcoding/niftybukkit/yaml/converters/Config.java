package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import net.netcoding.niftybukkit.yaml.ConfigSection;

public class Config extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		return (obj instanceof Map) ? obj : ((net.netcoding.niftybukkit.yaml.Config)obj).saveToMap();
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		net.netcoding.niftybukkit.yaml.Config obj1 = (net.netcoding.niftybukkit.yaml.Config) type.newInstance();
		obj1.loadFromMap((obj instanceof Map) ? (Map<?, ?>)obj : ((ConfigSection) obj).getRawMap());
		return obj;
	}

	@Override
	public boolean supports(Class<?> type) {
		return net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom(type);
	}

}