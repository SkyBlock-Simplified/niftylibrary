package net.netcoding.niftybukkit.signs;

import java.util.List;
import java.util.Map;

import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.StringUtil;
import net.netcoding.niftybukkit.util.gson.Gson;

import org.bukkit.util.Vector;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

class SignPacket {

	private static final transient Gson GSON = new Gson();
	private final transient PacketContainer updateSignPacket;

	SignPacket(PacketContainer updateSignPacket) {
		this.updateSignPacket = updateSignPacket;
	}

	private StructureModifier<BlockPosition> getBlockModifier() {
		return this.updateSignPacket.getBlockPositionModifier();
	}

	public Vector getPosition() {
		BlockPosition position = this.getBlockModifier().read(0);
		return new Vector(position.getX(), position.getY(), position.getZ());
	}

	void setPosition(Vector position) {
		this.getBlockModifier().write(0, new BlockPosition(position.getBlockX(), position.getBlockY(), position.getBlockZ()));
	}

	private String getLine(int index) {
		String json = this.updateSignPacket.getChatComponentArrays().read(0)[index].getJson();
		if (StringUtil.isEmpty(json) || json.equals("\"\"")) return "";
		Map<?, ?> jsonMap = GSON.fromJson(json, Map.class);
		return (String)((List<?>)jsonMap.get("extra")).get(0);
	}

	public String[] getLines() {
		String[] lines = new String[4];

		for (int i = 0; i < lines.length; i++)
			lines[i] = this.getLine(i);

		return lines;
	}

	void setLines(String[] lines) {
		if (ListUtil.isEmpty(lines)) throw new IllegalArgumentException("The passed lines cannot be null!");
		if (lines.length < 1 || lines.length > 4) throw new IllegalArgumentException("You must provide between 1 and 4 lines!");
		WrappedChatComponent[] chat = this.updateSignPacket.getChatComponentArrays().read(0);
		if (chat.length == 0) chat = new WrappedChatComponent[4];

		for (int i = 0; i < 4; i++)
			chat[i] = WrappedChatComponent.fromText(StringUtil.notEmpty(lines[i]) ? lines[i] : "");

		this.updateSignPacket.getChatComponentArrays().write(0, chat);
	}

	PacketContainer getPacket() {
		return updateSignPacket;
	}

}