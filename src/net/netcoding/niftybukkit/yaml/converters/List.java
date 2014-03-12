package net.netcoding.niftybukkit.yaml.converters;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import net.netcoding.niftybukkit.yaml.InternalConverter;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class List implements Converter {

	private InternalConverter internalConverter;

	public List(InternalConverter internalConverter) {
		this.internalConverter = internalConverter;
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		java.util.List values = (java.util.List)obj;
		java.util.List newList = new ArrayList();

		try {
			newList = ((java.util.List) type.newInstance());
		} catch (Exception e) { }


		if (genericType.getActualTypeArguments()[0] instanceof Class && net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom((Class)genericType.getActualTypeArguments()[0])) {
			Converter converter = internalConverter.getConverter(net.netcoding.niftybukkit.yaml.Config.class);

			for(int i = 0; i < values.size(); i++)
				newList.add(converter.toConfig((Class)genericType.getActualTypeArguments()[0], values.get(i), null));
		} else
			newList = values;

		return newList;
	}

	@Override
	public Object fromConfig(Class type, Object section, ParameterizedType genericType) throws Exception {
		java.util.List newList = new ArrayList();
		try {
			newList = (java.util.List)type.newInstance();
		} catch (Exception e) { }

		java.util.List values = (java.util.List)section;

		if (genericType.getActualTypeArguments()[0] instanceof Class && net.netcoding.niftybukkit.yaml.Config.class.isAssignableFrom((Class) genericType.getActualTypeArguments()[0])) {
			Converter converter = internalConverter.getConverter(net.netcoding.niftybukkit.yaml.Config.class);

			for(int i = 0; i < values.size(); i++)
				newList.add(converter.fromConfig((Class) genericType.getActualTypeArguments()[0], values.get(i), null));
		} else
			newList = values;

		return newList;
	}

	@Override
	public boolean supports(Class<?> type) {
		return java.util.List.class.isAssignableFrom(type);
	}

}