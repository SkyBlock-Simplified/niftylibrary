package net.netcoding.nifty.common.minecraft.block;

import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

import java.util.Arrays;

public enum PistonMoveReaction {

	MOVE(0),
	BREAK(1),
	BLOCK(2);

	private static final ConcurrentMap<Integer, PistonMoveReaction> BY_ID = new ConcurrentMap<>();
	private final int id;

	static {
		Arrays.stream(values()).forEach(reaction -> BY_ID.put(reaction.getId(), reaction));
	}

	PistonMoveReaction(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static PistonMoveReaction getById(int id) {
		return BY_ID.get(id);
	}

}