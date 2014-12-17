package pl.zientarski;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static pl.zientarski.JsonSchema4.TYPE_BOOLEAN;

public class Jsr303NotNullObjectPropertyTest {
    private SchemaMapper mapper;

    class BooleanProperty {
        @NotNull
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
        mapper.setRequiredFieldAnnotations(Arrays.asList(NotNull.class));

        //when
        final JSONObject schema = mapper.toJsonSchema4(BooleanProperty.class);

        //then
        assertTrue(schema.has("required"));
    }

    @Test
    public void requiredValueTest() throws Exception {
        //given
        mapper.setRequiredFieldAnnotations(Arrays.asList(NotNull.class));

        //when
        final JSONObject schema = mapper.toJsonSchema4(BooleanProperty.class);

        //then
        final JSONArray required = schema.getJSONArray("required");
        assertThat(required.length(), equalTo(1));
        assertThat(required.getString(0), equalTo("booleanProperty"));
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
