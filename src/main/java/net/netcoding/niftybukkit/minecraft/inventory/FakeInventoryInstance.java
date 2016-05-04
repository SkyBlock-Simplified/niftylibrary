package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.plugin.java.JavaPlugin;

public class FakeInventoryInstance extends FakeInventoryFrame {

	private final transient FakeInventory inventory;
	private final transient BukkitMojangProfile profile;

	FakeInventoryInstance(JavaPlugin plugin, FakeInventory inventory, BukkitMojangProfile profile) {
		super(plugin);
		this.inventory = inventory;
		this.profile = profile;
	}

	FakeInventoryInstance(FakeInventoryFrame frame, FakeInventory inventory, BukkitMojangProfile profile) {
		super(frame);
		this.inventory = inventory;
		this.profile = profile;
	}

	public void close() {
		this.inventory.close(this.getProfile());
	}

	public BukkitMojangProfile getProfile() {
		return this.profile;
	}

	public BukkitMojangProfile getTarget() {
		return this.inventory.getTarget(this.getProfile());
	}

	public boolean isOpen() {
		return this.inventory.isOpen(this.getProfile());
	}

	public void open() {
		this.open(this.isOpen() ? this.getTarget() : this.getProfile());
	}

	public void open(BukkitMojangProfile target) {
		this.inventory.open(this.getProfile(), target, this);
	}

}