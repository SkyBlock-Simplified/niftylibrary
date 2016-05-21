package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;

class FakeInventoryInfo {

	private final BukkitMojangProfile target;
	private final FakeInventoryFrame frame;
	private boolean opening = false;

	FakeInventoryInfo(BukkitMojangProfile target, FakeInventoryFrame frame) {
		this.target = target;
		this.frame = frame;
	}

	public final BukkitMojangProfile getTarget() {
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