package net.netcoding.nifty.common.minecraft.command;

import net.netcoding.nifty.core.util.json.JsonMessage;

public interface ConsoleCommandSource extends CommandSource {

	@Override
	default void sendMessage(JsonMessage message) throws Exception {
		this.sendRawMessage(message.toLegacyText());
	}

	@Override
	default void sendMessage(String message) {
		System.out.println(message);
	}

}