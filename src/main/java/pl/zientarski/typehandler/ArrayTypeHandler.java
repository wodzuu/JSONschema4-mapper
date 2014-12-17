package pl.zientarski.typehandler;

import org.json.JSONObject;
import pl.zientarski.JsonSchema4;
import pl.zientarski.MapperContext;
import pl.zientarski.TypeDescription;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static pl.zientarski.JsonSchema4.*;
import static pl.zientarski.Utils.*;

public class ArrayTypeHandler implements TypeHandler {

    @Override
    public boolean accepts(final Type type) {
        return !(type instanceof ParameterizedType) && isArrayType((Class<?>) type);
    }

    @Override
    public JSONObject process(final TypeDescription typeDescription, final MapperContext mapperContext) {
        final Class<?> clazz = (Class<?>) typeDescription.getType();
        return arrayTypeSchema(getTypeArgument(clazz), mapperContext);
    }

    public static JSONObject arrayTypeSchema(final Type typeArgument, final MapperContext mapperContext) {
        JSONObject items = new JSONObject();
        if (isGenericType(typeArgument)) {
            items = arrayTypeSchema(getTypeArgument((ParameterizedType) typeArgument), mapperContext);
        } else {
            final Class<?> itemClass = (Class<?>) typeArgument;
            if (itemClass.equals(boolean.class) || itemClass.equals(Boolean.class)) {
                items.put("type", TYPE_BOOLEAN);
            } else if (itemClass.equals(String.class)) {
                items.put("type", TYPE_STRING);
            } else if (itemClass.isArray()) {
                items = arrayTypeSchema(getTypeArgument(itemClass), mapperContext);
            } else {
                items.put("$ref", mapperContext.getTypeReference(itemClass));
                mapperContext.addDependency(typeArgument);
            }
        }

        final JSONObject result = new JSONObject();
        result.put("type", TYPE_ARRAY);
        result.put("items", items);
        result.put("$schema", JsonSchema4.SCHEMA_REFERENCE);
        return result;
    }

}
