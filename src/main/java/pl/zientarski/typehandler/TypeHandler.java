package pl.zientarski.typehandler;

import org.json.JSONObject;
import pl.zientarski.MapperContext;
import pl.zientarski.TypeDescription;

import java.lang.reflect.Type;

public interface TypeHandler {
    boolean accepts(Type type);

    JSONObject process(TypeDescription typeDescription, MapperContext mapperContext);
}
