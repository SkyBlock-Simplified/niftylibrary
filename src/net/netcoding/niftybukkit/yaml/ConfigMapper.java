package net.netcoding.niftybukkit.yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.netcoding.niftybukkit.minecraft.BukkitHelper;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

public class ConfigMapper extends BukkitHelper {

	private final transient Yaml yaml;
	private final transient HashMap<String, ArrayList<String>> comments = new HashMap<>();
	private final transient Representer yamlRepresenter = new Representer();
	protected transient File CONFIG_FILE;
	protected transient String[] CONFIG_HEADER;
	protected transient ConfigSection root;

	protected ConfigMapper(JavaPlugin plugin, String fileName, String... header) {
		super(plugin);
		CONFIG_FILE = new File(this.getPlugin().getDataFolder(), fileName + (fileName.endsWith(".yml") ? "" : ".yml"));
		CONFIG_HEADER = header;
		DumperOptions yamlOptions = new DumperOptions();
		yamlOptions.setIndent(2);
		yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
		yaml = new Yaml(new CustomClassLoaderConstructor(ConfigMapper.class.getClassLoader()), yamlRepresenter, yamlOptions);
	}

	public void addComment(String key, String value) {
		if (!comments.containsKey(key))
			comments.put(key, new ArrayList<String>());

		comments.get(key).add(value);
	}

	public void clearComments() {
		comments.clear();
	}

	private void convertMapsToSections(Map<?, ?> input, ConfigSection section) {
		if (input == null) return;

		for (Map.Entry<?, ?> entry : input.entrySet()) {
			String key = entry.getKey().toString();
			Object value = entry.getValue();

			if (value instanceof Map)
				convertMapsToSections((Map<?, ?>) value, section.create(key));
			else
				section.set(key, value);
		}
	}

	protected static boolean doSkip(Field field) {
		return Modifier.isTransient(field.getModifiers()) || Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers());
	}

	@SuppressWarnings("unchecked")
	public void loadFromMap(Map<?, Object> section) throws NoSuchFieldException, IllegalAccessException {
		for (Map.Entry<String, Object> entry : ((Map<String, Object>)section).entrySet()) {
			String path = entry.getKey().replace(".", "_");
			Field field = this.getClass().getDeclaredField(path);
			if (Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
			field.set(this, entry.getValue());
		}
	}

	protected void loadFromYaml() throws InvalidConfigurationException {
		root = new ConfigSection();

		try (FileReader fileReader = new FileReader(CONFIG_FILE)) {
			Object object = yaml.load(fileReader);

			if (object != null)
				convertMapsToSections((Map<?, ?>) object, root);
		} catch (IOException | ClassCastException | YAMLException e) {
			throw new InvalidConfigurationException("Could not load YML", e);
		}
	}

	public Map<String, Object> saveToMap() {
		Map<String, Object> returnMap = new HashMap<>();

		for (Field field : this.getClass().getDeclaredFields()) {
			String path = field.getName().replaceAll("_", ".");
			if (doSkip(field)) continue;
			if(Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);
			try { returnMap.put(path, field.get(this)); } catch (IllegalAccessException e) { }
		}

		return returnMap;
	}

	protected void saveToYaml() throws InvalidConfigurationException {
		try (FileWriter fileWriter = new FileWriter(CONFIG_FILE)) {
			if (CONFIG_HEADER != null && CONFIG_HEADER.length > 0) {
				for (String line : CONFIG_HEADER) fileWriter.write("# " + line + "\n");
				fileWriter.write("\n");
			}

			Integer depth = 0;
			ArrayList<String> keyChain = new ArrayList<>();
			String yamlString = yaml.dump(root.getValues(true));
			StringBuilder writeLines = new StringBuilder();

			for (String line : yamlString.split("\n")) {
				if (line.startsWith(new String(new char[depth]).replace("\0", " "))) {
					keyChain.add(line.split(":")[0].trim());
					depth += 2;
					writeLines.append("\n");
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

						depth = spaces;
						if (spaces == 0) {
							keyChain = new ArrayList<>();
							depth = 2;
						} else {
							ArrayList<String> temp = new ArrayList<>();
							int index = 0;

							for (int i = 0; i < spaces; i = i + 2, index++)
								temp.add(keyChain.get(index));

							keyChain = temp;
							depth += 2;
						}
					}

					keyChain.add(line.split(":")[0].trim());
				}

				String search = (keyChain.size() > 0 ? StringUtil.implode(".", keyChain) : "");
				if (comments.containsKey(search)) {
					for (String comment : comments.get(search)) {
						writeLines.append(new String(new char[depth - 2]).replace("\0", " "));
						writeLines.append("# ");
						writeLines.append(comment);
						writeLines.append("\n");
					}
				}

				writeLines.append(line);
				writeLines.append("\n");
			}

			fileWriter.write(writeLines.toString());
		} catch (IOException e) {
			throw new InvalidConfigurationException("Could not save YML", e);
		}
	}

}