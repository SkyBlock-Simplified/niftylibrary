package net.netcoding.nifty.common.api.plugin;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.command.CommandSource;

public abstract class MinecraftListener extends MinecraftHelper implements Listener {

	public MinecraftListener(MinecraftPlugin plugin) {
		super(plugin);
		Nifty.getPluginManager().registerListener(plugin, this);
	}

	/**
	 * Shows command usage if arguments end in a question mark or help.
	 * Also if they have too few or too many arguments.
	 * <p>
	 * Only works inside a method annotated with {@link Command}.
	 *
	 * @param source The source to send the usage to
	 */
	public final void showUsage(CommandSource source) {
		this.showUsage(source, Nifty.getPluginManager().getRunning(source).getLabel());
	}

	/**
	 * Shows command usage if arguments end in a question mark or help.
	 * Also if they have too few or too many arguments.
	 * <p>
	 * Only works inside a method annotated with {@link Command}.
	 *
	 * @param source The source to send the usage to
	 * @param label The label to use in the usage (defaults to {@link Command#name()})
	 */
	public final void showUsage(CommandSource source, String label) {
		CommandCache cached = Nifty.getPluginManager().getRunning(source);
		ListenerMap.showUsage(this.getLog(), source, cached.getCommand(), label, cached.getArgs());
	}

}