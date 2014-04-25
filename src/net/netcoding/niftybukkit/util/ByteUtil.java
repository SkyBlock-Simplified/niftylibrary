package net.netcoding.niftybukkit.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.minecraft.util.com.google.common.io.ByteArrayDataOutput;
import net.minecraft.util.com.google.common.io.ByteStreams;

public class ByteUtil {

	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static byte[] toByteArray(Object... data) {
		return toByteArray(Arrays.asList(data));
	}

	public static byte[] toByteArray(List<Object> data) {
		ByteArrayDataOutput output = ByteStreams.newDataOutput();

		for (Object obj : data) {
			if (obj instanceof byte[])
				output.write((byte[])obj);
			else if (obj instanceof Byte)
				output.writeByte((int)obj);
			else if (obj instanceof Boolean)
				output.writeBoolean((boolean)obj);
			else if (obj instanceof Character)
				output.writeChar((char)obj);
			else if (obj instanceof Short)
				output.writeShort((short)obj);
			else if (obj instanceof Integer)
				output.writeInt((int)obj);
			else if (obj instanceof Long)
				output.writeLong((long)obj);
			else if (obj instanceof Double)
				output.writeDouble((double)obj);
			else if (obj instanceof Float)
				output.writeFloat((float)obj);
			else if (obj instanceof String)
				output.writeUTF((String)obj);
			else if (obj instanceof UUID)
				output.writeUTF(((UUID)obj).toString());
		}

		return output.toByteArray();
	}

	public static String toHexString(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];

		for (int i = 0; i < bytes.length; i++) {
			int v = bytes[i] & 0xFF;
			hexChars[i * 2] = hexArray[v >>> 4];
			hexChars[i * 2 + 1] = hexArray[v & 0x0F];
		}

		return new String(hexChars);
	}

}