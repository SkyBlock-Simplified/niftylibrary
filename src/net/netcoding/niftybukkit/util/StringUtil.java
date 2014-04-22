package net.netcoding.niftybukkit.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;

public class StringUtil {

	private static final transient Map<String, MessageFormat> messageCache = new HashMap<>();

	public static String format(String format, Object... objects) {
		MessageFormat messageFormat = messageCache.get(format);

		if (messageFormat == null) {
			try {
				messageFormat = new MessageFormat(format);
			} catch (IllegalArgumentException e) {
				format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
				messageFormat = new MessageFormat(format);
			}

			messageCache.put(format, messageFormat);
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

	public static String implode(String[] pieces, int startIndex) {
		return implode("", toList(pieces), startIndex);
	}

	public static String implode(List<String> pieces, int startIndex) {
		return implode("", pieces, startIndex);
	}

	public static String implode(String glue, String[] pieces, int startIndex) {
		return implode(glue, toList(pieces), startIndex);
	}

	public static String implode(String glue, List<String> pieces, int startIndex) {
		if (isEmpty(glue)) glue = "";
		if (ListUtil.isEmpty(pieces)) throw new IllegalArgumentException("Pieces cannot be empty!");
		if (startIndex > pieces.size()) throw new IndexOutOfBoundsException(String.format("Cannot access index %d out of %d total pieces!", startIndex, pieces.size()));
		List<String> newPieces = new ArrayList<>();

		for (int i = startIndex; i < pieces.size(); i++)
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