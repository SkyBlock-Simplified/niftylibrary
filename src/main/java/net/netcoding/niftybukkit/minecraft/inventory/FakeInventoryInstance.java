package net.netcoding.niftybukkit.minecraft.inventory;

import net.netcoding.niftybukkit.minecraft.items.ItemData;
import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

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
		this.open(this.getProfile());
	}

	public void open(BukkitMojangProfile target) {
		this.inventory.open(this.getProfile(), target, this);
	}

	public void update() {
		this.update(this.inventory.getItems().values());
	}

	public <T extends ItemStack> void update(T[] items) {
		this.update(Arrays.asList(items));
	}

	public <T extends ItemStack> void update(Collection<T> items) {
		this.clearItems();
		this.addAll(items);
		this.inventory.update(this.getProfile(), this);
	}

	public void update(Map<Integer, ItemData> items) {
		this.clearItems();
		this.putAll(items);
		this.inventory.update(this.getProfile(), this);
	}

}