package pl.zientarski;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TypeDescription {

    private static final Predicate<Method> hasGetterName = method -> (method.getName().length() > 3 && method.getName().startsWith("get")) || (method.getName().length() > 2 && method.getName().startsWith("is"));
    private static final Predicate<Method> hasSetterName = method -> (method.getName().length() > 3 && method.getName().startsWith("set"));
    private static final Predicate<Method> hasZeroParameters = method -> method.getParameterCount() == 0;
    private static final Predicate<Method> hasOneParameter = method -> method.getParameterCount() == 1;
    private static final Predicate<Method> fromObject = method -> method.getDeclaringClass().equals(Object.class);

    private static final Predicate<Method> isSetter = method -> hasSetterName.and(hasOneParameter).and(fromObject.negate()).test(method);
    private static final Predicate<Method> isGetter = method -> hasGetterName.and(hasZeroParameters).and(fromObject.negate()).test(method);
    private static final Predicate<Field> isThisReference = field -> field.getName().equals("this$0");

    private final Type type;
    private final MapperContext mapperContext;

    public TypeDescription(final Type type, final MapperContext mapperContext) {
        this.type = type;
        this.mapperContext = mapperContext;

    }

    public Type getType() {
        return type;
    }

    public Set<PropertyDescription> getProperties() {
        return getProperties((Class<?>) type);
    }

    public Set<PropertyDescription> getProperties(final Class<?> clazz) {
        final Map<String, Four> fours = new DefaultHashMap();

        getAllFields(clazz).stream()
                .filter(isThisReference.negate())
                .forEach(field -> {
                    Four four = fours.get(hash(field));
                    four.field = field;
                    four.name = field.getName();
                });

        Arrays.asList(clazz.getMethods()).stream()
                .filter(isSetter)
                .forEach(setter -> {
                    Four four = fours.get(hashSetter(setter));
                    four.setter = setter;
                    four.name = toFieldName(setter.getName());
                });

        Arrays.asList(clazz.getMethods()).stream()
                .filter(isGetter)
                .forEach(getter -> {
                    Four four = fours.get(hashGetter(getter));
                    four.getter = getter;
                    four.name = toFieldName(getter.getName());
                });

        return fours.values().stream().map(four -> new PropertyDescription(four.name, four.field, four.setter, four.getter, mapperContext)).collect(Collectors.toSet());

    }

    private static Object hashGetter(final Method getter) {
        return String.format("%s %s", toFieldName(getter.getName()), getter.getGenericReturnType().getTypeName());
    }

    private static Object hashSetter(final Method setter) {
        return String.format("%s %s", toFieldName(setter.getName()), setter.getGenericParameterTypes()[0].getTypeName());
    }

    private static String hash(final Field field) {
        return String.format("%s %s", field.getName(), field.getGenericType().getTypeName());
    }

    private static String toFieldName(final String methodName) {
        if (methodName.startsWith("is")) {
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
        } else {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        }
    }

    private static Set<Field> getAllFields(final Class<?> clazz) {
        if (clazz.equals(Object.class)) {
            return new HashSet<>();
        }

        final Set<Field> fields = new HashSet<>(Arrays.asList(clazz.getDeclaredFields()));
        if (clazz.isInterface()) {
            for (final Class<?> superInterface : clazz.getInterfaces()) {
                fields.addAll(getAllFields(superInterface));
            }
        } else {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        return fields;
    }

    private static class Four {
        String name;
        Field field;
        Method getter;
        Method setter;
    }

    private static class DefaultHashMap extends HashMap<String, Four> {
        private static final long serialVersionUID = 3379715062215139110L;

        @Override
        public Four get(final Object key) {
            if (containsKey(key)) {
                return super.get(key);
            }
            final Four triple = new Four();
            put((String) key, triple);
            return triple;
        }
    }
}
