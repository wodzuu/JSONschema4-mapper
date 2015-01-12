package pl.zientarski.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ParameterizedTypeFactory {
    public static ParameterizedType create(final Type genericType, final Type ... typeParameters) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return typeParameters;
            }

            @Override
            public Type getRawType() {
                return genericType;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
