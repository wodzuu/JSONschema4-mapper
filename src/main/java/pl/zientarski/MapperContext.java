package pl.zientarski;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static pl.zientarski.JsonSchema4.FORMAT_DATE_TIME;

public class MapperContext {
    private Map<String, Type> genericTypeNamesToTypesMapping;
    private ReferenceNameProvider referenceNameProvider = new DefaultReferenceNameProvider();
    private final List<Type> dependencies = new LinkedList<>();
    private String dateTimeFormat = FORMAT_DATE_TIME;
    private boolean strict = true;
    private PropertyDiscoveryMode propertyDiscoveryMode = PropertyDiscoveryMode.GETTER_WITH_FIELD;
    private final Set<Class<? extends Annotation>> requiredFieldAnnotations = new HashSet<>();
    private DescriptionProvider descriptionProvider;

    public Type getGenericTypeByName(final String name) {
        return genericTypeNamesToTypesMapping.get(name);
    }

    public String getTypeReference(final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) type;
            return referenceNameProvider.typeReferenceName((Class<?>) parameterizedType.getRawType(), getGenericReferenceArgumentList(parameterizedType));
        }
        return referenceNameProvider.typeReferenceName((Class<?>) type, null);
    }

    public void setGenericTypeNamesToTypesMapping(final Map<String, Type> genericTypeNamesToTypesMapping) {
        this.genericTypeNamesToTypesMapping = genericTypeNamesToTypesMapping;
    }

    public void setReferenceNameProvider(final ReferenceNameProvider referenceNameProvider) {
        this.referenceNameProvider = referenceNameProvider;
    }

    public void setRequiredFieldAnnotation(final List<Class<? extends Annotation>> annotations) {
        requiredFieldAnnotations.clear();
        requiredFieldAnnotations.addAll(annotations);
    }

    public void setDescriptionProvider(DescriptionProvider descriptionProvider) {
        this.descriptionProvider = descriptionProvider;
    }

    public void addDescription(final Type type, final JSONObject object) {
        if (descriptionProvider == null) return;

        object.put("description", descriptionProvider.process(type));
    }

    public void addDependency(final Type type) {
        if (!dependencies.contains(type)) {
            dependencies.add(type);
        }
    }

    public Iterator<Type> getDependencies() {
        return new Iterator<Type>() {

            int pointer = 0;

            @Override
            public boolean hasNext() {
                return pointer != dependencies.size();
            }

            @Override
            public Type next() {
                return dependencies.get(pointer++);
            }
        };
    }

    public void setDateTimeFormat(final String format) {
        this.dateTimeFormat = format;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public boolean isStrict() {
        return strict;
    }

    public void setStrict(final boolean strict) {
        this.strict = strict;
    }

    public void setPropertyDiscoveryMode(final PropertyDiscoveryMode propertyDiscoveryMode) {
        this.propertyDiscoveryMode = propertyDiscoveryMode;
    }

    public PropertyDiscoveryMode getPropertyDiscoveryMode() {
        return propertyDiscoveryMode;
    }

    public Set<Class<? extends Annotation>> getRequiredFieldAnnotations() {
        return requiredFieldAnnotations;
    }

    private List<Type> getGenericReferenceArgumentList(ParameterizedType type) {
        final List<Type> result = new LinkedList<>();

        Type argument = type.getActualTypeArguments()[0];
        while (argument instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType) argument;
            final Type rawType = parameterizedType.getRawType();
            result.add(rawType);
            argument = parameterizedType.getActualTypeArguments()[0];
        }

        if (argument instanceof TypeVariable) {
            final String name = ((TypeVariable<?>) argument).getName();
            result.add(getGenericTypeByName(name));
        }

        if (argument instanceof Class<?>) {
            result.add(argument);
        }

        return result;
    }

}
