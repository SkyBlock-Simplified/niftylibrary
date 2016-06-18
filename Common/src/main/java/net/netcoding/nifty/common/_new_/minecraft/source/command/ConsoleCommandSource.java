package net.netcoding.nifty.common._new_.minecraft.source.command;

public interface ConsoleCommandSource extends CommandSource {

	default void sendMessage(String message) {
		System.out.println(message);
	}

}