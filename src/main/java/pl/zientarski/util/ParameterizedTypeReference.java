package pl.zientarski.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class ParameterizedTypeReference<T> {

    private final ParameterizedType type;

    protected ParameterizedTypeReference() {
        final Class<?> parameterizedTypeReferenceSubClass = findParameterizedTypeReferenceSubClass(getClass());
        final Type type = parameterizedTypeReferenceSubClass.getGenericSuperclass();
        final ParameterizedType parameterizedType = (ParameterizedType) type;
        final Type parameterType = parameterizedType.getActualTypeArguments()[0];
        if(!(parameterType instanceof ParameterizedType)){
            throw new IllegalArgumentException("Given type is not generic");
        }
        this.type = (ParameterizedType) parameterType;
    }

    private static Class<?> findParameterizedTypeReferenceSubClass(Class<?> child) {

        Class<?> parent = child.getSuperclass();

        if (Object.class.equals(parent)) {
            throw new IllegalStateException("Expected ParameterizedTypeReference superclass");
        }
        else if (ParameterizedTypeReference.class.equals(parent)) {
            return child;
        }
        else {
            return findParameterizedTypeReferenceSubClass(parent);
        }
    }

    public ParameterizedType getType() {
        return this.type;
    }
}
