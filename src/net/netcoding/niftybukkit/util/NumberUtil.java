package net.netcoding.niftybukkit.util;

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

}