package pl.zientarski.serialization;

import pl.zientarski.MapperContext;
import pl.zientarski.PropertyDescription;

public class ObjectSerializer extends ReferenceSerializer {

    public ObjectSerializer(final PropertyDescription propertyDescription, final MapperContext mapperContext) {
        super(propertyDescription, mapperContext);
    }

    @Override
    public boolean isPropertyRequired() {
        return false;
    }

    @Override
    protected String getReference() {
        final Class<?> clazz = (Class<?>) propertyDescription.getType();
        mapperContext.addDependency(propertyDescription.getType());
        return mapperContext.getTypeReference(clazz);
    }
}
