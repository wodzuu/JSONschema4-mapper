package pl.zientarski;

import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;
import static pl.zientarski.JsonSchema4.TYPE_BOOLEAN;
import static pl.zientarski.TestUtils.toStringSet;

public class PrimitivePropertiesTest {
    private SchemaMapper mapper;

    class PrimitiveProperties {
        private boolean booleanProperty;
        private byte byteProperty;
        private short shortProperty;
        private int intProperty;
        private long longProperty;
        private float floatProperty;
        private double doubleProperty;
        private char charProperty;

        public boolean isBooleanProperty() {
            return booleanProperty;
        }

        public byte getByteProperty() {
            return byteProperty;
        }

        public short getShortProperty() {
            return shortProperty;
        }

        public int getIntProperty() {
            return intProperty;
        }

        public long getLongProperty() {
            return longProperty;
        }

        public float getFloatProperty() {
            return floatProperty;
        }

        public double getDoubleProperty() {
            return doubleProperty;
        }

        public char getCharProperty() {
            return charProperty;
        }
    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void requiredKeySetTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(PrimitiveProperties.class);

        //then
        assertTrue(schema.has("required"));
    }

    @Test
    public void requiredPropertiesTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(PrimitiveProperties.class);

        //then
        assertThat(schema.getJSONArray("required").length(), equalTo(8));

        final Set<String> requiredProperties = toStringSet(schema.getJSONArray("required"));
        assertThat(requiredProperties, hasItem("booleanProperty"));
        assertThat(requiredProperties, hasItem("byteProperty"));
        assertThat(requiredProperties, hasItem("shortProperty"));
        assertThat(requiredProperties, hasItem("intProperty"));
        assertThat(requiredProperties, hasItem("longProperty"));
        assertThat(requiredProperties, hasItem("floatProperty"));
        assertThat(requiredProperties, hasItem("doubleProperty"));
        assertThat(requiredProperties, hasItem("charProperty"));
    }

    @Test
    public void booleanTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(PrimitiveProperties.class);

        //then
        final JSONObject booleanProperty = schema.getJSONObject("properties").getJSONObject("booleanProperty");
        assertTrue(booleanProperty.has("type"));

        final String booleanPropertyType = booleanProperty.getString("type");
        assertThat(booleanPropertyType, equalTo(TYPE_BOOLEAN));
    }

    @Test
    public void refTest() throws Exception {
        //given
        final Map<String, String> typeToRef = new ImmutableMap.Builder<String, String>()
                .put("byteProperty", "byte")
                .put("shortProperty", "short")
                .put("intProperty", "int")
                .put("longProperty", "long")
                .put("floatProperty", "float")
                .put("doubleProperty", "double")
                .put("charProperty", "char")
                .build();

        //when
        final JSONObject schema = mapper.toJsonSchema4(PrimitiveProperties.class);

        //then
        for (final Entry<String, String> typeRefPair : typeToRef.entrySet()) {
            final String type = typeRefPair.getKey();
            final String ref = typeRefPair.getValue();

            final JSONObject shortProperty = schema.getJSONObject("properties").getJSONObject(type);
            assertFalse(type + " should not have 'type' property", shortProperty.has("type"));
            assertTrue(type + " should have '$ref' property", shortProperty.has("$ref"));

            final String shortPropertyRef = shortProperty.getString("$ref");
            assertThat(type + " '$ref' property should be equal to " + ref, shortPropertyRef, equalTo(ref));
        }
    }
}
