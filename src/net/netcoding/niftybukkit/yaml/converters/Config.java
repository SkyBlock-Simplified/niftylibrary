package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import net.netcoding.niftybukkit.yaml.ConfigSection;

@SuppressWarnings("unchecked")
public class Config extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) {
		return (obj instanceof Map) ? obj : ((net.netcoding.niftybukkit.yaml.Config)obj).saveToMap();
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		if (section instanceof Config) return section;
		net.netcoding.niftybukkit.yaml.Config obj = (net.netcoding.niftybukkit.yaml.Config)type.cast(type.newInstance());
		obj.loadFromMap((Map<?, Object>)((section instanceof Map) ? section : ((ConfigSection)section).getRawMap()));
		return obj;
	}

	@Override
	public boolean supports(Class<?> type) {
		return net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom(type);
	}

}