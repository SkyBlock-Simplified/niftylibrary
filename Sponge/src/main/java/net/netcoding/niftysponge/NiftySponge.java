package net.netcoding.niftysponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

public class NiftySponge {

	private boolean isOnline;

	public NiftySponge() {
		ItemStack stack = Sponge.getRegistry().createBuilder(ItemStack.Builder.class).itemType(ItemTypes.BOOK).build();
		stack.offer(Keys.DISPLAY_NAME, Text.of("Wtf"));
		stack.setQuantity(5);
	}

}