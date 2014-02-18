package net.netcoding.niftybukkit.inventory;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import net.netcoding.niftybukkit.minecraft.BukkitListener;

public class FakeInventory extends BukkitListener {

	public FakeInventory(JavaPlugin plugin) {
		super(plugin);
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		
	}

	@EventHandler
	public void onInventoryInteract(InventoryInteractEvent event) {
		
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		
	}

	public void setInventoryItem(Material material) {
		this.setInventoryItem(material, false);
	}

	public void setInventoryItem(Material material, boolean dropDispose) {
		
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getItem(int id) {
		ItemStack item = new ItemStack(id);
		//Apply ItemMeta before doing this
		net.minecraft.server.v1_7_R1.ItemStack nms = org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.server.v1_7_R1.NBTTagCompound tag = (nms.tag != null ? nms.tag :
			(nms.tag = new net.minecraft.server.v1_7_R1.NBTTagCompound()));
		tag.setBoolean("NoDropMe", true);

		ItemStack newItem = org.bukkit.craftbukkit.v1_7_R1.inventory.CraftItemStack.asCraftMirror(nms);
		return newItem;
	}

}