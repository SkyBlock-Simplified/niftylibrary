package net.netcoding.niftybukkit.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtil {

	private static final transient Map<String, MessageFormat> cache = new HashMap<String, MessageFormat>();

	public static String join(String format, String... objects) {
		MessageFormat messageFormat = cache.get(format);

		if (messageFormat == null) {
			try {
				messageFormat = new MessageFormat(format);
			} catch (IllegalArgumentException e) {
				format = format.replaceAll("\\{(\\D*?)\\}", "\\[$1\\]");
				messageFormat = new MessageFormat(format);
			}

			cache.put(format, messageFormat);
		}

		return messageFormat.format(objects);
	}

	public static String implode(String[] pieces) {
		return implode(toList(pieces), 0);
	}

	public static String implode(List<String> pieces) {
		return implode("", pieces, 0);
	}

	public static String implode(String glue, String[] pieces) {
		return implode(glue, toList(pieces), 0);
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

	public static String implode(String glue, List<String> pieces, int startIndex) throws NullPointerException, IllegalArgumentException, IndexOutOfBoundsException {
		if (pieces == null) throw new NullPointerException();
		int length = pieces.size();
		if (length == 0) throw new IllegalArgumentException();
		if (startIndex > length) throw new IndexOutOfBoundsException();

		StringBuilder builder = new StringBuilder();
		builder.append(pieces.get(startIndex));
		int _continue = startIndex + 1;

		if (length >= _continue) {
			for (int i = _continue; i < length; i++) {
				builder.append(glue);
				builder.append(pieces.get(i));
			}
		}

		return builder.toString();
	}

	public static List<String> toList(String... strArray) {
		return new ArrayList<String>(Arrays.asList(strArray));
	}

}