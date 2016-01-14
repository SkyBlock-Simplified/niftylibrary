package net.netcoding.niftybukkit.inventory;

import net.netcoding.niftybukkit.mojang.BukkitMojangProfile;
import net.netcoding.niftycore.util.ListUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class FakeInventoryInstance extends FakeInventoryFrame {

	private final transient FakeInventory inventory;
	private final transient BukkitMojangProfile profile;

	FakeInventoryInstance(JavaPlugin plugin, FakeInventory inventory, BukkitMojangProfile profile) {
		super(plugin);
		this.inventory = inventory;
		this.profile = profile;
	}

	public void close() {
		this.inventory.close(this.getProfile());
	}

	public BukkitMojangProfile getProfile() {
		return this.profile;
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
		this.update(this.inventory.getOpened().keySet());
	}

	public void update(BukkitMojangProfile profile) {
		this.update(Collections.singletonList(profile));
	}

	public void update(Collection<BukkitMojangProfile> profiles) {
		this.inventory.update(profiles, this);
	}

	public <T extends ItemStack> void update(T[] items) {
		this.update(this.inventory.getOpened().keySet(), Arrays.asList(items));
	}

	public <T extends ItemStack> void update(BukkitMojangProfile profile, T[] items) {
		this.update(Collections.singletonList(profile), Arrays.asList(items));
	}

	public <T extends ItemStack> void update(Collection<BukkitMojangProfile> profiles, T[] items) {
		this.update(profiles, Arrays.asList(items));
	}

	public <T extends ItemStack> void update(BukkitMojangProfile profile, Collection<? extends T> items) {
		this.update(Collections.singletonList(profile), items);
	}

	public <T extends ItemStack> void update(Collection<BukkitMojangProfile> profiles, Collection<? extends T> items) {
		for (BukkitMojangProfile profile : profiles) {
			Player player = profile.getOfflinePlayer().getPlayer();
			player.getOpenInventory().getTopInventory().setContents(ListUtil.toArray(items, ItemStack.class));
		}
	}

}