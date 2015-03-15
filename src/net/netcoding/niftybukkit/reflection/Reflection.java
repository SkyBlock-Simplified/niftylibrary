package net.netcoding.niftybukkit.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import net.netcoding.niftybukkit.util.StringUtil;

public class Reflection {

	private static final transient ConcurrentHashMap<Class<?>, Class<?>> CORRESPONDING_TYPES = new ConcurrentHashMap<>();
	private static final transient ConcurrentHashMap<Class<?>[], Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<>();
	private static final transient ConcurrentHashMap<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();
	private final String className;
	private final String subPackage;
	private final String packagePath;

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
		this(className, subPackage, minecraftPackage.toString());
	}

	public Reflection(String className, String packagePath) {
		this(className, "", packagePath);
	}

	public Reflection(String className, String subPackage, String packagePath) {
		this.className = className;
		this.subPackage = StringUtil.stripNull(subPackage).replaceAll("\\.$", "").replaceAll("^\\.", "");
		this.packagePath = packagePath;
	}

	public String getClassName() {
		return this.className;
	}

	public Class<?> getClazz() throws Exception {
		if (!CLASS_CACHE.containsKey(this.getClassPath())) CLASS_CACHE.put(this.getClassPath(), Class.forName(this.getClassPath()));
		return CLASS_CACHE.get(this.getClassPath());
	}

	public Constructor<?> getConstructor(Class<?>... paramTypes) throws Exception {
		Class<?>[] t = toPrimitiveTypeArray(paramTypes);

		if (CONSTRUCTOR_CACHE.get(t) != null)
			return CONSTRUCTOR_CACHE.get(t);

		for (Constructor<?> c : this.getClazz().getConstructors()) {
			Class<?>[] types = toPrimitiveTypeArray(c.getParameterTypes());

			if (equalsTypeArray(types, t)) {
				CONSTRUCTOR_CACHE.put(types, c);
				return c;
			}
		}

		System.out.println(StringUtil.format("The constructor {0} was not found!", Arrays.asList(t)));
		return CONSTRUCTOR_CACHE.put(t, null);
	}

	public String getClassPath() {
		return this.getPackagePath() + (StringUtil.notEmpty(this.subPackage) ? "." + this.subPackage : "") + "." + this.getClassName();
	}

	public Method getMethod(String name, Class<?>... paramTypes) throws Exception {
		Class<?>[] t = toPrimitiveTypeArray(paramTypes);

		for (Method m : this.getClazz().getMethods()) {
			Class<?>[] types = toPrimitiveTypeArray(m.getParameterTypes());

			if (m.getName().equals(name) && equalsTypeArray(types, t))
				return m;
		}

		System.out.println(StringUtil.format("The method {0} was not found with parameters {1}!", name, Arrays.asList(t)));
		return null;
	}

	public String getPackagePath() {
		return this.packagePath;
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

	public Field getField(String name) throws Exception {
		return this.getClazz().getDeclaredField(name);
	}

	public Object getValue(String name, Object obj) throws Exception {
		Field f = this.getField(name);
		f.setAccessible(true);
		return f.get(obj);
	}

	public void setValue(Object obj, FieldEntry entry) throws Exception {
		Field f = this.getField(entry.getKey());
		f.setAccessible(true);
		f.set(obj, entry.getValue());
	}

	public void setValues(Object obj, FieldEntry... entrys) throws Exception {
		for (FieldEntry entry : entrys)
			this.setValue(obj, entry);
	}

	private static Class<?> getPrimitiveType(Class<?> clazz) {
		return CORRESPONDING_TYPES.containsKey(clazz) ? CORRESPONDING_TYPES.get(clazz) : clazz;
	}

	private static Class<?>[] toPrimitiveTypeArray(Object[] objects) {
		Class<?>[] types = new Class<?>[objects != null ? objects.length : 0];
		for (int i = 0; i < types.length; i++) types[i] = getPrimitiveType(objects[i].getClass());
		return types;
	}

	private static Class<?>[] toPrimitiveTypeArray(Class<?>[] classes) {
		Class<?>[] types = new Class<?>[classes != null ? classes.length : 0];
		for (int i = 0; i < types.length; i++) types[i] = getPrimitiveType(classes[i]);
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