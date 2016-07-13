package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.command.CommandSource;
import net.netcoding.nifty.common.minecraft.event.Cancellable;
import net.netcoding.nifty.common.minecraft.event.Event;
import net.netcoding.nifty.core.util.ListUtil;
import net.netcoding.nifty.core.util.StringUtil;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentList;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Map;

final class ListenerMap {

	private final transient ConcurrentList<String> errors = Concurrent.newList();
	private final transient ConcurrentMap<Class<?>, Method> validEvents = Concurrent.newMap();
	private final transient ConcurrentMap<Command, Method> validCommands = Concurrent.newMap();
	private final transient MinecraftPlugin plugin;
	private final transient Listener listener;

	ListenerMap(MinecraftPlugin plugin, Listener listener) {
		this.plugin = plugin;
		this.listener = listener;

		// Collect Events
		for (Method method : listener.getClass().getDeclaredMethods()) {
			// Set Accessible
			method.setAccessible(true);

			// Static Unsupported
			if (Modifier.isStatic(method.getModifiers()))
				continue;

			// Not An Event
			if (!method.isAnnotationPresent(net.netcoding.nifty.common.api.plugin.Event.class))
				continue;

			// Invalid Event
			if (method.getParameterCount() == 0 || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
				plugin.getLog().warn("Method ''{0}'' in ''{1}'' claims to be an event but has no event parameter!", method.getName(), this.getName());
				continue;
			}

			// Invalid Event
			if (method.getParameterCount() != 1) {
				plugin.getLog().warn("Method ''{0}'' in ''{1}'' is an event but has too many parameters!", method.getName(), listener.getClass().getName());
				continue;
			}

			// Save Event
			this.validEvents.put(method.getParameterTypes()[0], method);
		}

		// Collect Commands
		for (Method method : listener.getClass().getDeclaredMethods()) {
			// Set Accessible
			method.setAccessible(true);

			// Static Unsupported
			if (Modifier.isStatic(method.getModifiers()))
				continue;

			// Not A Command
			if (!method.isAnnotationPresent(Command.class))
				continue;

			// Already Registered
			Command command = method.getAnnotation(Command.class);
			for (ListenerMap listenerMap : Nifty.getPluginManager().getListeners(this.getPlugin())) {
				for (Command acommand : listenerMap.validCommands.keySet()) {
					if (command.name().equalsIgnoreCase(acommand.name())) {
						plugin.getLog().warn("Command ''{0}'' in ''{1}'' is already registered in ''{2}''!", command.name(), this.getName(), listenerMap.getName());
						return;
					}
				}
			}

			// Invalid Command
			if (method.getParameterCount() == 0 || !CommandSource.class.isAssignableFrom(method.getParameterTypes()[0])) {
				plugin.getLog().warn("Method ''{0}'' in ''{1}'' claims to be a command but has no CommandSource parameters! See javadoc for details.", method.getName(), this.getName());
				continue;
			}

			// Invalid Command
			if (method.getParameterCount() > 3) {
				plugin.getLog().warn("Method ''{0}'' in ''{1}'' is a command but has too many parameters! See javadoc for details.", method.getName(), listener.getClass().getName());
				continue;
			}

			// Save Command
			this.validCommands.put(command, method);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (obj == null || !ListenerMap.class.isAssignableFrom(obj.getClass()))
			return false;
		else {
			ListenerMap listenerMap = (ListenerMap)obj;
			return this.getListener().getClass().equals(listenerMap.getListener().getClass());
		}
	}

	public final List<String> getErrors() {
		return Collections.unmodifiableList(this.errors);
	}

	private static String getLastArg(String... args) {
		List<String> properArgs = getProperArgs(args);
		int size = properArgs.size();
		return (size > 0 ? properArgs.get(size - 1) : "");
	}

	public Listener getListener() {
		return this.listener;
	}

	public MinecraftLogger getLog() {
		return this.getPlugin().getLog();
	}

	public String getName() {
		return this.getListener().getClass().getSimpleName();
	}

	public MinecraftPlugin getPlugin() {
		return this.plugin;
	}

	private static List<String> getProperArgs(String... args) {
		List<String> argList = StringUtil.toList(args);
		int size = argList.size();

		if (size > 0) {
			size--;

			if (argList.get(size).matches("^\\?|help$"))
				argList.remove(size);
		}

		return argList;
	}

	@Override
	public int hashCode() {
		return 31 * this.getListener().hashCode();
	}

	/**
	 * Checks if the arguments are looking for help.
	 *
	 * @param args Arguments to check if are looking for help.
	 * @return True if looking for help, otherwise false.
	 */
	protected boolean isHelp(String... args) {
		return (args.length > 0 && args[args.length - 1].matches("^[\\?]+|help$"));
	}

	void noPerms(CommandSource source, String permission) {
		this.getLog().error(source, "You do not have the permission {{0}}!", permission);
	}

	public void process(Event event, int priority) {
		for (Map.Entry<Class<?>, Method> entry : this.validEvents.entrySet()) {
			Class<?> eventClass = entry.getClass();
			Method method = entry.getValue();

			// Incorrect Event
			if (!eventClass.isAssignableFrom(event.getClass()))
				continue;

			// Details
			net.netcoding.nifty.common.api.plugin.Event eventAnno = method.getAnnotation(net.netcoding.nifty.common.api.plugin.Event.class);
			net.netcoding.nifty.common.api.plugin.Event.Priority eventPriority = eventAnno.priority();

			// Check Priority
			if (eventPriority.getSlot() == priority) {
				// Skip?
				if (event instanceof Cancellable) {
					if (((Cancellable) event).isCancelled() && !eventAnno.ignoreCancelled())
						continue;
				}

				try {
					method.invoke(this.getListener(), event);
				} catch (Exception ex) {
					this.getPlugin().getLog().console(ex);
				}
			}
		}
	}

	public boolean process(final CommandSource source, String label, final String[] args) {
		// Check Previous Command
		if (Nifty.getPluginManager().getRunning(source) != null) {
			this.getLog().error(source, "Still processing previously issued command!");
			return true;
		}

		// Plugin Name
		final String parsedLabel;
		if (label.contains(":")) {
			String[] parts = label.split(":");
			parsedLabel = parts[1];

			if (!this.getPlugin().getName().equalsIgnoreCase(parts[0]))
				return false;
		} else
			parsedLabel = label;

		for (Map.Entry<Command, Method> entry : this.validCommands.entrySet()) {
			// Details
			Command command = entry.getKey();
			Method method = entry.getValue();

			// Check Command & Aliases
			if (!command.name().equalsIgnoreCase(parsedLabel)) {
				boolean skip = true;

				for (String alias : command.aliases()) {
					if (alias.equalsIgnoreCase(parsedLabel)) {
						skip = false;
						break;
					}
				}

				if (skip)
					continue;
			}

			Nifty.getScheduler().schedule(() -> {
				// Console Only
				if (command.consoleOnly() && !MinecraftHelper.isConsole(source)) {
					this.getLog().error(source, "The command {{0}} is only possible from console!", command.name());
					return;
				}

				// Player Only
				if (command.playerOnly() && !MinecraftHelper.isPlayer(source)) {
					this.getLog().error(source, "The command {0} is not possible from console!", command.name());
					return;
				}

				// Bungee Required
				if (command.bungeeOnly() && !Nifty.getBungeeHelper().getDetails().isDetected()) {
					this.getLog().error(source, "The command {{0}} requires BungeeCord!", command.name());
					return;
				}

				// Check Permissions
				if (MinecraftHelper.isPlayer(source)) {
					if (command.checkPerms() && !source.hasPermission(command.permission())) {
						this.noPerms(source, command.permission());
						return;
					}
				}

				// Check Minimum Args
				if (command.minimumArgs() > 0 && args.length < command.minimumArgs()) {
					showUsage(this.getLog(), source, command, parsedLabel, args);
					return;
				}

				// Check Maximum Args
				if (command.maximumArgs() > 0 && args.length > command.maximumArgs()) {
					showUsage(this.getLog(), source, command, parsedLabel, args);
					return;
				}

				// Check Help
				if (command.checkHelp() && this.isHelp(args)) {
					showUsage(this.getLog(), source, command, parsedLabel, args);
					return;
				}

				try {
					Nifty.getPluginManager().putRunning(source, command, parsedLabel, args);

					if (CommandSource.class.isAssignableFrom(method.getParameterTypes()[0])) {
						if (method.getParameterCount() == 3) {
							if (String.class.equals(method.getParameterTypes()[1]) && String[].class.equals(method.getParameterTypes()[2]))
								method.invoke(this.getListener(), source, parsedLabel, args);
							else
								this.sendMethodError(source, command, method);
						} else if (method.getParameterCount() == 2) {
							if (String[].class.equals(method.getParameterTypes()[1]))
								method.invoke(this.getListener(), source, args);
							else if (String.class.equals(method.getParameterTypes()[1]))
								method.invoke(this.getListener(), source, parsedLabel);
							else
								this.sendMethodError(source, command, method);
						} else
							method.invoke(this.getListener(), source);
					} else
						this.sendMethodError(source, command, method);
				} catch (Exception ex) {
					this.getLog().error(source, "Unable to process command ''{0}''!", ex, command.name());
				}

				Nifty.getPluginManager().clearRunning(source);
			});

			return true;
		}

		return false;
	}

	private void sendMethodError(CommandSource source, Command command, Method method) {
		this.getLog().error(source, "Unable to process command ''{0}''!", command.name());
		this.getLog().console("Method ''{0}'' in ''{1}'' has invalid parameter types, see javadoc for details!", method.getName(), this.getName());
	}

	/**
	 * Shows command usage if arguments end in a question mark or help.
	 * Also if they have too few or too many arguments.
	 *
	 * @param source The source to send the usage to.
	 * @param command The command this usage is for.
	 * @param label The label to use in the usage (defaults to {@link Command#name()}).
	 */
	static void showUsage(MinecraftLogger log, CommandSource source, Command command, String label, String[] args) {
		if (Nifty.getPluginManager().getRunning(source) != null) {
			String usage = command.usage().replace("<command>", StringUtil.notEmpty(label) ? label : command.name());
			args = ListUtil.toArray(getProperArgs(args), String.class);

			for (int i = args.length; i >= 0; i--) {
				ConcurrentMap<String, String> usageMap = Concurrent.newMap();

				for (Command.Usage commandUsage : command.usages())
					usageMap.put(commandUsage.match(), commandUsage.replace());

				if (!usageMap.isEmpty()) {
					String lastArg = (i == 0 ? label : getLastArg(args));

					if (usageMap.containsKey(lastArg)) {
						String argStart = (i > 0 ? StringUtil.format("{0} ", StringUtil.implode(" ", args)) : "");
						usage = StringUtil.format("/{0} {1}{2}", label, argStart, usageMap.get(lastArg));
						args = new String[] {};
					}

					break;
				} else if (ListUtil.notEmpty(args))
					args = StringUtil.split(" ", StringUtil.implode(" ", args, 0, args.length - 1));
			}

			log.message(source, log.getPrefix("Usage") + usage, (Object[]) args);
		}
	}

}