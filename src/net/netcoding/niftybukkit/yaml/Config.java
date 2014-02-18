package net.netcoding.niftybukkit.yaml;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.plugin.java.JavaPlugin;

import net.netcoding.niftybukkit.yaml.annotations.Comment;
import net.netcoding.niftybukkit.yaml.annotations.Comments;

public class Config extends YamlConfig {

	public Config(JavaPlugin plugin, String name, String... header) {
		super(plugin, name, header);
	}

	public void save() throws InvalidConfigurationException {
		if (CONFIG_FILE == null) throw new IllegalArgumentException("Saving a config without given file");
		clearComments();

		for (Field field : getClass().getDeclaredFields()) {
			String path = field.getName().replaceAll("_", ".");
			if (doSkip(field)) continue;

			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof Comment)
					addComment(path, ((Comment)annotation).value());

				if (annotation instanceof Comments) {
					Comments comments = (Comments)annotation;

					for (String comment : comments.value())
						addComment(path, comment);
				}
			}

			if (Modifier.isPrivate(field.getModifiers()))
				field.setAccessible(true);

			try {
				Converter.toConfig(this, field, root, path);
			} catch (Exception e) {
				throw new InvalidConfigurationException("Could not save the field", e);
			}
		}

		saveToYaml();
	}

	public void save(File file) throws InvalidConfigurationException {
		if (file == null) throw new IllegalArgumentException("File argument can not be null");

		CONFIG_FILE = file;
		save();
	}

	public void init() throws InvalidConfigurationException {
		if (!CONFIG_FILE.exists()) {
			if (CONFIG_FILE.getParentFile() != null)
				CONFIG_FILE.getParentFile().mkdirs();

			try {
				CONFIG_FILE.createNewFile();
				save();
			} catch (IOException e) {
				throw new InvalidConfigurationException("Could not create new empty config", e);
			}
		} else {
			load();
		}
	}

	public void init(File file) throws InvalidConfigurationException {
		if (file == null) throw new IllegalArgumentException("File argument can not be null");

		CONFIG_FILE = file;
		init();
	}

	public void reload() throws InvalidConfigurationException {
		reloadFromYaml();
	}

	public void load() throws InvalidConfigurationException {
		if (CONFIG_FILE == null) throw new IllegalArgumentException("Loading a config without given File");
		loadFromYaml();
		boolean save = false;

		for (Field field : getClass().getDeclaredFields()) {
			String path = field.getName().replaceAll("_", ".");
			if (doSkip(field)) continue;
			if(Modifier.isPrivate(field.getModifiers())) field.setAccessible(true);

			if (root.has(path)) {
				try {
					Converter.fromConfig(this, field, root, path);
				} catch (Exception e) {
					throw new InvalidConfigurationException("Could not set field", e);
				}
			} else {
				try {
					Converter.toConfig(this, field, root, path);
					save = true;
				} catch (Exception e) {
					throw new InvalidConfigurationException("Could not get field", e);
				}
			}
		}

		if (save) save();
	}

	public void load(File file) throws InvalidConfigurationException {
		if (file == null) throw new IllegalArgumentException("File argument can not be null");

		CONFIG_FILE = file;
		load();
	}

}