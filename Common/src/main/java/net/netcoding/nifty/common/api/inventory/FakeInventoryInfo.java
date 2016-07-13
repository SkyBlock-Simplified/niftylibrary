package net.netcoding.nifty.common.api.inventory;

import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

class FakeInventoryInfo {

	private final MinecraftMojangProfile target;
	private final FakeInventoryFrame frame;
	private boolean opening = false;

	FakeInventoryInfo(MinecraftMojangProfile target, FakeInventoryFrame frame) {
		this.target = target;
		this.frame = frame;
	}

	public final MinecraftMojangProfile getTarget() {
		return this.target;
	}

	public final FakeInventoryFrame getFrame() {
		return this.frame;
	}

	public final boolean isOpening() {
		return this.opening;
	}

	public final void setOpening() {
		this.setOpening(true);
	}

	public final void setOpening(boolean value) {
		this.opening = value;
	}
}