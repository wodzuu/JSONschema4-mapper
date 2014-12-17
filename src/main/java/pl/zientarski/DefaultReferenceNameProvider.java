package pl.zientarski;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

import static pl.zientarski.Utils.isPrimitiveTypeWrapper;
import static pl.zientarski.Utils.unwrap;

public class DefaultReferenceNameProvider implements ReferenceNameProvider {

    protected String getPrefix() {
        return "";
    }

    @Override
    public String typeReferenceName(final Class<?> clazz, final List<Type> genericTypeArguments) {
        final StringBuilder ref = new StringBuilder();
        if (isPrimitiveTypeWrapper(clazz)) {
            ref.append(unwrap(clazz).getSimpleName());
        } else {
            ref.append(clazz.getSimpleName());
        }
        appendGenericType(ref, genericTypeArguments);

        return getPrefix() + ref.toString();

    }

    protected void appendGenericType(final StringBuilder ref, final List<Type> genericTypeArguments) {
        if (genericTypeArguments == null || genericTypeArguments.size() == 0) {
            return;
        }
        final Type removed = genericTypeArguments.remove(0);

        ref.append("<");
        if (removed instanceof TypeVariable) {
            ref.append(((TypeVariable<?>) removed).getName());
        } else {
            ref.append(((Class<?>) removed).getSimpleName());
            appendGenericType(ref, genericTypeArguments);
        }
        ref.append(">");
    }
}
