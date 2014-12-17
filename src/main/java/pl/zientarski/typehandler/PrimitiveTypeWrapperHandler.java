package pl.zientarski.typehandler;

import org.json.JSONObject;
import pl.zientarski.MapperContext;
import pl.zientarski.TypeDescription;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static pl.zientarski.Utils.isPrimitiveTypeWrapper;
import static pl.zientarski.Utils.unwrap;

public class PrimitiveTypeWrapperHandler extends PrimitiveTypeHandler {

    @Override
    public boolean accepts(final Type type) {
        return !(type instanceof ParameterizedType) && isPrimitiveTypeWrapper((Class<?>) type);
    }

    @Override
    public JSONObject process(final TypeDescription typeDescription, final MapperContext mapperContext) {
        final Class<?> clazz = (Class<?>) typeDescription.getType();
        return primitiveTypeSchema(unwrap(clazz));
    }
}
