package net.netcoding.niftybukkit.util;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Array and List Checking/Converting
 */
public class ListUtil {

	/**
	 * Gets if the {@code value} is empty or null.
	 * 
	 * @param value to check
	 * @return true if empty or null, otherwise false
	 */
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * Gets if the {@code value} is empty or null.
	 * 
	 * @param value to check
	 * @return true if empty or null, otherwise false
	 */
	public static <T> boolean isEmpty(Collection<T> collection) {
		return collection == null || collection.size() == 0;
	}

	/**
	 * Gets if the {@code value} is not empty.
	 * 
	 * @param value to check
	 * @return true if not empty or null, otherwise false
	 */
	public static <T> boolean notEmpty(T[] array) {
		return !isEmpty(array);
	}

	/**
	 * Gets if the {@code value} is not empty.
	 * 
	 * @param value to check
	 * @return true if not empty or null, otherwise false
	 */
	public static <T> boolean notEmpty(Collection<T> collection) {
		return !isEmpty(collection);
	}

	/**
	 * Gets a list converter to array.
	 * 
	 * @param list to convert to array
	 * @param type of {@code list} elements
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(Collection<? extends T> collection, Class<T> type) {
		try {
			return collection.toArray((T[])Array.newInstance(type, collection.size()));
		} catch (NullPointerException npe) {
			return (T[])new Object[] { };
		}
	}

}