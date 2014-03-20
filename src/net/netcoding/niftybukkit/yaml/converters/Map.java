package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;

import net.netcoding.niftybukkit.yaml.ConfigSection;
import net.netcoding.niftybukkit.yaml.InternalConverter;

@SuppressWarnings("unchecked")
public class Map extends Converter {

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		java.util.Map<Object, Object> map = (java.util.Map<Object, Object>)obj;

		for (java.util.Map.Entry<Object, Object> entry : map.entrySet()) {
			if (entry.getValue() == null) continue;
			Class<?> clazz = entry.getValue().getClass();
			Converter converter = InternalConverter.getConverter(clazz);
			map.put(entry.getKey(), (converter != null ? converter.toConfig(clazz, entry.getValue(), null) : entry.getValue()));
		}

		return map;
	}

	@Override
	public Object fromConfig(Class<?> type, Object obj, ParameterizedType parameterizedType) throws Exception {
		if (parameterizedType != null) {
			java.util.Map<Object, Object> map;

			try {
				map = ((java.util.Map<Object, Object>)((Class<?>)parameterizedType.getRawType()).newInstance());
			} catch (InstantiationException e) {
				map = new HashMap<Object, Object>();
			}

			if (parameterizedType.getActualTypeArguments().length == 2) {
				Class<?> keyClass = ((Class<?>)parameterizedType.getActualTypeArguments()[0]);

				java.util.Map<?, ?> map1 = (obj instanceof java.util.Map) ? (java.util.Map<Object, Object>)obj : ((ConfigSection)obj).getRawMap();
				for (java.util.Map.Entry<?, ?> entry : map1.entrySet()) {
					Object key;

					if (keyClass.equals(Integer.class) && !(entry.getKey() instanceof Integer))
						key = Integer.valueOf((String)entry.getKey());
					else if (keyClass.equals(Short.class) && !(entry.getKey() instanceof Short))
						key = Short.valueOf((String)entry.getKey());
					else if (keyClass.equals(Byte.class) && !(entry.getKey() instanceof Byte))
						key = Byte.valueOf((String)entry.getKey());
					else if (keyClass.equals(Float.class) && !(entry.getKey() instanceof Float))
						key = Float.valueOf((String)entry.getKey());
					else if (keyClass.equals(Double.class) && !(entry.getKey() instanceof Double))
						key = Double.valueOf((String)entry.getKey());
					else
						key = entry.getKey();

					Class<?> clazz;
					if (parameterizedType.getActualTypeArguments()[1] instanceof ParameterizedType) {
						ParameterizedType parameterizedType1 = (ParameterizedType) parameterizedType.getActualTypeArguments()[1];
						clazz = (Class<?>)parameterizedType1.getRawType();
					} else
						clazz = (Class<?>)parameterizedType.getActualTypeArguments()[1];

					Converter converter = InternalConverter.getConverter(clazz);
					map.put(key, ( converter != null ) ? converter.fromConfig(clazz, entry.getValue(), (parameterizedType.getActualTypeArguments()[1] instanceof ParameterizedType) ? (ParameterizedType)parameterizedType.getActualTypeArguments()[1] : null) : entry.getValue());
				}
			} else {
				Converter converter = InternalConverter.getConverter((Class<?>)parameterizedType.getRawType());
				if (converter != null) return converter.fromConfig((Class<?>)parameterizedType.getRawType(), obj, null);
				return (obj instanceof java.util.Map) ? (java.util.Map<Object, Object>) obj : ((ConfigSection) obj).getRawMap();
			}

			return map;
		} else
			return obj;
	}

	@Override
	public boolean supports(Class<?> type) {
		return java.util.Map.class.isAssignableFrom(type);
	}
}
