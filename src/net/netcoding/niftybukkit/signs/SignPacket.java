package net.netcoding.niftybukkit.signs;

import net.netcoding.niftybukkit.util.ListUtil;
import net.netcoding.niftybukkit.util.StringUtil;

import com.comphenix.protocol.events.PacketContainer;

class SignPacket {

	private final transient PacketContainer updateSignPacket;

	SignPacket(PacketContainer updateSignPacket) {
		this.updateSignPacket = updateSignPacket;
	}

	private Integer getCoord(int index) {
		return this.updateSignPacket.getIntegers().read(index);
	}

	public int getX() {
		return this.getCoord(0);
	}

	public short getY() {
		return this.getCoord(1).shortValue();
	}

	public int getZ() {
		return this.getCoord(2);
	}

	private void setCoord(int index, int value) {
		this.updateSignPacket.getIntegers().write(index, value);
	}

	void setX(int value) {
		this.setCoord(0, value);
	}

	void setY(short value) {
		this.setCoord(1, value);
	}

	void setZ(int value) {
		this.setCoord(2, value);
	}

	public String[] getLines() {
		return updateSignPacket.getStringArrays().read(0);
	}

	void setLines(String[] lines) {
		if (ListUtil.isEmpty(lines)) throw new IllegalArgumentException("The lines array cannot be null!.");
		if (lines.length < 1 || lines.length > 4) throw new IllegalArgumentException("You must provide 1-4 lines.");
		String[] push = new String[4];
		for (int i = 0; i < 4; i++) push[i] = (StringUtil.notEmpty(lines[i]) ? lines[i] : "");
		this.updateSignPacket.getStringArrays().write(0, lines);
	}

	public PacketContainer getPacket() {
		return updateSignPacket;
	}

}