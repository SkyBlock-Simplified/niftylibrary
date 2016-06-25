package net.netcoding.nifty.common.yaml.converters;

import net.netcoding.nifty.core.yaml.InternalConverter;
import net.netcoding.nifty.core.yaml.converters.Converter;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class VectorConverter extends Converter {

	public VectorConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> map = (Map<String, Object>)this.getConverter(Map.class).fromConfig(HashMap.class, section, null);
		return new net.netcoding.nifty.core.util.misc.Vector((Double)map.get("x"), (Double)map.get("y"), (Double)map.get("z"));
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		return ((net.netcoding.nifty.core.util.misc.Vector)obj).serialize();
	}

	@Override
	public boolean supports(Class<?> type) {
		return net.netcoding.nifty.core.util.misc.Vector.class.isAssignableFrom(type);
	}

}