package net.netcoding.niftybukkit.yaml.converters;

import net.netcoding.niftycore.util.NumberUtil;
import net.netcoding.niftycore.yaml.InternalConverter;
import net.netcoding.niftycore.yaml.converters.Converter;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class PotionEffect extends Converter {

	public PotionEffect(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> map = (Map<String, Object>)this.getConverter(Map.class).fromConfig(HashMap.class, section, null);
		String name = (String)map.get("name");
		int duration = NumberUtil.isInt((String)map.get("duration")) ? (Integer)map.get("duration") : 1;
		int amplifier = NumberUtil.isInt((String)map.get("amplifier")) ? (Integer)map.get("amplifier") : 0;
		return new org.bukkit.potion.PotionEffect(PotionEffectType.getByName(name), duration, amplifier);
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		org.bukkit.potion.PotionEffect potion = (org.bukkit.potion.PotionEffect)obj;
		Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("name", potion.getType().getName());
		saveMap.put("duration", potion.getDuration());
		saveMap.put("amplifier", potion.getAmplifier());
		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return PotionEffect.class.isAssignableFrom(type);
	}

}