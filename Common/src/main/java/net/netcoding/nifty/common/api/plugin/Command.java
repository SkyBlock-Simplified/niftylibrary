package net.netcoding.nifty.common.api.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This automatically registers a method to be used for handling a command.
 * <p>
 * Supported arguments:<br>
 * - 1 Argument: CommandSource<br>
 * - 2 Arguments: CommandSource, String (label) / String[] (arguments)<br>
 * - 3 Arguments: CommandSource, String (label), String[] (arguments)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

	/**
	 * The command name.
	 *
	 * @return The command name.
	 */
	String name();

	/**
	 * The parent command name this belongs to.
	 * <p>
	 * This can belong to any plugin, including those outside NiftyLib. It will<br>
	 * quietly handle command calls such as: {@literal /parent <this> [args]}.
	 * <p>
	 * Note: If this is used, {@link #requiresParent()} is true, and this is<br>
	 * not found then, this command will not be registered.
	 *
	 * @return The parent command name.
	 */
	String parent() default "";

	/**
	 * Checks if this command can only be run when BungeeCord is detected.
	 *
	 * @return True if bungee detected.
	 */
	boolean bungeeOnly() default false;

	/**
	 * Checks if the users permissions will be checked immediately upon running the command.
	 *
	 * @return True if checking.
	 */
	boolean checkPerms() default true;

	/**
	 * Checks if the arguments are being looked at for help.
	 *
	 * @return True if checking.
	 */
	boolean checkHelp() default true;

	/**
	 * Checks if this command can only be ran as console.
	 *
	 * @return True if console only.
	 */
	boolean consoleOnly() default false;

	/**
	 * The minimum amount of arguments that need to be passed to this command.
	 *
	 * @return Minimum amount of arguments required.
	 */
	int minimumArgs() default 1;

	/**
	 * The maximum amount of arguments that need to be passed to this command.
	 *
	 * @return Maximum amount of arguments allowed.
	 */
	int maximumArgs() default -1;

	/**
	 * The permission to check before running this command.
	 * <p>
	 * Defaults to {@link MinecraftPlugin#getName()}.{@link #name()}.<br>
	 * Example: niftylib.nifty
	 *
	 * @return Permission to check when running this command.
	 */
	String permission() default "";

	/**
	 * Checks if this command can only be ran as a player.
	 *
	 * @return True if player only.
	 */
	boolean playerOnly() default false;

	/**
	 * Checks if this command will tab complete for player names.
	 *
	 * @return True if auto tab completing.
	 */
	boolean playerTabComplete() default false;

	/**
	 * The index to use when automatically tab completing player names.
	 * <p>
	 * -1 for all, 0+ for argument index
	 *
	 * @return The index to use when tab completing player names.
	 */
	int playerTabCompleteIndex() default 0;

	/**
	 * Checks if the parent command is required to register this command.
	 *
	 * @return True if parent command is required.
	 */
	boolean requiresParent() default false;

	/**
	 * Checks if parent command call is required to run this command.
	 * <p>
	 * Example: {@literal /parent <this> [args]}.
	 *
	 * @return True if parent command call required.
	 */
	boolean requiresParentCall() default false;

	/**
	 * Aliases to link to this command.
	 *
	 * @return Aliases that act as this command.
	 */
	String[] aliases() default {};

	/**
	 * Usage for this command.
	 * <p>
	 * Custom using {@link #usages()}.
	 *
	 * @return Usage for this command.
	 */
	String usage() default "/<command>";

	/**
	 * Custom usages based on label or argument.
	 *
	 * @return The custom usages.
	 */
	Usage[] usages() default {};

	@interface TabComplete {

		/**
		 * The index to use when tab completing.
		 * <p>
		 * -1 fires for all (user handling), 0+ for argument index
		 *
		 * @return The index to use when tab completing.
		 */
		int index() default -1;

		/**
		 * The command name this tab complete belongs to.
		 *
		 * @return The command name this belongs to.
		 */
		String name();

	}

	@interface Usage {

		/**
		 * The index of the argument.
		 * <p>
		 * 0 to modify label usage, 1+ to modify argument usage.
		 *
		 * @return The index of the usage argument.
		 */
		int index() default 1;

		/**
		 * The label or argument to match.
		 *
		 * @return The label or argument this modifies.
		 */
		String match();

		/**
		 * The modified usage to display (after the specified {@link Usage#match()}.
		 *
		 * @return The modified usage.
		 */
		String replace() default "";

	}

}