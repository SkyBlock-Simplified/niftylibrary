package net.netcoding.nifty.sponge;

import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import net.netcoding.nifty.core.api.plugin.annotations.Dependency;
import net.netcoding.nifty.core.api.plugin.annotations.Plugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

@org.spongepowered.api.plugin.Plugin(id = "niftybukkit", name = "NiftyBukkit", version = "${project.version}", dependencies = {
		@org.spongepowered.api.plugin.Dependency(id = "ProtocolLib", optional = true),
		@org.spongepowered.api.plugin.Dependency(id = "Vault", optional = true)
})
@Plugin(id = "nifty${project.artifactId}", name = "Nifty${project.name}", version = "${project.version}", dependencies = {
		@Dependency(id = "ProtocolLib", optional = true),
		@Dependency(id = "Vault", optional = true)
})
public final class NiftySponge extends MinecraftPlugin {

	// ID: https://docs.spongepowered.org/master/en/plugin/plugin-identifier.html
	// Annotation: https://docs.spongepowered.org/master/en/plugin/plugin-class.html
	// Events: https://docs.spongepowered.org/master/en/plugin/event/index.html
	// Commands: https://docs.spongepowered.org/master/en/plugin/commands/childcommands.html
	// Bukkit -> Sponge: https://docs.google.com/spreadsheets/d/1phlRmbPHCVhRIaZBi9xMjUuiDQkW-j8rSEV_tQnD47E/edit#gid=0
	// Messages: https://forums.spongepowered.org/t/send-a-message-to-bungeecord/7119
	// Dependency Sorting: http://yongouyang.blogspot.com/2013/03/sorting-objects-with-one-way-dependency.html
	// NbtTranslator: https://github.com/SpongePowered/SpongeCommon/blob/master/src/main/java/org/spongepowered/common/data/persistence/NbtTranslator.java
	// Nbt?: https://github.com/SpongePowered/SpongeCommon/blob/master/src/main/java/org/spongepowered/common/mixin/core/

	@Override
	public void onEnable() {
		ItemStack stack = Sponge.getRegistry().createBuilder(ItemStack.Builder.class).itemType(ItemTypes.BOOK).build();
		ItemStack stack2 = ItemStack.of(ItemTypes.BOOK, 1);
		stack.offer(Keys.DISPLAY_NAME, Text.of("Wtf"));
		stack.setQuantity(5);
	}

	public void WIP() {
		// Sponge.getGame().getChannelRegistrar()
		// org.spongepowered.api.event.server.ClientPingServerEvent
		// net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent
		Player p = null;
		Location<World> loc = p.getLocation();
		p.getLocation().getBlockX();
		World world = Sponge.getServer().getWorlds().iterator().next();
		world.sendMessage(Text.of("HELLO EVERYBODY!"));
		ItemStack is = ItemStack.of(ItemTypes.ITEM_FRAME, 0);
		net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent l;
	}

	@Listener
	public void onLoad(GamePreInitializationEvent event) {
		// Provide Services
	}

	@Listener
	public void onEnable(GameStartingServerEvent event) {
		//Nifty.getServer().getPluginManager().call(new GameStartingEvent());
	}

	@Listener
	public void onDisable(GameStoppingServerEvent event) {
		//Nifty.getServer().getPluginManager().call(new GameStoppingEvent());
	}

}