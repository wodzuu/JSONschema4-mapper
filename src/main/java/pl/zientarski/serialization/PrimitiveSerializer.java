package pl.zientarski.serialization;

import pl.zientarski.MapperContext;
import pl.zientarski.PropertyDescription;

public class PrimitiveSerializer extends ReferenceSerializer {

    public PrimitiveSerializer(final PropertyDescription propertyDescription, final MapperContext mapperContext) {
        super(propertyDescription, mapperContext);
    }

    @Override
    public boolean isPropertyRequired() {
        return true;
    }

    @Override
    protected String getReference() {
        return propertyDescription.getType().getTypeName();
    }
}
