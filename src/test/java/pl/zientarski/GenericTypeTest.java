package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static pl.zientarski.JsonSchema4.TYPE_STRING;

public class GenericTypeTest {
    private SchemaMapper mapper;
    private Type type;

    class Generic<T> {
        private T property;

        private Generic<T> nested;

        private Generic<Generic<T>> doubleNested;

        public T getProperty() {
            return property;
        }

        public Generic<T> getNested() {
            return nested;
        }

        public Generic<Generic<T>> getDoubleNested() {
            return doubleNested;
        }
    }

    public Generic<String> helperMethod() {
        return null;
    }

    @Before
    public void before() throws NoSuchMethodException {
        mapper = new SchemaMapper();
        type = GenericTypeTest.class.getDeclaredMethod("helperMethod").getGenericReturnType();
    }

    @Test
    public void propertyTypeTest() throws Exception {
        //when
        final JSONObject schema = mapper.toJsonSchema4(type);

        //then
        final JSONObject property = schema.getJSONObject("properties").getJSONObject("property");
        final String propertyType = property.getString("type");

        assertThat(propertyType, equalTo(TYPE_STRING));
    }

    @Test
    public void nestedTypeTest() throws Exception {
        //when
        final JSONObject schema = mapper.toJsonSchema4(type);

        //then
        final JSONObject property = schema.getJSONObject("properties").getJSONObject("nested");
        final String propertyRef = property.getString("$ref");

        assertThat(propertyRef, equalTo("Generic<String>"));
    }

    @Test
    public void doubleNestedTypeTest() throws Exception {
        //when
        final JSONObject schema = mapper.toJsonSchema4(type);

        //then
        final JSONObject property = schema.getJSONObject("properties").getJSONObject("doubleNested");
        final String propertyRef = property.getString("$ref");

        assertThat(propertyRef, equalTo("Generic<Generic<String>>"));
    }
}
