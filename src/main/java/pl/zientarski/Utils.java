package pl.zientarski;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class Utils {
    private static final Map<Class<?>, Class<?>> primitivesToWrappers = new HashMap<Class<?>, Class<?>>();

    static {
        primitivesToWrappers.put(Boolean.class, boolean.class);
        primitivesToWrappers.put(Byte.class, byte.class);
        primitivesToWrappers.put(Character.class, char.class);
        primitivesToWrappers.put(Double.class, double.class);
        primitivesToWrappers.put(Float.class, float.class);
        primitivesToWrappers.put(Integer.class, int.class);
        primitivesToWrappers.put(Long.class, long.class);
        primitivesToWrappers.put(Short.class, short.class);
        primitivesToWrappers.put(Void.class, void.class);
    }

    public static boolean isPrimitiveTypeWrapper(final Class<?> clazz) {
        return primitivesToWrappers.containsKey(clazz);
    }

    public static Class<?> unwrap(final Class<?> clazz) {
        if (isPrimitiveTypeWrapper(clazz)) {
            return primitivesToWrappers.get(clazz);
        }
        return clazz;
    }

    public static boolean isPrimitiveType(final Class<?> clazz) {
        return clazz.isPrimitive();
    }

    public static boolean isDirectlyMappedToJsonSchemaType(final Class<?> clazz) {
        return clazz.equals(Boolean.class) || clazz.equals(boolean.class) || clazz.equals(String.class);
    }

    public static boolean isArrayType(final Class<?> clazz) {
        return clazz.isArray();
    }

    public static boolean isEnumType(final Class<?> clazz) {
        return clazz.isEnum();
    }

    public static boolean isParameterizedType(final Type type) {
        return type instanceof ParameterizedType;
    }

    public static boolean isWildcardType(final Type type) {
        return type instanceof WildcardType;
    }

    public static boolean isTypeVariable(final Type type) {
        return type instanceof TypeVariable;
    }

    public static boolean isDateTime(final Class<?> clazz) {
        return Calendar.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz);
    }

    public static Type getTypeArgument(final Class<?> clazz) {
        return clazz.getComponentType();
    }

    public static Type getTypeArgument(final ParameterizedType genericType) {
        return genericType.getActualTypeArguments()[0];
    }

    public static Type getTypeArgument(final WildcardType genericType) {
        return genericType.getUpperBounds()[0];
    }
}
