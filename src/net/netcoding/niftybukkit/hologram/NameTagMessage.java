package net.netcoding.niftybukkit.hologram;

import java.awt.image.BufferedImage;

import net.netcoding.niftybukkit.utilities.ProtocolLibNotFoundException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.base.Preconditions;

public class NameTagMessage extends ImageMessage {

	private NameTagSpawner spawner;
	private Location location;
	private double lineSpacing = 0.25d;

	public NameTagMessage(BufferedImage image, int height, char imgChar) throws ProtocolLibNotFoundException {
		super(image, height, imgChar);
		this.spawner = new NameTagSpawner(height);
	}

	public NameTagMessage(ChatColor[][] chatColors, char imgChar) throws ProtocolLibNotFoundException {
		super(chatColors, imgChar);
		this.location = Preconditions.checkNotNull(location, "location cannot be NULL");
		this.spawner = new NameTagSpawner(chatColors.length);
	}

	public NameTagMessage(String... imgLines) throws ProtocolLibNotFoundException {
		super(imgLines);
		this.spawner = new NameTagSpawner(imgLines.length);
	}

	@Override
	public NameTagMessage appendCenteredText(String... text) {
		super.appendCenteredText(text);
		return this;
	}

	@Override
	public NameTagMessage appendText(String... text) {
		super.appendText(text);
		return this;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Location getLocation() {
		return location;
	}

	/**
	 * Retrieve the default amount of meters in the y-axis between each name tag.
	 * @return The line spacing.
	 */
	public double getLineSpacing() {
		return lineSpacing;
	}

	/**
	 * Set the default amount of meters in the y-axis between each name tag.
	 * @param lineSpacing - the name spacing.
	 */
	public void setLineSpacing(double lineSpacing) {
		this.lineSpacing = lineSpacing;
	}

	@Override
	public void sendToPlayer(Player player) {
		sendToPlayer(player, location != null ? location : player.getLocation());
	}

	/**
	 * Send a floating image message to the given player at the specified starting location.
	 * @param player - the player.
	 * @param location - the starting location.
	 */
	public void sendToPlayer(Player player, Location location) {
		for (int i = 0; i < lines.length; i++)
			spawner.setNameTag(i, player, location, -i * lineSpacing, lines[i]);
	}

}