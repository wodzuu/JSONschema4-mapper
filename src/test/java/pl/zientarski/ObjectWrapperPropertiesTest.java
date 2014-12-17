package pl.zientarski;

import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Map.Entry;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static pl.zientarski.JsonSchema4.TYPE_BOOLEAN;

public class ObjectWrapperPropertiesTest {
    private SchemaMapper mapper;

    class PrimitiveProperties {
        private Boolean booleanProperty;
        private Byte byteProperty;
        private Short shortProperty;
        private Integer intProperty;
        private Long longProperty;
        private Float floatProperty;
        private Double doubleProperty;
        private Character charProperty;

        public Boolean getBooleanProperty() {
            return booleanProperty;
        }

        public Byte getByteProperty() {
            return byteProperty;
        }

        public Short getShortProperty() {
            return shortProperty;
        }

        public Integer getIntProperty() {
            return intProperty;
        }

        public Long getLongProperty() {
            return longProperty;
        }

        public Float getFloatProperty() {
            return floatProperty;
        }

        public Double getDoubleProperty() {
            return doubleProperty;
        }

        public Character getCharProperty() {
            return charProperty;
        }
    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void requiredKeyNotSetTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(PrimitiveProperties.class);

        //then
        assertFalse(schema.has("required"));
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
