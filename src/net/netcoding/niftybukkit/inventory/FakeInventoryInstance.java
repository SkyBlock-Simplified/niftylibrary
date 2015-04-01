package net.netcoding.niftybukkit.inventory;

import java.util.Arrays;
import java.util.Collection;

import net.netcoding.niftybukkit.mojang.MojangProfile;
import net.netcoding.niftybukkit.util.ListUtil;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

	public void update() {
		this.update(this.inventory.getOpened().keySet());
	}

	public void update(MojangProfile profile) {
		this.update(Arrays.asList(profile));
	}

	public void update(Collection<MojangProfile> profiles) {
		this.inventory.update(profiles, this);;
	}

	public <T extends ItemStack> void update(T[] items) {
		this.update(this.inventory.getOpened().keySet(), Arrays.asList(items));
	}

	public <T extends ItemStack> void update(MojangProfile profile, T[] items) {
		this.update(Arrays.asList(profile), Arrays.asList(items));
	}

	public <T extends ItemStack> void update(Collection<MojangProfile> profiles, T[] items) {
		this.update(profiles, Arrays.asList(items));
	}

	public <T extends ItemStack> void update(MojangProfile profile, Collection<? extends T> items) {
		this.update(Arrays.asList(profile), items);
	}

	public <T extends ItemStack> void update(Collection<MojangProfile> profiles, Collection<? extends T> items) {
		for (MojangProfile profile : profiles) {
			Player player = profile.getOfflinePlayer().getPlayer();
			player.getOpenInventory().getTopInventory().setContents(ListUtil.toArray(items, ItemStack.class));
		}
	}

}