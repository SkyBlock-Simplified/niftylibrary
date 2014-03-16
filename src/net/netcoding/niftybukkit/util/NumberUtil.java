package net.netcoding.niftybukkit.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class NumberUtil {

	/**
	 * Check if a number is a valid number
	 * @param value The value to check
	 * @return True if the value is can be casted to a number
	 */
	public static boolean isInt(String value) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition position = new ParsePosition(0);
		formatter.parse(value, position);
		return value.length() == position.getIndex();
	}

	/**
	 * Generates a random number
	 * @param minimum The lowest number allowed
	 * @return Returns a random integer between the specified boundaries
	 */
	public static int rand(int minimum) {
		return rand(minimum, Integer.MAX_VALUE);
	}

	/**
	 * Generates a random number
	 * @param minimum The lowest number allowed
	 * @param maximum The highest number allowed
	 * @return Returns a random integer between the specified boundaries
	 */
	public static int rand(int minimum, int maximum) {
		return minimum + (int)(Math.random() * ((maximum - minimum) + 1));
	}

	public static int readVarInt(DataInputStream in) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			int k = in.readByte();
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128) break;
		}
		return i;
	}

	public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				out.writeByte(paramInt);
				return;
			}

			out.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}

}