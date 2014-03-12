package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

import net.netcoding.niftybukkit.yaml.ConfigSection;
import net.netcoding.niftybukkit.yaml.InternalConverter;

@SuppressWarnings("unchecked")
public class Config extends Converter {

	public Config(InternalConverter internalConverter) {
		super(internalConverter);
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) {
		return (obj instanceof Map) ? obj : ((net.netcoding.niftybukkit.yaml.Config)obj).saveToMap();
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		net.netcoding.niftybukkit.yaml.Config obj = (net.netcoding.niftybukkit.yaml.Config)type.cast(type.newInstance());
		obj.loadFromMap((section instanceof Map) ? (Map<?, Object>)section : ((ConfigSection)section).getRawMap());
		return obj;
	}

	@Override
	public boolean supports(Class<?> type) {
		return net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom(type);
	}

}