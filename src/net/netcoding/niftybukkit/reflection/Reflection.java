package net.netcoding.niftybukkit.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.netcoding.niftybukkit.reflection.MinecraftPackage;
import net.netcoding.niftybukkit.reflection.FieldEntry;

public class Reflection {

	private static final transient Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<>();

	static {
		CORRESPONDING_TYPES.put(Byte.class, byte.class);
		CORRESPONDING_TYPES.put(Short.class, short.class);
		CORRESPONDING_TYPES.put(Integer.class, int.class);
		CORRESPONDING_TYPES.put(Long.class, long.class);
		CORRESPONDING_TYPES.put(Character.class, char.class);
		CORRESPONDING_TYPES.put(Float.class, float.class);
		CORRESPONDING_TYPES.put(Double.class, double.class);
		CORRESPONDING_TYPES.put(Boolean.class, boolean.class);
	}

	private static Class<?> getPrimitiveType(Class<?> clazz) {
		return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES.get(clazz) : clazz;
	}

	private static Class<?>[] toPrimitiveTypeArray(Object[] objects) {
		int a = objects != null ? objects.length : 0;
		Class<?>[] types = new Class<?>[a];

		for (int i = 0; i < a; i++)
			types[i] = getPrimitiveType(objects[i].getClass());

		return types;
	}

	private static Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
		int a = classes != null ? classes.length : 0;
		Class<?>[] types = new Class<?>[a];

		for (int i = 0; i < a; i++)
			types[i] = getPrimitiveType(classes[i]);

		return types;
	}

	private static boolean equalsTypeArray(Class<?>[] a, Class<?>[] o) {
		if (a.length != o.length) return false;

		for (int i = 0; i < a.length; i++) {
			if (!a[i].equals(o[i]) && !a[i].isAssignableFrom(o[i]))
				return false;
		}

		return true;
	}

	public static Class<?> getClass(String name, MinecraftPackage pack, String subPackage) throws Exception {
		return Class.forName(pack + (subPackage != null && subPackage.length() > 0 ? "." + subPackage : "") + "." + name);
	}

	public static Class<?> getClass(String name, MinecraftPackage pack) throws Exception {
		return getClass(name, pack, null);
	}

	public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... paramTypes) {
		Class<?>[] t = toPrimitiveTypeArray(paramTypes);

		for (Constructor<?> c : clazz.getConstructors()) {
			Class<?>[] types = toPrimitiveTypeArray(c.getParameterTypes());
			if (equalsTypeArray(types, t))
				return c;
		}

		return null;
	}

	public static Object newInstance(Class<?> clazz, Object... args) throws Exception {
		return getConstructor(clazz, toPrimitiveTypeArray(args)).newInstance(args);
	}

	public static Object newInstance(String name, MinecraftPackage pack, String subPackage, Object... args) throws Exception {
		return newInstance(getClass(name, pack, subPackage), args);
	}

	public Object newInstance(String name, MinecraftPackage pack, Object... args) throws Exception {
		return newInstance(getClass(name, pack, null), args);
	}

	public Method getMethod(String name, Class<?> clazz, Class<?>... paramTypes) {
		Class<?>[] t = toPrimitiveTypeArray(paramTypes);

		for (Method m : clazz.getMethods()) {
			Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
			if (m.getName().equals(name) && equalsTypeArray(types, t))
				return m;
		}

		return null;
	}

	public Object invokeMethod(String name, Class<?> clazz, Object obj, Object... args) throws Exception {
		return getMethod(name, clazz, toPrimitiveTypeArray(args)).invoke(obj, args);
	}

	public Field getField(String name, Class<?> clazz) throws Exception {
		return clazz.getDeclaredField(name);
	}

	public Object getValue(String name, Object obj) throws Exception {
		Field f = getField(name, obj.getClass());
		f.setAccessible(true);
		return f.get(obj);
	}

	public void setValue(Object obj, FieldEntry entry) throws Exception {
		Field f = getField(entry.getKey(), obj.getClass());
		f.setAccessible(true);
		f.set(obj, entry.getValue());
	}

	public void setValues(Object obj, FieldEntry... entrys) throws Exception {
		for (FieldEntry f : entrys)
			setValue(obj, f);
	}

}