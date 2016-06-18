package net.netcoding.nifty.common._new_.api.plugin;

import com.google.common.base.Preconditions;
import net.netcoding.nifty.common._new_.minecraft.event.Cancellable;
import net.netcoding.nifty.common._new_.minecraft.event.Event;
import net.netcoding.nifty.common._new_.api.Listener;
import net.netcoding.niftycore.api.plugin.Plugin;
import net.netcoding.niftycore.util.StringUtil;
import net.netcoding.niftycore.util.concurrent.ConcurrentMap;
import net.netcoding.niftycore.util.concurrent.ConcurrentSet;

import java.lang.reflect.Method;
import java.util.Map;

public abstract class PluginManager {

	private final transient ConcurrentMap<Plugin, ConcurrentSet<Listener>> listeners = new ConcurrentMap<>();

	protected PluginManager() { }

	public void call(Event event) {
		ConcurrentSet<Method> ignore = new ConcurrentSet<>();

		for (int i = 0; i <= 5; i++) {
			for (Map.Entry<Plugin, ConcurrentSet<Listener>> entry : listeners.entrySet()) {
				Plugin plugin = entry.getKey();

				for (Listener listener : entry.getValue()) {
					for (Method method : listener.getClass().getDeclaredMethods()) {
						// Invalid Event (Previously Processed)
						if (ignore.contains(method))
							return;

						// Set Accessible
						method.setAccessible(true);

						// Not An Event
						if (!method.isAnnotationPresent(net.netcoding.nifty.common._new_.api.Event.class))
							continue;

						// Invalid Event
						if (method.getParameterCount() > 1) {
							plugin.getLog().warn("Method ''{0}'' in ''{1}'' must only contain parameter ''{2}''!", method.getName(), listener.getClass().getName(), event.getClass().getName());
							ignore.add(method);
							continue;
						}

						// Details
						net.netcoding.nifty.common._new_.api.Event eventAnno = method.getAnnotation(net.netcoding.nifty.common._new_.api.Event.class);
						net.netcoding.nifty.common._new_.api.Event.Priority priority = eventAnno.priority();

						// Check Priority
						if (priority.getSlot() == i) {
							// Skip?
							if (event instanceof Cancellable) {
								if (((Cancellable)event).isCancelled() && !eventAnno.ignoreCancelled())
									continue;
							}

							try {
								method.invoke(listener, event);
							} catch (Exception ex) {
								plugin.getLog().console(ex);
							}
						}
					}
				}
			}
		}
	}

	public boolean hasRegistered(Plugin plugin) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		return this.listeners.containsKey(plugin);
	}

	public abstract boolean hasPlugin(String name);

	public boolean isRegistered(Plugin plugin, Listener listener) {
		Preconditions.checkArgument(plugin != null, "Plugin cannot be NULL!");
		Preconditions.checkArgument(listener != null, "Listener cannot be NULL!");
		return this.listeners.containsKey(plugin) && this.listeners.get(plugin).contains(listener);
	}

	public abstract boolean isPluginEnabled(String name);

	public void registerListener(Plugin plugin, Listener listener) {
		if (!this.isRegistered(plugin, listener)) {
			if (!this.listeners.containsKey(plugin))
				this.listeners.put(plugin, new ConcurrentSet<>());

			this.listeners.get(plugin).add(listener);
		} else
			throw new IllegalArgumentException(StringUtil.format("Listener has already been registered for plugin ''{0}''!", plugin.getName()));
	}

	public void unregisterListener(Plugin plugin, Listener listener) {
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

	public void unregisterListeners(Plugin plugin) {
		if (this.hasRegistered(plugin))
			this.listeners.remove(plugin);
		else
			throw new IllegalArgumentException(StringUtil.format("Plugin ''{0}'' has no registered listeners!", plugin.getName()));
	}

}