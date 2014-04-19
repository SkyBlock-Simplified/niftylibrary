package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import net.netcoding.niftybukkit.yaml.ConfigSection;
import net.netcoding.niftybukkit.yaml.InternalConverter;

public class Config extends Converter {

	public Config(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		net.netcoding.niftybukkit.yaml.Config obj1 = (net.netcoding.niftybukkit.yaml.Config) type.newInstance();
		obj1.loadFromMap((obj instanceof Map) ? (Map<?, ?>)obj : ((ConfigSection) obj).getRawMap());
		return obj;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		return (obj instanceof Map) ? obj : ((net.netcoding.niftybukkit.yaml.Config)obj).saveToMap();
	}

	@Override
	public boolean supports(Class<?> type) {
		return net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom(type);
	}

}