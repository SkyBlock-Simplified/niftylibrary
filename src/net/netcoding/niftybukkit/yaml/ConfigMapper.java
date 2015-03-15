package net.netcoding.niftybukkit.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.yaml.annotations.Path;
import net.netcoding.niftybukkit.yaml.converters.Converter;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Represent;
import org.yaml.snakeyaml.representer.Representer;

public class ConfigMapper extends BukkitHelper {

	private final transient Yaml yaml;
	private final Map<String, ArrayList<String>> comments = new LinkedHashMap<>();
	private final transient NullRepresenter representer = new NullRepresenter();
	protected final transient InternalConverter converter = new InternalConverter();
	protected File configFile;
	protected String[] header;
	protected transient ConfigSection root;

	protected ConfigMapper(JavaPlugin plugin) {
		this(plugin, null);
	}

	protected ConfigMapper(JavaPlugin plugin, String fileName, String... header) {
		super(plugin);
		if (fileName != null) this.configFile = new File(this.getPlugin().getDataFolder(), fileName + (fileName.endsWith(".yml") ? "" : ".yml"));
		this.header = header;
		DumperOptions options = new DumperOptions();
		options.setIndent(2);
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		this.representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		this.yaml = new Yaml(new CustomClassLoaderConstructor(ConfigMapper.class.getClassLoader()), this.representer, options);
	}

	public void addComment(String key, String value) {
		if (!this.comments.containsKey(key))
			this.comments.put(key, new ArrayList<String>());

		this.comments.get(key).add(value);
	}

	public void addCustomConverter(Class<? extends Converter> converter) {
		this.converter.addCustomConverter(converter);
	}

	public void clearComments() {
		this.comments.clear();
	}

	public static ConfigSection convertFromMap(Map<?, ?> config) {
		ConfigSection section = new ConfigSection();
		section.map.putAll(config);
		return section;
	}

	private void convertMapsToSections(Map<?, ?> input, ConfigSection section) {
		if (input == null) return;

		for (Map.Entry<?, ?> entry : input.entrySet()) {
			String key = entry.getKey().toString();
			Object value = entry.getValue();

			if (value instanceof Map)
				convertMapsToSections((Map<?, ?>)value, section.create(key));
			else
				section.set(key, value);
		}
	}

	protected static boolean doSkip(Field field) {
		return Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers());
	}

	public void loadFromMap(Map<?, ?> section, Class<?> clazz) throws Exception {
		if (!clazz.getSuperclass().equals(Config.class))
			loadFromMap(section, clazz.getSuperclass());

		for (Field field : this.getClass().getDeclaredFields()) {
			if (doSkip(field)) continue;
			String path = field.getName().replace(".", "_");

			if (field.isAnnotationPresent(Path.class))
				path = field.getAnnotation(Path.class).value();

			if(Modifier.isPrivate(field.getModifiers()))
				field.setAccessible(true);

			this.converter.fromConfig((Config)this, field, convertFromMap(section), path);
		}
	}

	protected void loadFromYaml() throws InvalidConfigurationException {
		this.root = new ConfigSection();

		try (InputStreamReader fileReader = new InputStreamReader(new FileInputStream(this.configFile), Charset.forName("UTF-8"))) {
			Object object = this.yaml.load(fileReader);
			if (object != null) convertMapsToSections((Map<?, ?>)object, this.root);
		} catch (IOException | ClassCastException | YAMLException ex) {
			throw new InvalidConfigurationException("Could not load YML", ex);
		}
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> saveToMap(Class<?> clazz) throws Exception {
		Map<String, Object> returnMap = new HashMap<>();

		if (!clazz.getSuperclass().equals(Config.class) && !clazz.getSuperclass().equals(Object.class)) {
			Map<String, Object> map = saveToMap(clazz.getSuperclass());

			for (Map.Entry<String, Object> entry : map.entrySet())
				returnMap.put(entry.getKey(), entry.getValue());
		}

		for (Field field : this.getClass().getDeclaredFields()) {
			if (doSkip(field)) continue;
			String path = field.getName().replaceAll("_", ".");

			if (field.isAnnotationPresent(Path.class))
				path = field.getAnnotation(Path.class).value();

			if (Modifier.isPrivate(field.getModifiers()))
				field.setAccessible(true);

			try {
				returnMap.put(path, field.get(this));
			} catch (IllegalAccessException e) { }
		}

		Converter converter = this.converter.getConverter(Map.class);
		return (Map<String, Object>)converter.toConfig(HashMap.class, returnMap, null);
	}

	protected void saveToYaml() throws InvalidConfigurationException {
		try (OutputStreamWriter fileWriter = new OutputStreamWriter(new FileOutputStream(this.configFile), Charset.forName("UTF-8"))) {
			if (ListUtil.notEmpty(this.header)) {
				for (String line : this.header) fileWriter.write("# " + line + "\n");
				fileWriter.write("\n");
			}

			Integer depth = 0;
			ArrayList<String> keyChain = new ArrayList<>();
			String yamlString = this.yaml.dump(root.getValues(true));
			StringBuilder writeLines = new StringBuilder();
			String[] yamlSplit = yamlString.split("\n");

			for (int y = 0; y < yamlSplit.length; y++) {
				String line = yamlSplit[y];

				if (line.startsWith(new String(new char[depth]).replace("\0", " "))) {
					keyChain.add(line.split(":")[0].trim());
					depth += 2;
				} else {
					if (line.startsWith(new String(new char[depth - 2]).replace("\0", " ")))
						keyChain.remove(keyChain.size() - 1);
					else {
						int spaces = 0;
						for (int i = 0; i < line.length(); i++) {
							if (line.charAt(i) == ' ')
								spaces++;
							else
								break;
						}

						if (spaces == 0) writeLines.append("\n");
						depth = spaces;
						if (spaces == 0) {
							keyChain = new ArrayList<>();
							depth = 2;
						} else {
							ArrayList<String> temp = new ArrayList<>();
							int index = 0;

							for (int i = 0; i < spaces; i += 2, index++)
								temp.add(keyChain.get(index));

							keyChain = temp;
							depth += 2;
						}
					}

					keyChain.add(line.split(":")[0].trim());
				}

				String search = (keyChain.size() > 0 ? StringUtil.implode(".", keyChain) : "");
				if (this.comments.containsKey(search)) {
					for (String comment : comments.get(search)) {
						writeLines.append(new String(new char[depth - 2]).replace("\0", " "));
						writeLines.append("# ");
						writeLines.append(comment);
						writeLines.append("\n");
					}
				}

				writeLines.append(line);
				if (y < yamlSplit.length - 1) writeLines.append("\n");
			}

			fileWriter.write(writeLines.toString());
		} catch (IOException e) {
			throw new InvalidConfigurationException("Could not save YML", e);
		}
	}

	private class NullRepresenter extends Representer {

		public NullRepresenter() {
			this.nullRepresenter = new RepresentNull();
		}

		private class RepresentNull implements Represent {

			public Node representData(Object data) {
				return representScalar(Tag.NULL, "");
			}

		}

	}

}