package net.netcoding.niftybukkit.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;

import com.google.common.base.Joiner;

public class StringUtil {

	private static final transient Map<String, MessageFormat> MESSAGE_CACHE = new HashMap<>();

	public static String format(String format, Object... objects) {
		format = RegexUtil.replace(format, RegexUtil.LOG_PATTERN, (ChatColor.RED + "$1" + ChatColor.GRAY));
		MessageFormat messageFormat = MESSAGE_CACHE.get(format);

		if (messageFormat == null) {
			try {
				messageFormat = new MessageFormat(format);
			} catch (IllegalArgumentException e) {
				format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
				messageFormat = new MessageFormat(format);
			}

			MESSAGE_CACHE.put(format, messageFormat);
		}

		return messageFormat.format(objects);
	}

	public static String implode(String[] pieces) {
		return implode(toList(pieces));
	}

	public static String implode(List<String> pieces) {
		return implode("", pieces);
	}

	public static String implode(String glue, String[] pieces) {
		return implode(glue, toList(pieces));
	}

	public static String implode(String glue, List<String> pieces) {
		return implode(glue, pieces, 0);
	}

	public static String implode(String[] pieces, int start) {
		return implode("", toList(pieces), start);
	}

	public static String implode(List<String> pieces, int start) {
		return implode("", pieces, start);
	}

	public static String implode(String glue, String[] pieces, int start) {
		return implode(glue, toList(pieces), start);
	}

	public static String implode(String glue, List<String> pieces, int start) {
		return implode(glue, pieces, start, -1);
	}

	public static String implode(String[] pieces, int start, int end) {
		return implode("", toList(pieces), start, end);
	}

	public static String implode(List<String> pieces, int start, int end) {
		return implode("", pieces, start, end);
	}

	public static String implode(String glue, String[] pieces, int start, int end) {
		return implode(glue, toList(pieces), start, end);
	}

	public static String implode(String glue, List<String> pieces, int start, int end) {
		if (isEmpty(glue)) glue = "";
		if (ListUtil.isEmpty(pieces)) throw new IllegalArgumentException("Pieces cannot be empty!");
		if (start < 0) start = 0;;
		if (start > pieces.size()) throw new IndexOutOfBoundsException(String.format("Cannot access index %d out of %d total pieces!", start, pieces.size()));
		if (end < 0) end = pieces.size();
		if (end > pieces.size()) throw new IndexOutOfBoundsException(String.format("Cannot access index %d out of %d total pieces!", end, pieces.size()));
		List<String> newPieces = new ArrayList<>();

		for (int i = start; i < end; i++)
			newPieces.add(pieces.get(i));

		return Joiner.on(glue).join(newPieces);
	}

	public static boolean isEmpty(String value) {
		return "".equals(value) || value == null;
	}

	public static boolean notEmpty(String value) {
		return !isEmpty(value);
	}

	public static List<String> toList(String... strArray) {
		return new ArrayList<String>(Arrays.asList(strArray));
	}

}