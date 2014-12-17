package pl.zientarski.typehandler;

import org.json.JSONObject;
import pl.zientarski.JsonSchema4;
import pl.zientarski.MapperContext;
import pl.zientarski.TypeDescription;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static pl.zientarski.JsonSchema4.TYPE_STRING;
import static pl.zientarski.Utils.isDateTime;

public class DateTimeTypeHandler implements TypeHandler {

    @Override
    public boolean accepts(final Type type) {
        return !(type instanceof ParameterizedType) && isDateTime((Class<?>) type);
    }

    @Override
    public JSONObject process(final TypeDescription typeDescription, final MapperContext mapperContext) {
        final JSONObject result = new JSONObject();
        result.put("type", TYPE_STRING);
        result.put("format", mapperContext.getDateTimeFormat());
        result.put("$schema", JsonSchema4.SCHEMA_REFERENCE);
        return result;
    }
}
