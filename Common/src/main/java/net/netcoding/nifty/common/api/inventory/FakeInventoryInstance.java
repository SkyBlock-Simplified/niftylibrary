package net.netcoding.nifty.common.api.inventory;

import net.netcoding.nifty.common.mojang.MinecraftMojangProfile;

public class FakeInventoryInstance extends FakeInventoryFrame {

	private final transient FakeInventory inventory;
	private final transient MinecraftMojangProfile profile;

	FakeInventoryInstance(FakeInventory inventory, MinecraftMojangProfile profile) {
		this.inventory = inventory;
		this.profile = profile;
	}

	FakeInventoryInstance(FakeInventoryFrame frame, FakeInventory inventory, MinecraftMojangProfile profile) {
		super(frame);
		this.inventory = inventory;
		this.profile = profile;
	}

	public void close() {
		this.inventory.close(this.getProfile());
	}

	public MinecraftMojangProfile getProfile() {
		return this.profile;
	}

	public MinecraftMojangProfile getTarget() {
		return this.inventory.getTarget(this.getProfile());
	}

	public boolean isOpen() {
		return this.inventory.isOpen(this.getProfile());
	}

	public void open() {
		this.open(this.isOpen() ? this.getTarget() : this.getProfile());
	}

	public void open(MinecraftMojangProfile target) {
		this.inventory.open(this.getProfile(), target, this);
	}

}