package pl.zientarski.serialization;

import org.json.JSONObject;
import pl.zientarski.MapperContext;
import pl.zientarski.PropertyDescription;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import static pl.zientarski.JsonSchema4.*;
import static pl.zientarski.Utils.*;

public class CollectionSerializer extends PropertySerializer {

    private final Type collectedType;

    public CollectionSerializer(final PropertyDescription propertyDescription, final Type collectedType, final MapperContext mapperContext) {
        super(propertyDescription, mapperContext);
        this.collectedType = collectedType;
    }

    @Override
    public JSONObject toJsonObject() {
        return arrayTypeSchema(collectedType);
    }

    protected JSONObject arrayTypeSchema(final Type typeArgument) {
        JSONObject items = new JSONObject();
        final Type itemType = getItemType(typeArgument);
        if (isParameterizedType(itemType)) {
            items = arrayTypeSchema(getTypeArgument((ParameterizedType) itemType));
        } else if(isTypeVariable(itemType)) {
            final Type genericTypeByName = mapperContext.getGenericTypeByName(itemType.getTypeName());
            items.put("$ref", mapperContext.getTypeReference(genericTypeByName));
        } else {

            final Class<?> itemClass = (Class<?>) itemType;

            if (itemClass.equals(boolean.class) || itemClass.equals(Boolean.class)) {
                items.put("type", TYPE_BOOLEAN);
            } else if (itemClass.equals(Object.class)) {
                items.put("type", TYPE_ANY);
            } else if (itemClass.equals(String.class)) {
                items.put("type", TYPE_STRING);
            } else if (itemClass.isArray()) {
                items = arrayTypeSchema(getTypeArgument(itemClass));
            } else {
                items.put("$ref", mapperContext.getTypeReference(itemClass));
                mapperContext.addDependency(itemType);
            }
        }

        final JSONObject result = new JSONObject();
        result.put("type", TYPE_ARRAY);
        result.put("items", items);
        return result;
    }

    private Type getItemType(final Type typeArgument) {
        if (isWildcardType(typeArgument)) {
            return getTypeArgument((WildcardType) typeArgument);
        }
        return typeArgument;
    }
}
