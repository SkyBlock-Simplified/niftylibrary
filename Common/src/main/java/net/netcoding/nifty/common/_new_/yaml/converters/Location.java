package net.netcoding.nifty.common._new_.yaml.converters;

import net.netcoding.niftycore.yaml.InternalConverter;
import net.netcoding.niftycore.yaml.converters.Converter;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class Location extends Converter {

	public Location(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> map = (Map<String, Object>)this.getConverter(Map.class).fromConfig(HashMap.class, section, null);
		return net.netcoding.nifty.common._new_.minecraft.region.Location.deserialize(map);
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		return ((net.netcoding.nifty.common._new_.minecraft.region.Location)obj).serialize();
	}

	@Override
	public boolean supports(Class<?> type) {
		return net.netcoding.nifty.common._new_.minecraft.region.Location.class.isAssignableFrom(type);
	}

}