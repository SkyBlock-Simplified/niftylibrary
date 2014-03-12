package net.netcoding.niftybukkit.yaml;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

import net.netcoding.niftybukkit.yaml.annotations.Comment;
import net.netcoding.niftybukkit.yaml.annotations.Comments;
import net.netcoding.niftybukkit.yaml.annotations.Path;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

import org.bukkit.plugin.java.JavaPlugin;

public class Config extends ConfigMapper {

	public Config(JavaPlugin plugin, String fileName, String... header) {
		super(plugin, fileName, header);
		if (CONFIG_FILE == null) throw new IllegalArgumentException("Filename cannot be null!");
	}

	public void init() throws InvalidConfigurationException {
		if (!CONFIG_FILE.exists()) {
			if (CONFIG_FILE.getParentFile() != null)
				CONFIG_FILE.getParentFile().mkdirs();

			try {
				CONFIG_FILE.createNewFile();
				this.save();
			} catch (IOException ex) {
				throw new InvalidConfigurationException("Could not create new empty config!", ex);
			}
		} else
			this.load();
	}

	public void init(File file) throws InvalidConfigurationException {
		if (file == null) throw new IllegalArgumentException("File cannot be null!");
		CONFIG_FILE = file;
		init();
	}

	private void internalLoad(Class<?> clazz) throws InvalidConfigurationException {
		if (!clazz.getSuperclass().equals(Config.class)) internalLoad(clazz.getSuperclass());
		boolean save = false;

		for (Field field : clazz.getDeclaredFields()) {
			String path = field.getName().replaceAll("_", ".");

			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof Path) {
					path = ((Path)annotation).value();
					break;
				}
			}

			if (doSkip(field))
				continue;

			if (Modifier.isPrivate(field.getModifiers()))
				field.setAccessible(true);

			if (root.has(path)) {
				try {
					this.fromConfig(this, field, root, path);
				} catch (Exception ex) {
					throw new InvalidConfigurationException(String.format("Could not set field %s!", field.getName()), ex);
				}
			} else {
				try {
					this.toConfig(this, field, root, path);
					save = true;
				} catch (Exception ex) {
					throw new InvalidConfigurationException(String.format("Could not get field %s!", field.getName()), ex);
				}
			}
		}

		if (save) save();
	}

	private void internalSave(Class<?> clazz) throws InvalidConfigurationException {
		if (!clazz.getSuperclass().equals(Config.class)) internalSave(clazz.getSuperclass());

		for (Field field : clazz.getDeclaredFields()) {
			String path = field.getName().replaceAll("_", ".");
			if (doSkip(field)) continue;
			ArrayList<String> comments = new ArrayList<>();

			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof Comment)
					comments.add(((Comment)annotation).value());

				if (annotation instanceof Comments)
					comments.addAll(Arrays.asList(((Comments)annotation).value()));

				if (annotation instanceof Path)
					path = ((Path)annotation).value();
			}

			if (comments.size() > 0) {
				for (String comment : comments)
					addComment(path, comment);
			}

			if (Modifier.isPrivate(field.getModifiers()))
				field.setAccessible(true);

			try {
				this.toConfig(this, field, root, path);
			} catch (Exception ex) {
				throw new InvalidConfigurationException(String.format("Could not save field %s!", field.getName()), ex);
			}
		}
	}

	public void load() throws InvalidConfigurationException {
		if (CONFIG_FILE == null) throw new IllegalArgumentException("Cannot load config without file!");
		this.loadFromYaml();
		this.update(root);
		this.internalLoad(this.getClass());
	}

	public void load(File file) throws InvalidConfigurationException {
		if (file == null) throw new IllegalArgumentException("File cannot be null!");
		CONFIG_FILE = file;
		this.load();
	}

	public void reload() throws InvalidConfigurationException {
		this.loadFromYaml();
		this.internalLoad(this.getClass());
	}

	public void save() throws InvalidConfigurationException {
		if (CONFIG_FILE == null) throw new IllegalArgumentException("Saving a config without given File");
		if (root == null) root = new ConfigSection();
		this.clearComments();
		this.internalSave(this.getClass());
		this.saveToYaml();
	}

	public void save(File file) throws InvalidConfigurationException {
		if (file == null) throw new IllegalArgumentException("File argument can not be null");
		CONFIG_FILE = file;
		this.save();
	}

	/**
	 * This function gets called after the File has been loaded and before the Converter gets it.
	 * This is used to manually edit the configSection when you updated the config or something
	 * @param configSection The root ConfigSection with all Subnodes loaded into
	 */
	public void update(ConfigSection configSection) { }

}