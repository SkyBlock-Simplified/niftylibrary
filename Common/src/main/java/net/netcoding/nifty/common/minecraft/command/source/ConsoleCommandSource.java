package net.netcoding.nifty.common.minecraft.command.source;

public interface ConsoleCommandSource extends CommandSource {

	default void sendMessage(String message) {
		System.out.println(message);
	}

}