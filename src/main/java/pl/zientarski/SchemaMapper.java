package pl.zientarski;

import org.json.JSONObject;
import pl.zientarski.typehandler.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

import static pl.zientarski.Utils.isGenericType;

public class SchemaMapper {

    private final MapperContext mapperContext = new MapperContext();
    private final List<TypeHandler> typeHandlers = new LinkedList<>();

    public SchemaMapper() {
        typeHandlers.add(new EnumTypeHandler());
        typeHandlers.add(new ArrayTypeHandler());
        typeHandlers.add(new PrimitiveTypeHandler());
        typeHandlers.add(new PrimitiveTypeWrapperHandler());
        typeHandlers.add(new DateTimeTypeHandler());
        typeHandlers.add(new DefaultTypeHandler());
        typeHandlers.add(new AnyTypeHandler());
    }

    public void setReferenceNameProvider(final ReferenceNameProvider referenceNameProvider) {
        mapperContext.setReferenceNameProvider(referenceNameProvider);
    }

    public void setPropertyDiscoveryMode(final PropertyDiscoveryMode propertyDiscoveryMode) {
        mapperContext.setPropertyDiscoveryMode(propertyDiscoveryMode);
    }

    public void setDateTimeFormat(final String format) {
        mapperContext.setDateTimeFormat(format);
    }

    public void setRelaxedMode(final boolean isRelaxed) {
        mapperContext.setStrict(!isRelaxed);
    }

    public void setRequiredFieldAnnotations(final List<Class<? extends Annotation>> annotations) {
        mapperContext.setRequiredFieldAnnotation(annotations);
    }

    public void addTypeHandler(final TypeHandler typeHandler) {
        typeHandlers.add(0, typeHandler);
    }

    public void setDescriptionProvider(DescriptionProvider descriptionProvider) {
        mapperContext.setDescriptionProvider(descriptionProvider);
    }

    public Iterator<Type> getDependencies() {
        return mapperContext.getDependencies();
    }

    public JSONObject toJsonSchema4(final Type type) {
        if (isGenericType(type)) {
            final ParameterizedType genericType = (ParameterizedType) type;
            final Type rawType = genericType.getRawType();

            final Map<String, Type> genericTypeNamesToTypes = discoverGenericTypeNameToActualTypeMapping(genericType);
            mapperContext.setGenericTypeNamesToTypesMapping(genericTypeNamesToTypes);

            return toJsonSchema4(rawType);
        } else {
            return toJsonSchema4((Class<?>) type);
        }
    }

    public JSONObject toJsonSchema4(final Class<?> clazz) {
        final Optional<TypeHandler> typeHandler = typeHandlers.stream().filter(th -> th.accepts(clazz)).findFirst();
        if (typeHandler.isPresent()) {
            return typeHandler.get().process(new TypeDescription(clazz, mapperContext), mapperContext);
        }

        throw new MappingException("No type handler for type: " + clazz.toString());
    }

    public JSONObject toJsonSchema4(Class<?> clazz, boolean includeDependencies) {
        if (includeDependencies) {
            setReferenceNameProvider(new DefinitionsReferenceNameProvider());

            final JSONObject schema = toJsonSchema4(clazz);

            setReferenceNameProvider(new DefaultReferenceNameProvider());

            final JSONObject definitions = new JSONObject();

            getDependencies().forEachRemaining(type -> {
                final JSONObject dependencySchema = toJsonSchema4(type);
                dependencySchema.remove("$schema");
                definitions.put(mapperContext.getTypeReference(type), dependencySchema);
            });

            schema.put("definitions", definitions);
            return schema;
        } else {
            return toJsonSchema4(clazz);
        }
    }

    private static Map<String, Type> discoverGenericTypeNameToActualTypeMapping(final ParameterizedType genericType) {
        final Map<String, Type> result = new HashMap<>();
        final Class<?> rawType = (Class<?>) genericType.getRawType();
        final TypeVariable<?>[] typeParameters = rawType.getTypeParameters();
        final Type[] actualTypeArguments = genericType.getActualTypeArguments();
        for (int i = 0; i < typeParameters.length; i++) {
            result.put(typeParameters[i].getName(), actualTypeArguments[i]);
        }
        return result;
    }
}
