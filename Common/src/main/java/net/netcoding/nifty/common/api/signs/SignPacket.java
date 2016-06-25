package net.netcoding.nifty.common.api.signs;

import com.google.gson.Gson;

final class SignPacket {

	private static final transient Gson GSON = new Gson();
	//private final transient PacketContainer updateSignPacket;

	/*SignPacket(PacketContainer updateSignPacket) {
		this.updateSignPacket = updateSignPacket;
	}

	public Vector getPosition() {
		int x;
		int y;
		int z;

		if (MinecraftProtocol.isPre1_8()) {
			x = this.getPacket().getIntegers().read(0);
			y = this.getPacket().getIntegers().read(1);
			z = this.getPacket().getIntegers().read(1);
		} else {
			BlockPosition position = this.getPacket().getBlockPositionModifier().read(0);
			x = position.getX();
			y = position.getY();
			z = position.getZ();
		}

		return new Vector(x, y, z);
	}

	private String getLine(int index) {
		if (SignMonitor.IS_PRE_1_9_3) {
			String json = this.getPacket().getChatComponentArrays().read(0)[index].getJson();
			if (StringUtil.isEmpty(json) || "\"\"".equals(json)) return "";
			Map<?, ?> jsonMap = GSON.fromJson(json, Map.class);
			return (String) ((List<?>) jsonMap.get("extra")).get(0);
		} else {
			NbtCompound compound = (NbtCompound)this.getPacket().getNbtModifier().read(0);
			return compound.getString(StringUtil.format("Text{0}", index + 1));
		}
	}

	public String[] getLines() {
		if (MinecraftProtocol.isPre1_8())
			return this.getPacket().getStringArrays().read(0);
		else {
			String[] lines = new String[4];

			for (int i = 0; i < lines.length; i++)
				lines[i] = this.getLine(i);

			return lines;
		}
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

		if (MinecraftProtocol.isPre1_8())
			this.getPacket().getStringArrays().write(0, lines);
		else if (SignMonitor.IS_PRE_1_9_3) {
			WrappedChatComponent[] chat = this.getPacket().getChatComponentArrays().read(0);
			if (chat.length == 0) chat = new WrappedChatComponent[4];

			for (int i = 0; i < lines.length; i++)
				chat[i] = WrappedChatComponent.fromText(lines[i]);

			this.getPacket().getChatComponentArrays().write(0, chat);
		} else {
			NbtCompound compound = (NbtCompound)this.getPacket().getNbtModifier().read(0);
			Vector position = this.getPosition();
			compound.put("id", "Sign");
			compound.put("x", position.getX());
			compound.put("y", position.getY());
			compound.put("z", position.getZ());

			for (int i = 0; i < lines.length; i++) {
				List<JsonMessage> messages = JsonMessage.fromLegacyText(lines[i]);
				String jsonLine = "";

				for (JsonMessage message : messages)
					jsonLine += message.toJSONString();

				compound.put(StringUtil.format("Text{0}", i + 1), jsonLine);
			}

			this.getPacket().getNbtModifier().write(0, compound);
			//new Reflection("PacketPlayOutTileEntityData", MinecraftPackage.MINECRAFT_SERVER).setValue(NbtFactory.NBT_TAG_COMPOUND.getClazz(), this.getPacket().getHandle(), compound.getHandle());
			//Reflection wrappedCompound = new Reflection("WrappedCompound", "nbt", "com.comphenix.protocol.wrappers");
			//this.getPacket().getNbtModifier().write(0, (com.comphenix.protocol.wrappers.nbt.NbtCompound)wrappedCompound.newInstance(compound.getHandle()));
		}
	}

	void setPosition(Vector position) {
		if (MinecraftProtocol.isPre1_8()) {
			this.getPacket().getIntegers().write(0, position.getBlockX());
			this.getPacket().getIntegers().write(1, position.getBlockY());
			this.getPacket().getIntegers().write(2, position.getBlockZ());
		} else {
			this.getPacket().getBlockPositionModifier().write(0, new BlockPosition(position.getBlockX(), position.getBlockY(), position.getBlockZ()));

			if (SignMonitor.IS_POST_1_9_3) {
				NbtCompound compound = (NbtCompound)this.getPacket().getNbtModifier().read(0);
				compound.put("x", position.getBlockX());
				compound.put("y", position.getBlockY());
				compound.put("z", position.getBlockZ());
				this.getPacket().getNbtModifier().write(0, compound);
			}
		}
	}*/

}