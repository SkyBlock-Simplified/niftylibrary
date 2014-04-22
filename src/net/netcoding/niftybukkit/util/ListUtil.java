package net.netcoding.niftybukkit.util;

import java.lang.reflect.Array;
import java.util.List;

public class ListUtil {

	public static boolean isEmpty(String[] value) {
		return value == null || value.length == 0;
	}

	public static boolean isEmpty(List<String> value) {
		return value == null || value.size() == 0;
	}

	public static boolean notEmpty(String[] value) {
		return !isEmpty(value);
	}

	public static boolean notEmpty(List<String> value) {
		return !isEmpty(value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(List<T> list, Class<T> type) {
		try {
			return list.toArray((T[])Array.newInstance(type, list.size()));
		} catch (NullPointerException npe) {
			return (T[])new Object[] { };
		}
	}

}