package net.netcoding.nifty.sponge;

import net.netcoding.nifty.common.api.plugin.MinecraftPlugin;
import org.spongepowered.api.Sponge;
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

import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

@org.spongepowered.api.plugin.Plugin(id = "${id}", name = "${name}", version = "${version}", dependencies = {
		@org.spongepowered.api.plugin.Dependency(id = "Vault", optional = true)
})
public final class NiftySponge extends MinecraftPlugin {

	// ID: https://docs.spongepowered.org/master/en/plugin/plugin-identifier.html
	// Annotation: https://docs.spongepowered.org/master/en/plugin/plugin-class.html
	// Events: https://docs.spongepowered.org/master/en/plugin/event/index.html
	// Commands: https://docs.spongepowered.org/master/en/plugin/commands/childcommands.html
	// Bukkit -> Sponge: https://docs.google.com/spreadsheets/d/1phlRmbPHCVhRIaZBi9xMjUuiDQkW-j8rSEV_tQnD47E/edit#gid=0
	// Messages: https://forums.spongepowered.org/t/send-a-message-to-bungeecord/7119
	// NbtTranslator: https://github.com/SpongePowered/SpongeCommon/blob/master/src/main/java/org/spongepowered/common/data/persistence/NbtTranslator.java
	// Nbt?: https://github.com/SpongePowered/SpongeCommon/blob/master/src/main/java/org/spongepowered/common/mixin/core/

/*
Processing Class: net.minecraft.item.ItemStack : zx.class
Processing Field: field_111284_a: java.text.DecimalFormat
Processing Field: field_77994_a: int
Processing Field: field_77992_b: int
Processing Field: field_151002_e: net.minecraft.item.Item
Processing Field: field_77990_d: net.minecraft.nbt.NBTTagCompound : dn.class
Processing Field: field_77991_e: int
Processing Field: field_82843_f: net.minecraft.entity.item.EntityItemFrame
Processing Field: field_179552_h: net.minecraft.block.Block
Processing Field: field_179553_i: boolean
Processing Field: field_179550_j: net.minecraft.block.Block
Processing Field: field_179551_k: boolean
Processing Field: delegate: net.minecraftforge.fml.common.registry.RegistryDelegate
Processing Field: capabilities: net.minecraftforge.common.capabilities.CapabilityDispatcher
Processing Field: capNBT: net.minecraft.nbt.NBTTagCompound : can ignore??
Processing Field: manipulators: java.util.List
Processing Field: observers: java.util.List
*/

	@Override
	public void onEnable() {
		/*ItemStack book = Sponge.getRegistry().createBuilder(ItemStack.Builder.class).itemType(ItemTypes.BOOK).build();
		book.offer(Keys.DISPLAY_NAME, Text.of("Wtf"));
		book.setQuantity(5);*/

		ItemStack skull = ItemStack.of(ItemTypes.SKULL, 1);
		this.check(skull);
	}

	public void check(Object obj) {
		this.recurse(obj.getClass());
	}

	public void recurse(Class<?> clazz) {
		System.out.println("Processing Class: " + clazz.getName() + ":" + this.getClazzLocation(clazz).toString());

		for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
			System.out.println("Processing Field: " + field.getName() + ": " + field.getType().getName());
		}

		if (clazz.getSuperclass() != null)
			this.recurse(clazz.getSuperclass());
	}

	public final URL getClazzLocation(Class<?> clazz) {
		ProtectionDomain domain = clazz.getProtectionDomain();

		if (domain != null) {
			CodeSource source = domain.getCodeSource();

			if (source != null)
				return source.getLocation();
		}

		return null;
	}

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	public void WIP() {
		// Sponge.getGame().getChannelRegistrar()
		// org.spongepowered.api.event.server.ClientPingServerEvent
		// net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent
		Player p = Sponge.getServer().getPlayer("CraftedFury").get();
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