package pl.zientarski.serialization;

import org.json.JSONObject;
import pl.zientarski.MapperContext;
import pl.zientarski.PropertyDescription;

public abstract class PropertySerializer {

    protected final PropertyDescription propertyDescription;
    protected final MapperContext mapperContext;

    public PropertySerializer(final PropertyDescription propertyDescription, final MapperContext mapperContext) {
        this.propertyDescription = propertyDescription;
        this.mapperContext = mapperContext;
    }

    public String getPropertyName() {
        return propertyDescription.getName();
    }

    public abstract JSONObject toJsonObject();

    public boolean isPropertyRequired() {
        return false;
    }
}
