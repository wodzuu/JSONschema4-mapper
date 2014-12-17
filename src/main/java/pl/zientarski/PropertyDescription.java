package pl.zientarski;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PropertyDescription {

    private final boolean hasField;
    private final boolean hasSetter;
    private final boolean hasGetter;
    private final Type type;
    private final Set<Annotation> getterAnnotations = new HashSet<>();
    private final Set<Annotation> setterAnnotations = new HashSet<>();
    private final Set<Annotation> fieldAnnotations = new HashSet<>();
    private final Set<Annotation> allAnnotations = new HashSet<>();
    private final String name;
    private final MapperContext mapperContext;

    public PropertyDescription(final String name, final Field field, final Method setter, final Method getter, final MapperContext mapperContext) {
        this.mapperContext = mapperContext;
        assertName(name);
        assertTypesMatch(field, getter, setter);

        this.type = readType(field, getter, setter);
        this.name = name;

        if (field != null) {
            fieldAnnotations.addAll(Arrays.asList(field.getAnnotations()));
        }

        if (setter != null) {
            setterAnnotations.addAll(Arrays.asList(setter.getAnnotations()));
        }

        if (getter != null) {
            getterAnnotations.addAll(Arrays.asList(getter.getAnnotations()));
        }

        hasField = field != null;
        hasSetter = setter != null;
        hasGetter = getter != null;

        allAnnotations.addAll(getterAnnotations);
        allAnnotations.addAll(setterAnnotations);
        allAnnotations.addAll(fieldAnnotations);
    }

    private Type readType(final Field field, final Method getter, final Method setter) {
        Type type = null;
        if (field != null) {
            type = field.getGenericType();
        }
        if (setter != null) {
            type = setter.getGenericParameterTypes()[0];
        }
        if (getter != null) {
            type = getter.getGenericReturnType();
        }

        return replaceGenericVariables(type);
    }

    private Type replaceGenericVariables(final Type type) {
        if (type instanceof TypeVariable) {
            final TypeVariable<?> typeVariable = (TypeVariable<?>) type;
            return mapperContext.getGenericTypeByName(typeVariable.getTypeName());
        }
        return type;
    }

    private static void assertName(final String name) {
        if (name == null || name.length() == 0) {
            throw new MappingException("Parameter name cannot be empty");
        }
    }

    private static void assertTypesMatch(final Field field, final Method getter, final Method setter) {
        if (setter == null && getter == null && field == null) {
            throw new MappingException("All parameters null");
        }
        if (setter != null) {
            if (setter.getParameterCount() != 1) {
                throw new MappingException("Setter " + setter + " should have single parameter");
            }
        }
        if (field != null && getter != null) {
            if (!field.getGenericType().equals(getter.getGenericReturnType())) {
                throw new MappingException("Field and getter " + getter + " types did not match");
            }
        }
        if (field != null && setter != null) {
            if (!field.getGenericType().equals(setter.getGenericParameterTypes()[0])) {
                throw new MappingException("Field and setter " + setter + " types did not match");
            }
        }
        if (getter != null && setter != null) {
            if (!getter.getGenericReturnType().equals(setter.getGenericParameterTypes()[0])) {
                throw new MappingException("Getter and setter " + setter + " types did not match");
            }
        }
    }

    public boolean hasGetter() {
        return hasGetter;
    }

    public boolean hasSetter() {
        return hasSetter;
    }

    public boolean hasField() {
        return hasField;
    }

    public boolean hasAnnotation(final Class<? extends Annotation> annotation) {
        return allAnnotations.stream().filter(a -> a.annotationType().equals(annotation)).findAny().isPresent();
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
