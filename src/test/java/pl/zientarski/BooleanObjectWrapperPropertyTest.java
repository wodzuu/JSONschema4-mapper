package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static pl.zientarski.JsonSchema4.TYPE_BOOLEAN;

public class BooleanObjectWrapperPropertyTest {
    private SchemaMapper mapper;

    class BooleanProperty {
        private Boolean booleanProperty;

        public Boolean getBooleanProperty() {
            return booleanProperty;
        }
    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void propertiesKeyTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(BooleanProperty.class);

        //then
        assertTrue(schema.has("properties"));
    }

    @Test
    public void requiredKeyTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(BooleanProperty.class);

        //then
        assertFalse(schema.has("required"));
    }

    @Test
    public void propertySetTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(BooleanProperty.class);

        //then
        final JSONObject properties = schema.getJSONObject("properties");
        assertThat(properties.length(), equalTo(1));
        assertTrue(properties.has("booleanProperty"));
    }

    @Test
    public void propertyTypeKeyTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(BooleanProperty.class);

        //then
        final JSONObject properties = schema.getJSONObject("properties");
        final JSONObject booleanProperty = properties.getJSONObject("booleanProperty");
        assertTrue(booleanProperty.has("type"));
    }

    @Test
    public void propertyTypeSetToBooleanTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(BooleanProperty.class);

        //then
        final JSONObject properties = schema.getJSONObject("properties");
        final JSONObject booleanProperty = properties.getJSONObject("booleanProperty");
        assertThat(booleanProperty.getString("type"), equalTo(TYPE_BOOLEAN));
    }
}
