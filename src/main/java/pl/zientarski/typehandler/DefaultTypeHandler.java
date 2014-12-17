package pl.zientarski.typehandler;

import org.json.JSONArray;
import org.json.JSONObject;
import pl.zientarski.*;
import pl.zientarski.serialization.PropertySerializer;
import pl.zientarski.serialization.PropertySerializerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DefaultTypeHandler implements TypeHandler {

    private static final Predicate<PropertyDescription> hasGetter = PropertyDescription::hasGetter;
    private static final Predicate<PropertyDescription> hasField = PropertyDescription::hasField;

    @Override
    public boolean accepts(final Type type) {
        return !(type instanceof ParameterizedType);
    }

    @Override
    public JSONObject process(final TypeDescription typeDescription, final MapperContext mapperContext) {
        final Set<PropertyDescription> properties = typeDescription.getProperties();

        final JSONObject result = new JSONObject();
        result.put("type", "object");
        result.put("additionalProperties", false);
        result.put("$schema", JsonSchema4.SCHEMA_REFERENCE);
        mapperContext.addDescription(typeDescription.getType(), result);

        final Predicate<PropertyDescription> byGetterSetterAndFieldPresence = getByGetterSetterAndFieldPresencePredicate(mapperContext.getPropertyDiscoveryMode());
        final Set<PropertyDescription> filteredProperties = properties.stream().filter(byGetterSetterAndFieldPresence).collect(Collectors.toSet());

        addProperties(result, filteredProperties, mapperContext);

        return result;
    }

    private Predicate<PropertyDescription> getByGetterSetterAndFieldPresencePredicate(final PropertyDiscoveryMode propertyDiscoveryMode) {
        switch (propertyDiscoveryMode) {
            case GETTER:
                return hasGetter;
            case GETTER_WITH_FIELD:
                return hasGetter.and(hasField);
        }
        throw new MappingException("Not supported propertyDiscoveryMode: " + propertyDiscoveryMode);
    }

    private void addProperties(final JSONObject result, final Set<PropertyDescription> properties, final MapperContext mapperContext) {
        final Set<PropertySerializer> serializers = properties.stream().map(p -> PropertySerializerFactory.get(p, mapperContext)).collect(Collectors.toSet());

        if (properties.size() != 0) {
            final JSONObject objectProperties = new JSONObject();
            serializers.stream().forEach(serializer -> objectProperties.put(serializer.getPropertyName(), serializer.toJsonObject()));
            result.put("properties", objectProperties);
        }

        addRequiredPropertiesList(result, serializers);
    }

    private void addRequiredPropertiesList(final JSONObject result, final Set<PropertySerializer> serializers) {
        final Set<PropertySerializer> required = serializers.stream().filter(PropertySerializer::isPropertyRequired).collect(Collectors.toSet());
        if (required.size() != 0) {
            final JSONArray requiredNames = new JSONArray();
            required.stream().map(PropertySerializer::getPropertyName).forEach(requiredNames::put);
            result.put("required", requiredNames);
        }
    }
}
