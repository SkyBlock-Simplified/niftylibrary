package net.netcoding.niftybukkit.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class FakeInventoryInstance extends FakeInventoryFrame {

	private final transient FakeInventory inventory;
	private final transient String playerName;

	FakeInventoryInstance(JavaPlugin plugin, FakeInventory inventory, String playerName) {
		this(plugin, inventory, playerName, false);
	}

	FakeInventoryInstance(JavaPlugin plugin, FakeInventory inventory, String playerName, boolean autoCancel) {
		super(plugin, autoCancel);
		this.inventory = inventory;
		this.playerName = playerName;
	}

	public void close() {
		this.inventory.close(this.getPlayerName());
	}

	public Player getPlayer() {
		return matchPlayer(playerName);
	}

	public String getPlayerName() {
		return this.playerName;
	}

	public boolean isOpen() {
		return this.inventory.isOpen(this.getPlayerName());
	}

	public void open() {
		this.inventory.open(this.getPlayerName(), (ItemStack[])this.getItems().toArray());
	}

}