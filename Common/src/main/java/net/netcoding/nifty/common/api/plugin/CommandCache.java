package net.netcoding.nifty.common.api.plugin;

final class CommandCache {

	private final Command command;
	private final String label;
	private final String[] args;

	CommandCache(Command command, String label, String[] args) {
		this.command = command;
		this.label = label;
		this.args = args;
	}

	public Command getCommand() {
		return this.command;
	}

	public String getLabel() {
		return this.label;
	}

	public String[] getArgs() {
		return this.args;
	}

}