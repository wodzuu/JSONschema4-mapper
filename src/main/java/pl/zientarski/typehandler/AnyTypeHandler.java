package pl.zientarski.typehandler;

import org.json.JSONObject;
import pl.zientarski.MapperContext;
import pl.zientarski.MappingException;
import pl.zientarski.TypeDescription;

import java.lang.reflect.Type;

import static pl.zientarski.JsonSchema4.TYPE_ANY;

public class AnyTypeHandler implements TypeHandler {
    @Override
    public boolean accepts(final Type type) {
        return true;
    }

    @Override
    public JSONObject process(final TypeDescription typeDescription, final MapperContext mapperContext) {
        if (mapperContext.isStrict()) {
            throw new MappingException("Cannot process type:" + typeDescription.getType().getTypeName());
        } else {
            final JSONObject any = new JSONObject();
            any.put("type", TYPE_ANY);
            return any;
        }
    }
}
