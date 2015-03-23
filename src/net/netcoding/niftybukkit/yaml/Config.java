package net.netcoding.niftybukkit.yaml;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.FileSystems;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;

import net.netcoding.niftybukkit.yaml.annotations.Comment;
import net.netcoding.niftybukkit.yaml.annotations.Comments;
import net.netcoding.niftybukkit.yaml.annotations.Path;
import net.netcoding.niftybukkit.yaml.exceptions.InvalidConfigurationException;

import org.bukkit.plugin.java.JavaPlugin;

public class Config extends ConfigMapper implements Runnable {

	private boolean skipFailedConversion = false;
	private int taskId = -1;
	private WatchService watchService;
	private WatchKey watchKey;
	private boolean reloadProcessing = false;

	public Config(JavaPlugin plugin) {
		this(plugin, null);
	}

	public Config(JavaPlugin plugin, String fileName, String... header) {
		this(plugin, fileName, false, header);
	}

	public Config(JavaPlugin plugin, String fileName, boolean skipFailedConversion, String... header) {
		super(plugin, fileName, header);
		if (this.configFile == null) throw new IllegalArgumentException("Filename cannot be null!");
		this.setSuppressFailedConversions(skipFailedConversion);
	}

	public boolean delete() {
		return this.configFile.delete();
	}

	public boolean exists() {
		return this.configFile.exists();
	}

	public void init() throws InvalidConfigurationException {
		if (!this.exists()) {
			if (this.configFile.getParentFile() != null)
				this.configFile.getParentFile().mkdirs();

			try {
				this.configFile.createNewFile();
				this.save();
			} catch (IOException ex) {
				throw new InvalidConfigurationException("Could not create new empty config!", ex);
			}
		} else
			this.load();
	}

	public void init(File file) throws InvalidConfigurationException {
		if (file == null) throw new IllegalArgumentException("File cannot be null!");
		this.configFile = file;
		this.init();
	}

	private void internalLoad(Class<?> clazz, boolean dontSave) throws InvalidConfigurationException {
		if (!clazz.getSuperclass().equals(Config.class)) this.internalLoad(clazz.getSuperclass(), dontSave);
		boolean save = false;

		for (Field field : clazz.getDeclaredFields()) {
			if (doSkip(field)) continue;
			String path = field.getName().replaceAll("_", ".");

			if (field.isAnnotationPresent(Path.class))
				path = field.getAnnotation(Path.class).value();

			if (Modifier.isPrivate(field.getModifiers()))
				field.setAccessible(true);

			if (root.has(path)) {
				try {
					this.converter.fromConfig(this, field, root, path);
				} catch (Exception ex) {
					if (!this.isSuppressingFailures())
						throw new InvalidConfigurationException(String.format("Could not set field %s!", field.getName()), ex);
				}
			} else {
				try {
					this.converter.toConfig(this, field, root, path);
					this.converter.fromConfig(this,  field, root, path);
					save = true;
				} catch (Exception ex) {
					if (!this.isSuppressingFailures())
						throw new InvalidConfigurationException(String.format("Could not get field %s!", field.getName()), ex);
				}
			}
		}

		if (save && !dontSave) this.saveToYaml();
	}

	private void internalSave(Class<?> clazz) throws InvalidConfigurationException {
		if (!clazz.getSuperclass().equals(Config.class)) internalSave(clazz.getSuperclass());

		for (Field field : clazz.getDeclaredFields()) {
			if (doSkip(field)) continue;
			String path = field.getName().replaceAll("_", ".");
			ArrayList<String> comments = new ArrayList<>();

			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof Comment)
					comments.add(((Comment)annotation).value());

				if (annotation instanceof Comments)
					comments.addAll(Arrays.asList(((Comments)annotation).value()));
			}

			if (field.isAnnotationPresent(Path.class))
				path = field.getAnnotation(Path.class).value();

			if (comments.size() > 0) {
				for (String comment : comments)
					addComment(path, comment);
			}

			if (Modifier.isPrivate(field.getModifiers()))
				field.setAccessible(true);

			try {
				this.converter.toConfig(this, field, root, path);
				this.converter.fromConfig(this, field, root, path);
			} catch (Exception ex) {
				if (!this.isSuppressingFailures())
					throw new InvalidConfigurationException(String.format("Could not save field %s!", field.getName()), ex);
			}
		}
	}

	public boolean isSuppressingFailures() {
		return this.skipFailedConversion;
	}

	public void load() throws InvalidConfigurationException {
		if (this.configFile == null) throw new IllegalArgumentException("Cannot load config without file!");
		this.loadFromYaml();
		this.update(this.root);
		this.internalLoad(this.getClass(), true);
	}

	public void load(File file) throws InvalidConfigurationException {
		if (file == null) throw new IllegalArgumentException("File cannot be null!");
		this.configFile = file;
		this.load();
	}

	public void reload() throws InvalidConfigurationException {
		this.reload(true);
	}

	private void reload(boolean dontSave) throws InvalidConfigurationException {
		this.loadFromYaml();
		this.internalLoad(this.getClass(), dontSave);
	}

	@Override
	public void run() {
		WatchKey key = this.watchService.poll();
		if (key == null) return;

		for (WatchEvent<?> event : key.pollEvents()) {
			if (StandardWatchEventKinds.OVERFLOW.equals(event.kind()))
				continue;
			else if (StandardWatchEventKinds.ENTRY_DELETE.equals(event.kind())) {
				this.stopWatcher();
				break;
			} else if (StandardWatchEventKinds.ENTRY_MODIFY.equals(event.kind())) {
				java.nio.file.Path context = (java.nio.file.Path)event.context();
				String path = ((java.nio.file.Path)this.watchKey.watchable()).resolve(context).toString();

				if (path.equals(this.configFile.toString())) {
					if (!this.reloadProcessing) {
						this.reloadProcessing = true;
						while (true) {
							try {
								this.reload(true);
								break;
							} catch (Exception ex) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) { }
							}
						}
						this.reloadProcessing = false;
					}
				}
			}

			if (!key.reset()) break;
		}
	}

	public void save() throws InvalidConfigurationException {
		if (this.configFile == null) throw new IllegalArgumentException("Cannot save a config without given File");
		if (root == null) root = new ConfigSection();
		this.clearComments();
		this.internalSave(this.getClass());
		this.saveToYaml();
	}

	public void save(File file) throws InvalidConfigurationException {
		if (file == null) throw new IllegalArgumentException("File argument can not be null");
		this.configFile = file;
		this.save();
	}

	public void setSuppressFailedConversions() {
		this.setSuppressFailedConversions(true);
	}

	public void setSuppressFailedConversions(boolean suppress) {
		this.skipFailedConversion = suppress;
	}

	public void startWatcher() {
		if (this.taskId == -1) {
			try {
				this.watchService = FileSystems.getDefault().newWatchService();
				this.watchKey = this.configFile.toPath().getParent().register(this.watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
				this.taskId = this.getPlugin().getServer().getScheduler().runTaskTimerAsynchronously(this.getPlugin(), this, 0L, 5L).getTaskId();
			} catch (Exception ex) {
				this.getLog().console("Unable to start watch service!", ex);
			}
		}
	}

	public void stopWatcher() {
		if (this.taskId != -1) {
			this.getPlugin().getServer().getScheduler().cancelTask(this.taskId);
			this.taskId = -1;
			this.watchKey.cancel();

			try {
				this.watchService.close();
			} catch (IOException ioex) { }
		}
	}

	/**
	 * Called after the file is loaded but before the converter gets it.
	 * <p>
	 * Used to manually edit the passed root node when you updated the config.
	 * 
	 * @param configSection The root ConfigSection with all sub-nodes.
	 */
	public void update(ConfigSection section) { }

}