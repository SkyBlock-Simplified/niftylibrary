package net.netcoding.niftybukkit.yaml;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ConfigSection {

	private String key;
	private String fullPath;
	private CacheConfig cacheConfig;
	protected final Map<String, Object> map = new LinkedHashMap<>();

	public ConfigSection() {
		this.key = "";
		this.fullPath = "";

		cacheConfig = new CacheConfig();
	}

	public ConfigSection(ConfigSection root, String key) {
		this.key = key;
		this.fullPath = (!"".equals(root.fullPath)) ? String.format("%s.%s", root.fullPath, key) : key;
		this.cacheConfig = root.cacheConfig;
		cacheConfig.set(fullPath, this);
	}

	public ConfigSection create(String path) {
		if (path == null) throw new IllegalArgumentException("Cannot create section at empty path");
		int i1 = -1, i2;
		ConfigSection section = this;

		while ((i1 = path.indexOf('.', i2 = i1 + 1)) != -1) {
			String node = path.substring(i2, i1);
			ConfigSection subSection = section.getConfigSection(node);
			section = (section == null ? section.create(node) : subSection);
		}

		String key = path.substring(i2);
		if (section == this) {
			ConfigSection result = new ConfigSection(this, key);
			map.put(key, result);
			return result;
		}

		return section.create(key);
	}

	private ConfigSection getConfigSection(String node) {
		return (map.containsKey(node) && map.get(node) instanceof ConfigSection) ? (ConfigSection) map.get(node) : null;
	}

	public String getKey() {
		return this.key;
	}

	public void set(String path, Object value) {
		if (path == null) throw new IllegalArgumentException("Cannot set a value at empty path");
		int i1 = -1, i2;
		ConfigSection section = this;

		while ((i1 = path.indexOf('.', i2 = i1 + 1)) != -1) {
			String node = path.substring(i2, i1);
			ConfigSection subSection = section.getConfigSection(node);
			section = (section == null ? section.create(node) : subSection);
		}

		String key = path.substring(i2);
		if (section == this) {
			if (value == null)
				map.remove(key);
			else {
				map.put(key, value);
				cacheConfig.set(!"".equals(fullPath) ? String.format("%s.%s", fullPath, key) : key, value);
			}
		} else
			section.set(key, value);
	}

	protected void mapChildrenValues(Map<String, Object> output, ConfigSection section, boolean deep) {
		if (section != null) {
			for (Map.Entry<String, Object> entry : section.map.entrySet()) {
				if (entry.getValue() instanceof ConfigSection) {
					Map<String, Object> result = new LinkedHashMap<>();
					output.put(entry.getKey(), result);
					if (deep) mapChildrenValues(result, (ConfigSection) entry.getValue(), true);
				} else
					output.put(entry.getKey(), entry.getValue());
			}
		}/* else {
			Map<String, Object> values = section.getValues(deep);

			for (Map.Entry<String, Object> entry : values.entrySet())
				output.put(section.fullPath + "." + entry.getKey(), entry.getValue());
		}*/
	}

	public Map getMap(String path) {
		return ((ConfigSection) cacheConfig.get(path)).getRawMap();
	}

	public Map<String, Object> getValues() {
		return this.getValues(false);
	}

	public Map<String, Object> getValues(boolean deep) {
		Map<String, Object> result = new LinkedHashMap<>();
		mapChildrenValues(result, this, deep);
		return result;
	}

	public void niceOutput() {
		cacheConfig.niceOutput();
	}

	public boolean has(String path) {
		return cacheConfig.has(path);
	}

	public <T> T get(String path) {
		return cacheConfig.get(path);
	}

	public Map getRawMap() {
		return map;
	}

}