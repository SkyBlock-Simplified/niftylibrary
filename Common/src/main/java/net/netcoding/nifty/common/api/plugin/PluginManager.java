package net.netcoding.nifty.common.api.plugin;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.common.minecraft.event.Event;
import net.netcoding.nifty.common.minecraft.event.server.GameStoppingEvent;
import net.netcoding.nifty.core.api.plugin.PluginDescription;
import net.netcoding.nifty.core.api.plugin.annotations.Dependency;
import net.netcoding.nifty.core.api.plugin.annotations.Plugin;
import net.netcoding.nifty.core.reflection.Reflection;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.core.util.concurrent.ConcurrentSet;
import net.netcoding.nifty.core.util.concurrent.linked.ConcurrentLinkedMap;
import net.netcoding.nifty.core.util.misc.DirectedGraph;
import net.netcoding.nifty.core.util.misc.TopologicalSort;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public abstract class PluginManager {

	private final transient ConcurrentLinkedMap<MinecraftPlugin, PluginDetails> plugins = Concurrent.newLinkedMap();
	private final transient ConcurrentLinkedMap<MinecraftPlugin, ConcurrentSet<ListenerMap>> listeners = Concurrent.newLinkedMap();
	private final transient ConcurrentLinkedMap<CommandSource, CommandCache> running = Concurrent.newLinkedMap();
	private transient boolean acceptingRegistrations = true;

	protected PluginManager(MinecraftPlugin plugin, PluginDescription desc) {
		this.plugins.put(plugin, new PluginDetails(plugin, desc));
		new PluginUnloader();
	}

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

	public boolean isAcceptingRegistrations() {
		return this.acceptingRegistrations;
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

	public final void loadPlugins() {
		this.acceptingRegistrations = false;
		File pluginsDir = new File(Nifty.getPlugin().getDataFolder(), "plugins");
		File[] plugins = pluginsDir.listFiles((dir, name) -> StringUtil.notEmpty(name) && name.endsWith(".jar"));
		DirectedGraph<String> graph = new DirectedGraph<>();

		for (File plugin : plugins) {
			try {
				JarFile jar = new JarFile(plugin);
				URLClassLoader loader = URLClassLoader.newInstance(new URL[] { plugin.toURI().toURL() }, this.getClass().getClassLoader());
				Set<JarEntry> entries = jar.stream().filter(entry -> !entry.isDirectory() && entry.getName().endsWith(".class")).collect(Collectors.toSet());
				Class<? extends MinecraftPlugin> main;

				for (JarEntry entry : entries) {
					String className = entry.getName().replace("/", ".").replace(".class", "");
					boolean found = false;

					try {
						Class<?> clazz = loader.loadClass(className);

						if (MinecraftPlugin.class.isAssignableFrom(clazz)) {
							main = clazz.asSubclass(MinecraftPlugin.class);
							found = true;

							if (main.isAnnotationPresent(Plugin.class)) {
								Plugin pluginAnno = main.getAnnotation(Plugin.class);
								String name = pluginAnno.name();

								if (!name.matches("^[A-Za-z0-9 _.-]+$"))
									throw new IllegalArgumentException(StringUtil.format("Plugin name ''{0}'' contains invalid characters!", name));

								if (name.contains(" "))
									Nifty.getLog().warn("Plugin name ''{0}'' contains spaces in the name. This is not recommended.", name);

								name = name.replace(" ", "_");
								graph.addNode(name);

								if (pluginAnno.dependencies() != null) {
									for (Dependency dependency : pluginAnno.dependencies())
										graph.addEdge(name, dependency.name());
								}

								MinecraftPlugin iplugin = (MinecraftPlugin)new Reflection(main).newInstance();
								File dataFolder = new File(plugin.getAbsoluteFile().getParentFile(), name);
								this.plugins.put(iplugin, new PluginDetails(iplugin, new PluginDescription(name, plugin, dataFolder, pluginAnno.version())));
							} else
								throw new IllegalStateException(StringUtil.format("Plugin ''{0}'' is missing @Plugin annotation!", main.getName()));
						}
					} catch (ClassNotFoundException ignore) { /* Will never throw */ }

					if (found)
						break;
				}
			} catch (IOException ioex) {
				Nifty.getLog().console("Unable to processes jar ''{0}''!", plugin.getName());
			}
		}

		ConcurrentList<String> sorted = TopologicalSort.sort(graph);
		for (String name : sorted)
			this.plugins.keySet().stream().filter(plugin -> plugin.getName().equalsIgnoreCase(name)).forEach(MinecraftPlugin::onEnable);
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

	private class PluginUnloader extends MinecraftListener {

		public PluginUnloader() {
			super(Nifty.getPlugin());
		}

		@net.netcoding.nifty.common.api.plugin.Event
		public void onGameStopping(GameStoppingEvent event) {
			PluginManager.this.plugins.keySet().stream()
					.collect(Collectors.toCollection(LinkedList::new))
					.descendingIterator().forEachRemaining(plugin -> {
				PluginManager.this.unregisterListeners(plugin);

				if (!plugin.equals(Nifty.getPlugin()))
					plugin.onDisable();
			});

			PluginManager.this.plugins.clear();
			PluginManager.this.acceptingRegistrations = true;
		}

	}

}