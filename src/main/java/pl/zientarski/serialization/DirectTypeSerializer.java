package pl.zientarski.serialization;

import org.json.JSONObject;
import pl.zientarski.MapperContext;
import pl.zientarski.PropertyDescription;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import static pl.zientarski.JsonSchema4.TYPE_BOOLEAN;
import static pl.zientarski.JsonSchema4.TYPE_STRING;

public class DirectTypeSerializer extends PropertySerializer {

    public DirectTypeSerializer(final PropertyDescription propertyDescription, final MapperContext mapperContext) {
        super(propertyDescription, mapperContext);
    }

    @Override
    public boolean isPropertyRequired() {
        if (propertyDescription.getType().equals(boolean.class)) {
            return true;
        }
        for (final Class<? extends Annotation> annotation : mapperContext.getRequiredFieldAnnotations()) {
            if (propertyDescription.hasAnnotation(annotation)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public JSONObject toJsonObject() {
        final Type type = propertyDescription.getType();

        final JSONObject result = new JSONObject();
        if (type.equals(boolean.class) || type.equals(Boolean.class)) {
            result.put("type", TYPE_BOOLEAN);
        }
        if (type.equals(String.class)) {
            result.put("type", TYPE_STRING);
        }
        return result;
    }

}
