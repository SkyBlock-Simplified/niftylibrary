package net.netcoding.niftybukkit.minecraft.nbt;

import java.util.List;
import java.util.Map;

public enum NbtType {

	TAG_END(0, Void.class),
	TAG_BYTE(1, byte.class),
	TAG_SHORT(2, short.class),
	TAG_INT(3, int.class),
	TAG_LONG(4, long.class),
	TAG_FLOAT(5, float.class),
	TAG_DOUBLE(6, double.class),
	TAG_BYTE_ARRAY(7, byte[].class),
	TAG_STRING(8, String.class),
	TAG_LIST(9, List.class),
	TAG_COMPOUND(10, Map.class),
	TAG_INT_ARRAY(11, int[].class);

	private final byte id;

	NbtType(int id, Class<?> type) {
		this.id = (byte)id;
		NbtFactory.NBT_CLASS.put(this.id, type);
		NbtFactory.NBT_ENUM.put(this.id, this);
	}

	public final byte getId() {
		return this.id;
	}

	final String getFieldName() {
		if (this == TAG_COMPOUND)
			return "map";
		else if (this == TAG_LIST)
			return "list";
		else
			return "data";
	}

	public final Class<?> getFieldType() {
		return NbtFactory.NBT_CLASS.get(this.getId());
	}

}