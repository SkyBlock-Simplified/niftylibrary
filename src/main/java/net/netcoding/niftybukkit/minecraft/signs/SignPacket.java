package net.netcoding.niftybukkit.minecraft.signs;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.google.gson.Gson;
import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftycore.util.ListUtil;
import net.netcoding.niftycore.util.StringUtil;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Map;

class SignPacket {

	private static final transient Gson GSON = new Gson();
	private final transient PacketContainer updateSignPacket;

	SignPacket(PacketContainer updateSignPacket) {
		this.updateSignPacket = updateSignPacket;
	}

	private StructureModifier<BlockPosition> getBlockModifier() {
		return this.getPacket().getBlockPositionModifier();
	}

	private Integer getCoord(int index) {
		return this.getPacket().getIntegers().read(index);
	}

	public Vector getPosition() {
		int x;
		int y;
		int z;

		if (MinecraftPackage.IS_PRE_1_8) {
			x = this.getCoord(0);
			y = this.getCoord(1);
			z = this.getCoord(1);
		} else {
			BlockPosition position = this.getBlockModifier().read(0);
			x = position.getX();
			y = position.getY();
			z = position.getZ();
		}

		return new Vector(x, y, z);
	}

	private String getLine(int index) {
		String json = this.getPacket().getChatComponentArrays().read(0)[index].getJson();
		if (StringUtil.isEmpty(json) || "\"\"".equals(json)) return "";
		Map<?, ?> jsonMap = GSON.fromJson(json, Map.class);
		return (String)((List<?>)jsonMap.get("extra")).get(0);
	}

	public String[] getLines() {
		if (MinecraftPackage.IS_PRE_1_8)
			return this.getPacket().getStringArrays().read(0);

		String[] lines = new String[4];

		for (int i = 0; i < lines.length; i++)
			lines[i] = this.getLine(i);

		return lines;
	}

	private void setCoord(int index, int value) {
		this.getPacket().getIntegers().write(index, value);
	}

	PacketContainer getPacket() {
		return this.updateSignPacket;
	}

	void setLines(String[] lines) {
		if (ListUtil.isEmpty(lines)) throw new IllegalArgumentException("The passed lines cannot be null!");
		if (lines.length < 1) throw new IllegalArgumentException("You must provide between at least 1 line!");
		if (lines.length > 4) lines = new String[] { lines[0], lines[1], lines[2], lines[3] };

		if (lines.length < 4) {
			String[] newLines = new String[4];

			for (int i = 0; i < lines.length; i++)
				newLines[i] = StringUtil.notEmpty(lines[i]) ? lines[i] : "";

			for (int i = lines.length; i < 4; i++)
				newLines[i] = "";

			lines = newLines;
		}

		if (MinecraftPackage.IS_PRE_1_8)
			this.getPacket().getStringArrays().write(0, lines);
		else {
			WrappedChatComponent[] chat = this.getPacket().getChatComponentArrays().read(0);
			if (chat.length == 0) chat = new WrappedChatComponent[4];

			for (int i = 0; i < lines.length; i++)
				chat[i] = WrappedChatComponent.fromText(lines[i]);

			this.getPacket().getChatComponentArrays().write(0, chat);
		}
	}

	void setPosition(Vector position) {
		if (MinecraftPackage.IS_PRE_1_8) {
			this.setCoord(0, position.getBlockX());
			this.setCoord(1, position.getBlockY());
			this.setCoord(2, position.getBlockZ());
		} else
			this.getBlockModifier().write(0, new BlockPosition(position.getBlockX(), position.getBlockY(), position.getBlockZ()));
	}

}