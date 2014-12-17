package pl.zientarski.serialization;

import org.json.JSONObject;
import pl.zientarski.MapperContext;
import pl.zientarski.MappingException;
import pl.zientarski.PropertyDescription;

import java.lang.reflect.ParameterizedType;

import static pl.zientarski.JsonSchema4.TYPE_ANY;

public class GenericObjectSerializer extends ObjectSerializer {

    private final ParameterizedType type;

    public GenericObjectSerializer(final PropertyDescription propertyDescription, final MapperContext mapperContext) {
        super(propertyDescription, mapperContext);
        type = (ParameterizedType) propertyDescription.getType();
    }

    @Override
    public JSONObject toJsonObject() {
        final JSONObject result = new JSONObject();
        if (type.getActualTypeArguments().length != 1) {
            if (mapperContext.isStrict()) {
                throw new MappingException("Could not serialize type:" + type);
            } else {
                result.put("type", TYPE_ANY);
            }
        } else {
            result.put("$ref", getReference());
        }
        return result;
    }

    @Override
    protected String getReference() {
        mapperContext.addDependency(type);
        return mapperContext.getTypeReference(type);
    }
}
