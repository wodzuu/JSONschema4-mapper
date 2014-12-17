package pl.zientarski.serialization;

import org.json.JSONObject;
import pl.zientarski.MapperContext;
import pl.zientarski.PropertyDescription;

public abstract class ReferenceSerializer extends PropertySerializer {

    public ReferenceSerializer(final PropertyDescription propertyDescription, final MapperContext mapperContext) {
        super(propertyDescription, mapperContext);
    }

    @Override
    public JSONObject toJsonObject() {
        final JSONObject result = new JSONObject();
        result.put("$ref", getReference());
        return result;
    }

    protected abstract String getReference();

}
