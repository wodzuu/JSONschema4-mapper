package pl.zientarski.typehandler;

import org.json.JSONObject;
import pl.zientarski.JsonSchema4;
import pl.zientarski.MapperContext;
import pl.zientarski.TypeDescription;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static pl.zientarski.JsonSchema4.*;
import static pl.zientarski.Utils.isPrimitiveType;

public class PrimitiveTypeHandler implements TypeHandler {

    @Override
    public boolean accepts(final Type type) {
        return !(type instanceof ParameterizedType) && isPrimitiveType((Class<?>) type);
    }

    @Override
    public JSONObject process(final TypeDescription typeDescription, final MapperContext mapperContext) {
        final Class<?> clazz = (Class<?>) typeDescription.getType();
        return primitiveTypeSchema(clazz);
    }

    protected JSONObject primitiveTypeSchema(final Class<?> clazz) {
        final JSONObject result = new JSONObject();
        result.put("$schema", JsonSchema4.SCHEMA_REFERENCE);

        if (clazz.equals(boolean.class)) {
            result.put("type", TYPE_BOOLEAN);
        }

        if (clazz.equals(char.class)) {
            result.put("type", TYPE_STRING);
            result.put("minLength", 1);
            result.put("maxLength", 1);
        }

        if (clazz.equals(byte.class)) {
            result.put("type", TYPE_INTEGER);
            result.put("minimum", Byte.MIN_VALUE);
            result.put("maximum", Byte.MAX_VALUE);
        }

        if (clazz.equals(short.class)) {
            result.put("type", TYPE_INTEGER);
            result.put("minimum", Short.MIN_VALUE);
            result.put("maximum", Short.MAX_VALUE);
        }

        if (clazz.equals(int.class)) {
            result.put("type", TYPE_INTEGER);
            result.put("minimum", Integer.MIN_VALUE);
            result.put("maximum", Integer.MAX_VALUE);
        }

        if (clazz.equals(long.class)) {
            result.put("type", TYPE_INTEGER);
            result.put("minimum", Long.MIN_VALUE);
            result.put("maximum", Long.MAX_VALUE);
        }

        if (clazz.equals(float.class)) {
            result.put("type", TYPE_NUMBER);
            result.put("minimum", Float.MIN_VALUE);
            result.put("maximum", Float.MAX_VALUE);
        }

        if (clazz.equals(double.class)) {
            result.put("type", TYPE_NUMBER);
            result.put("minimum", Double.MIN_VALUE);
            result.put("maximum", Double.MAX_VALUE);
        }

        return result;
    }
}
