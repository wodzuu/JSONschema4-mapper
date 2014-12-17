package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static pl.zientarski.JsonSchema4.*;

public class PrimitiveTypesTest {
    private SchemaMapper mapper;

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void booleanTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(boolean.class);

        //then
        assertTrue(schema.has("type"));
        assertThat(schema.getString("type"), equalTo(TYPE_BOOLEAN));
    }

    @Test
    public void booleanWrapperTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(Boolean.class);

        //then
        assertTrue(schema.has("type"));
        assertThat(schema.getString("type"), equalTo(TYPE_BOOLEAN));
    }

    @Test
    public void numericTypesTest() throws Exception {
        testPrimitiveType(byte.class, Byte.MIN_VALUE, Byte.MAX_VALUE);
        testPrimitiveType(short.class, Short.MIN_VALUE, Short.MAX_VALUE);
        testPrimitiveType(int.class, Integer.MIN_VALUE, Integer.MAX_VALUE);
        testPrimitiveType(long.class, Long.MIN_VALUE, Long.MAX_VALUE);

        testPrimitiveType(float.class, Float.MIN_VALUE, Float.MAX_VALUE);
        testPrimitiveType(double.class, Double.MIN_VALUE, Double.MAX_VALUE);

        testPrimitiveType(Byte.class, Byte.MIN_VALUE, Byte.MAX_VALUE);
        testPrimitiveType(Short.class, Short.MIN_VALUE, Short.MAX_VALUE);
        testPrimitiveType(Integer.class, Integer.MIN_VALUE, Integer.MAX_VALUE);
        testPrimitiveType(Long.class, Long.MIN_VALUE, Long.MAX_VALUE);

        testPrimitiveType(Float.class, Float.MIN_VALUE, Float.MAX_VALUE);
        testPrimitiveType(Double.class, Double.MIN_VALUE, Double.MAX_VALUE);
    }

    @Test
    public void charTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(char.class);

        //then
        assertTrue(schema.has("type"));
        assertTrue(schema.has("minLength"));
        assertTrue(schema.has("maxLength"));

        assertThat(schema.getString("type"), equalTo(TYPE_STRING));
        assertThat(schema.getInt("minLength"), equalTo(1));
        assertThat(schema.getInt("maxLength"), equalTo(1));
    }

    @Test
    public void charWrapperTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(Character.class);

        //then
        assertTrue(schema.has("type"));
        assertTrue(schema.has("minLength"));
        assertTrue(schema.has("maxLength"));

        assertThat(schema.getString("type"), equalTo(TYPE_STRING));
        assertThat(schema.getInt("minLength"), equalTo(1));
        assertThat(schema.getInt("maxLength"), equalTo(1));
    }

    private void testPrimitiveType(final Class<?> clazz, final double min, final double max) {
        //when
        final JSONObject schema = mapper.toJsonSchema4(clazz);

        //then
        assertTrue(schema.has("type"));
        assertTrue(schema.has("minimum"));
        assertTrue(schema.has("maximum"));

        assertThat(schema.getString("type"), equalTo(TYPE_NUMBER));
        assertThat(schema.getDouble("minimum"), equalTo(min));
        assertThat(schema.getDouble("maximum"), equalTo(max));
    }

    private void testPrimitiveType(final Class<?> clazz, final long min, final long max) {
        //when
        final JSONObject schema = mapper.toJsonSchema4(clazz);

        //then
        assertTrue(schema.has("type"));
        assertTrue(schema.has("minimum"));
        assertTrue(schema.has("maximum"));

        assertThat(schema.getString("type"), equalTo(TYPE_INTEGER));
        assertThat(schema.getLong("minimum"), equalTo(min));
        assertThat(schema.getLong("maximum"), equalTo(max));
    }
}
