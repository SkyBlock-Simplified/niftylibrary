package net.netcoding.niftybukkit.inventory;

import net.netcoding.niftybukkit.mojang.MojangProfile;

import org.bukkit.plugin.java.JavaPlugin;

public class FakeInventoryInstance extends FakeInventoryFrame {

	private final transient FakeInventory inventory;
	private final transient MojangProfile profile;

	FakeInventoryInstance(JavaPlugin plugin, FakeInventory inventory, MojangProfile profile) {
		super(plugin);
		this.inventory = inventory;
		this.profile = profile;
	}

	public void close() {
		this.inventory.close(this.getProfile());
	}

	public MojangProfile getProfile() {
		return this.profile;
	}

	public boolean isOpen() {
		return this.inventory.isOpen(this.getProfile());
	}

	public void open() {
		this.open(this.getProfile());
	}

	public void open(MojangProfile target) {
		this.inventory.open(this.getProfile(), target, this);
	}

}