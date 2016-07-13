package net.netcoding.nifty.craftbukkit.yaml.converters;

import net.netcoding.nifty.core.util.NumberUtil;
import net.netcoding.nifty.core.yaml.InternalConverter;
import net.netcoding.nifty.core.yaml.converters.Converter;
import org.bukkit.Color;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class CraftPotionEffectConverter extends Converter {

	public CraftPotionEffectConverter(InternalConverter converter) {
		super(converter);
	}

	@Override
	public Object fromConfig(Class<?> type, Object section, ParameterizedType genericType) throws Exception {
		Map<String, Object> map = (Map<String, Object>)this.getConverter(Map.class).fromConfig(Map.class, section, null);
		String name = (String)map.get("name");
		int duration = NumberUtil.isNumber((String)map.get("duration")) ? (Integer)map.get("duration") : 1;
		int amplifier = NumberUtil.isNumber((String)map.get("amplifier")) ? (Integer)map.get("amplifier") : 0;
		boolean ambient = map.containsKey("ambient") ? (Boolean)map.get("ambient") : false;
		boolean particles = map.containsKey("particles") ? (Boolean)map.get("particles") : false;
		Color color = map.containsKey("color") ? Color.fromRGB(NumberUtil.toLong((String)map.get("color")).intValue()) : null;
		return new PotionEffect(PotionEffectType.getByName(name), duration, amplifier, ambient, particles, color);
	}

	@Override
	public Object toConfig(Class<?> type, Object obj, ParameterizedType genericType) throws Exception {
		PotionEffect potion = (PotionEffect)obj;
		Map<String, Object> saveMap = new HashMap<>();
		saveMap.put("name", potion.getType().getName());
		saveMap.put("duration", potion.getDuration());
		saveMap.put("amplifier", potion.getAmplifier());
		saveMap.put("ambient", potion.isAmbient());
		saveMap.put("particles", potion.hasParticles());
		saveMap.put("color", NumberUtil.toHexString(potion.getColor().asRGB()));
		return saveMap;
	}

	@Override
	public boolean supports(Class<?> type) {
		return org.bukkit.potion.PotionEffect.class.isAssignableFrom(type);
	}

}