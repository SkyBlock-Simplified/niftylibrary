package net.netcoding.nifty.common.api.plugin;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.common.minecraft.event.Event;
import net.netcoding.nifty.core.api.plugin.PluginDescription;
import net.netcoding.nifty.core.api.plugin.annotations.Plugin;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public abstract class PluginManager {

	private final transient ConcurrentMap<MinecraftPlugin, PluginDetails> plugins = Concurrent.newMap();
	private final transient ConcurrentMap<MinecraftPlugin, ConcurrentSet<ListenerMap>> listeners = Concurrent.newMap();
	private final transient ConcurrentMap<CommandSource, CommandCache> running = Concurrent.newMap();

	public void call(Event event) {
		for (int i = 0; i <= 5; i++) {
			for (Map.Entry<MinecraftPlugin, ConcurrentSet<ListenerMap>> entry : this.listeners.entrySet()) {
				for (ListenerMap listenerMap : entry.getValue())
					listenerMap.process(event, i);
			}
		}
	}

	void clearRunning(CommandSource source) {
		this.running.remove(source);
	}

	public void dispatch(CommandSource source, String command, String... args) {
		for (Map.Entry<MinecraftPlugin, ConcurrentSet<ListenerMap>> entry : this.listeners.entrySet()) {
			for (ListenerMap listenerMap : entry.getValue()) {
				if (listenerMap.process(source, command, args))
					return;
			}
		}
	}

	public final MinecraftPlugin getPlugin(String name) {
		for (MinecraftPlugin iplugin : this.plugins.keySet()) {
			if (iplugin.getName().equalsIgnoreCase(name))
				return iplugin;
		}

		return null;
	}

	public final <T extends MinecraftPlugin> T getPlugin(Class<T> plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");

		for (MinecraftPlugin iplugin : this.plugins.keySet()) {
			if (iplugin.getClass().equals(plugin))
				return plugin.cast(iplugin);
		}

		return null;
	}

	<T extends MinecraftPlugin> PluginDescription getPluginDescription(T plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");

		for (Map.Entry<MinecraftPlugin, PluginDetails> entry : this.plugins.entrySet()) {
			if (entry.getKey().getClass().equals(plugin.getClass()))
				return entry.getValue().getDescription();
		}

		return null;
	}

	public final List<String> getPluginErrors(MinecraftPlugin plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		ConcurrentList<String> errors = Concurrent.newList();

		for (ListenerMap listenerMap : this.listeners.get(plugin))
			errors.addAll(listenerMap.getErrors());

		return errors;
	}

	public final List<String> getListenerErrors(MinecraftPlugin plugin, Listener listener) {
		if (this.isRegistered(plugin, listener)) {
			for (ListenerMap listenerMap : this.listeners.get(plugin)) {
				if (listenerMap.getListener().getClass().equals(listener.getClass()))
					return listenerMap.getErrors();
			}
		}

		return Collections.emptyList();
	}

	Set<ListenerMap> getListeners(MinecraftPlugin plugin) {
		return this.listeners.get(plugin);
	}

	<T extends MinecraftPlugin> MinecraftLogger getPluginLog(T plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");

		for (Map.Entry<MinecraftPlugin, PluginDetails> entry : this.plugins.entrySet()) {
			if (entry.getKey().getClass().equals(plugin.getClass()))
				return entry.getValue().getLog();
		}

		return null;
	}

	public final Set<MinecraftPlugin> getPlugins() {
		return Collections.unmodifiableSet(this.plugins.keySet());
	}

	CommandCache getRunning(CommandSource source) {
		return this.running.get(source);
	}

	public final boolean hasPlugin(String name) {
		for (MinecraftPlugin iplugin : this.plugins.keySet()) {
			if (iplugin.getName().equalsIgnoreCase(name))
				return true;
		}

		return false;
	}

	public final boolean hasRegistered(MinecraftPlugin plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		return this.listeners.containsKey(plugin);
	}

	public boolean isRegistered(MinecraftPlugin plugin, Listener listener) {
		Preconditions.checkArgument(listener != null, "Listener cannot be NULL!");

		if (this.hasRegistered(plugin)) {
			for (ListenerMap listenerMap : this.listeners.get(plugin)) {
				if (listenerMap.getListener().getClass().equals(listener.getClass()))
					return true;
			}
		}

		return false;
	}

	public final boolean isEnabled(String name) {
		for (MinecraftPlugin iplugin : this.plugins.keySet()) {
			if (iplugin.getName().equalsIgnoreCase(name))
				return iplugin.isEnabled();
		}

		return false;
	}

	@SuppressWarnings("unchecked")
	public final void loadPlugins() {
		if (this.plugins.size() > 0)
			throw new IllegalStateException("Plugins have already been loaded!");

		File pluginsDir = new File(Nifty.getPlugin().getDataFolder(), "modules");
		File[] plugins = pluginsDir.listFiles((dir, name) -> StringUtil.notEmpty(name) && name.endsWith(".jar"));
		Yaml yaml = new Yaml();

		for (File plugin : plugins) {
			try {
				JarFile jar = new JarFile(plugin);
				URLClassLoader loader = URLClassLoader.newInstance(new URL[] { plugin.toURI().toURL() }, this.getClass().getClassLoader());
				Set<JarEntry> entries = jar.stream().filter(entry -> !entry.isDirectory() && entry.getName().endsWith(".class")).collect(Collectors.toSet());
				Class<? extends MinecraftPlugin> main;

				for (JarEntry entry : entries) {
					String className = entry.getName().replace("/", ".").replace(".class", "");

					try {
						Class<?> clazz = loader.loadClass(className);

						if (MinecraftPlugin.class.isAssignableFrom(clazz)) {
							try {
								main = clazz.asSubclass(MinecraftPlugin.class);
								String name;
								String version;

								if (!main.isAnnotationPresent(Plugin.class)) {
									JarEntry pluginYml = jar.getJarEntry("plugin.yml");

									if (pluginYml != null) {
										InputStream stream = jar.getInputStream(pluginYml);
										Map<String, Object> map = (Map<String, Object>)yaml.load(stream);
										name = map.get("name").toString();
										version = map.get("version").toString();
										// TODO: Read depends, softdepends, commands and permissions
									} else {
										Nifty.getLog().console("Located main class but plugin does not have a plugin.yml or Plugin annotation!");
										break;
									}
								} else {
									Plugin pluginAnno = main.getAnnotation(Plugin.class);
									name = pluginAnno.name();
									version = pluginAnno.version();
									// TODO: Read dependencies
								}

								if (!name.matches("^[A-Za-z0-9 _.-]+$"))
									throw new IllegalArgumentException(StringUtil.format("Plugin name ''{0}'' contains invalid characters!", name));

								if (name.contains(" "))
									Nifty.getLog().warn("Plugin name ''{0}'' contains spaces in the name. This is not recommended.");

								name = name.replace(" ", "_");
								MinecraftPlugin iplugin = (MinecraftPlugin)new Reflection(main).newInstance();
								File dataFolder = new File(plugin.getAbsoluteFile().getParentFile(), name);
								this.plugins.put(iplugin, new PluginDetails(iplugin, new PluginDescription(name, plugin, dataFolder, version)));
								iplugin.onEnable();
							} catch (Exception ignore) {
								// Will never throw
							}
						}
					} catch (ClassNotFoundException ignore) {
						// Will never throw
					}
				}
			} catch (IOException ioex) {
				Nifty.getLog().console("Unable to processes jar ''{0}''!", plugin.getName());
			}
		}
	}

	protected abstract void injectCommand(Command command);

	void putRunning(CommandSource source, Command command, String label, String[] args) {
		this.running.put(source, new CommandCache(command, label, args));
	}

	void setEnabled(MinecraftPlugin plugin, boolean value) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		this.plugins.entrySet().stream().filter(entry -> entry.getKey().getClass().equals(plugin.getClass())).forEach(entry -> entry.getValue().setEnabled(value));
	}

	public final void registerListener(MinecraftPlugin plugin, Listener listener) {
		if (this.hasPlugin(plugin.getName())) {
			if (!this.isRegistered(plugin, listener)) {
				if (!this.listeners.containsKey(plugin))
					this.listeners.put(plugin, Concurrent.newSet());

				this.listeners.get(plugin).add(new ListenerMap(plugin, listener));
			} else
				throw new IllegalArgumentException(StringUtil.format("Listener has already been registered for plugin ''{0}''!", plugin.getName()));
		} else
			throw new IllegalStateException(StringUtil.format("Plugin ''{0}'' has not been registered in PluginManager!", plugin.getName()));
	}

	public final void unregisterListener(MinecraftPlugin plugin, Listener listener) {
		if (this.hasRegistered(plugin)) {
			if (this.isRegistered(plugin, listener)) {
				this.listeners.get(plugin).remove(listener);

				if (this.listeners.get(plugin).isEmpty())
					this.listeners.remove(plugin);
			} else
				throw new IllegalArgumentException(StringUtil.format("Listener has not been registered for plugin ''{0}''!", plugin.getName()));
		} else
			throw new IllegalArgumentException(StringUtil.format("Plugin ''{0}'' has no registered listeners!", plugin.getName()));
	}

	public final void unregisterListeners(MinecraftPlugin plugin) {
		if (this.hasRegistered(plugin))
			this.listeners.remove(plugin);
		else
			throw new IllegalArgumentException(StringUtil.format("Plugin ''{0}'' has no registered listeners!", plugin.getName()));
	}

}