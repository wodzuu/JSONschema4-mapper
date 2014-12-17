package pl.zientarski;

import java.lang.reflect.Type;
import java.util.List;

public interface ReferenceNameProvider {
    String typeReferenceName(Class<?> clazz, List<Type> types);
}
