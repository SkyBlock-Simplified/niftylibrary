package net.netcoding.niftysponge;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(id = "${project.artifactId}", name = "${project.name}", version = "${project.version}")
public class NiftySponge {

	// ID: https://docs.spongepowered.org/master/en/plugin/plugin-identifier.html
	// Annotation: https://docs.spongepowered.org/master/en/plugin/plugin-class.html
	// Events: https://docs.spongepowered.org/master/en/plugin/event/index.html
	// Commands: https://docs.spongepowered.org/master/en/plugin/commands/childcommands.html
	// Bukkit -> Sponge: https://docs.google.com/spreadsheets/d/1phlRmbPHCVhRIaZBi9xMjUuiDQkW-j8rSEV_tQnD47E/edit#gid=0

	public NiftySponge() {
		ItemStack stack = Sponge.getRegistry().createBuilder(ItemStack.Builder.class).itemType(ItemTypes.BOOK).build();
		stack.offer(Keys.DISPLAY_NAME, Text.of("Wtf"));
		stack.setQuantity(5);
	}

}