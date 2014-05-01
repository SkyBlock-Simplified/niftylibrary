package net.netcoding.niftybukkit.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import net.netcoding.niftybukkit.util.StringUtil;

public class Reflection {

	private static final transient Map<Class<?>, Class<?>> CORRESPONDING_TYPES = new HashMap<>();
	private static final transient Map<Class<?>[], Constructor<?>> CONSTRUCTOR_CACHE = new HashMap<>();
	private static final transient Map<String, Class<?>> CLASS_CACHE = new HashMap<>();
	private final String className;
	private final String subPackage;
	private final MinecraftPackage minecraftPackage;

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

	public Reflection(String className, MinecraftPackage minecraftPackage) {
		this(className, "", minecraftPackage);
	}

	public Reflection(String className, String subPackage, MinecraftPackage minecraftPackage) {
		this.className = className;
		subPackage = StringUtil.isEmpty(subPackage) ? "" : subPackage;
		this.subPackage = subPackage.replaceAll("\\.$", "").replaceAll("^\\.", "");
		this.minecraftPackage = minecraftPackage;
	}

	public String getClassName() {
		return this.className;
	}

	public Class<?> getClazz() throws Exception {
		return CLASS_CACHE.containsKey(this.getClassPath()) ? CLASS_CACHE.get(this.getClassPath()) : CLASS_CACHE.put(this.getClassPath(), Class.forName(this.getClassPath()));
	}

	public Constructor<?> getConstructor(Class<?>... paramTypes) throws Exception {
		Class<?>[] t = toPrimitiveTypeArray(paramTypes);

		if (CONSTRUCTOR_CACHE.get(t) != null)
			return CONSTRUCTOR_CACHE.get(t);
		else {
			for (Constructor<?> c : this.getClazz().getConstructors()) {
				Class<?>[] types = toPrimitiveTypeArray(c.getParameterTypes());
				if (equalsTypeArray(types, t)) return CONSTRUCTOR_CACHE.put(t, c);
			}
		}

		return CONSTRUCTOR_CACHE.put(t, null);
	}

	public String getClassPath() {
		return this.getMinecraftPackage() + (StringUtil.notEmpty(subPackage) ? "." + subPackage : "") + "." + this.getClassName();
	}

	public MinecraftPackage getMinecraftPackage() {
		return this.minecraftPackage;
	}

	public Method getMethod(String name, Class<?>... paramTypes) throws Exception {
		Class<?>[] t = toPrimitiveTypeArray(paramTypes);

		for (Method m : this.getClazz().getMethods()) {
			Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());
			if (m.getName().equals(name) && equalsTypeArray(types, t))
				return m;
		}

		return null;
	}

	public String getSubPackage() {
		return this.subPackage;
	}

	public Object invokeMethod(String name, Object obj, Object... args) throws Exception {
		return this.getMethod(name, toPrimitiveTypeArray(args)).invoke(obj, args);
	}

	public Object newInstance(Object... args) throws Exception {
		return this.getConstructor(toPrimitiveTypeArray(args)).newInstance(args);
	}

	// TODO
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
	// TODO

	private static Class<?> getPrimitiveType(Class<?> clazz) {
		return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES.get(clazz) : clazz;
	}

	private static Class<?>[] toPrimitiveTypeArray(Object[] objects) {
		int a = objects != null ? objects.length : 0;
		Class<?>[] types = new Class<?>[a];
		for (int i = 0; i < a; i++) types[i] = getPrimitiveType(objects[i].getClass());
		return types;
	}

	private static Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
		int a = classes != null ? classes.length : 0;
		Class<?>[] types = new Class<?>[a];
		for (int i = 0; i < a; i++) types[i] = getPrimitiveType(classes[i]);
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

}