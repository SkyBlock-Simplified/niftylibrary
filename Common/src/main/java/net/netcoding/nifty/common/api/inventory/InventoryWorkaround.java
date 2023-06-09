package net.netcoding.nifty.common.api.inventory;

import net.netcoding.nifty.common.Nifty;
import net.netcoding.nifty.common.minecraft.inventory.Inventory;
import net.netcoding.nifty.common.minecraft.inventory.item.ItemStack;
import net.netcoding.nifty.common.minecraft.inventory.type.PlayerInventory;
import net.netcoding.nifty.common.minecraft.material.Material;
import net.netcoding.nifty.core.util.concurrent.Concurrent;
import net.netcoding.nifty.core.util.concurrent.ConcurrentMap;

import java.util.Map;

public class InventoryWorkaround {

	private static int nextPartial(Inventory inventory, ItemStack item, int maxAmount, int slotNumber) {
		if (item == null) return -1;
		ItemStack[] stacks = inventory.getContents();

		for (int i = slotNumber; i < stacks.length; i++) {
			if ((inventory instanceof PlayerInventory) && (i >= 36 && i <= 39))
				continue;

			if (stacks[i] != null && stacks[i].getAmount() < maxAmount && item.isSimilar(stacks[i]))
				return i;
		}

		return -1;
	}

	// Returns what it couldnt store
	// This will will abort if it couldn't store all items
	public static Map<Integer, ItemStack> addAllItems(Inventory inventory, ItemStack... items) {
		Inventory fakeInventory = Nifty.getServer().createInventory(null, inventory.getType());
		fakeInventory.setContents(inventory.getContents());
		Map<Integer, ItemStack> overFlow = addItems(fakeInventory, items);

		if (overFlow.isEmpty()) {
			addItems(inventory, items);
			return null;
		}

		return addItems(fakeInventory, items);
	}

	// Returns what it couldnt store
	public static Map<Integer, ItemStack> addItems(Inventory inventory, ItemStack... items) {
		return addOversizedItems(inventory, 0, items);
	}

	// Returns what it couldnt store
	// Set oversizedStack to below normal stack size to disable oversized stacks
	public static Map<Integer, ItemStack> addOversizedItems(Inventory inventory, int oversizedStacks, ItemStack... items) {
		ConcurrentMap<Integer, ItemStack> leftover = Concurrent.newMap();
		ConcurrentMap<Material, Integer> lastPartial = Concurrent.newMap();
		ItemStack[] combined = new ItemStack[items.length];

		for (ItemStack item : items) {
			if (item == null || item.getAmount() < 1) continue;

			for (int j = 0; j < combined.length; j++) {
				if (combined[j] == null) {
					combined[j] = item.clone();
					break;
				}

				if (combined[j].isSimilar(item)) {
					combined[j].setAmount(combined[j].getAmount() + item.getAmount());
					break;
				}
			}
		}


		for (int i = 0; i < combined.length; i++) {
			ItemStack item = combined[i];
			if (item == null || item.getType() == Material.AIR) continue;

			while (true) {
				int maxAmount = oversizedStacks > item.getType().getMaxStackSize() ? oversizedStacks : item.getType().getMaxStackSize();
				int nextPartial = nextPartial(inventory, item, maxAmount, (lastPartial.containsKey(item.getType()) ? lastPartial.get(item.getType()) : 0));
				lastPartial.put(item.getType(), (nextPartial == -1 ? 0 : nextPartial));

				if (nextPartial == -1) {
					int firstFree = inventory.firstEmpty(); // Find empty slot

					if (firstFree == -1) {
						leftover.put(i, item); // No free space
						break;
					}

					if (item.getAmount() > maxAmount) {
						// More than a single stack!
						ItemStack stack = item.clone();
						stack.setAmount(maxAmount);
						inventory.setItem(firstFree, stack);
						item.setAmount(item.getAmount() - maxAmount);
					} else {
						inventory.setItem(firstFree, item); // Just store it
						break;
					}
				} else {
					ItemStack partialItem = inventory.getItem(nextPartial);
					int amount = item.getAmount();
					int partialAmount = partialItem.getAmount();

					// Check if it all fits
					if (amount + partialAmount <= maxAmount) {
						partialItem.setAmount(amount + partialAmount);
						break;
					}

					// Partial fit
					partialItem.setAmount(maxAmount);
					item.setAmount(amount + partialAmount - maxAmount);
				}
			}
		}

		return leftover;
	}

}