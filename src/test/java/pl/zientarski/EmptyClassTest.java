package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static pl.zientarski.JsonSchema4.TYPE_OBJECT;

public class EmptyClassTest {
    private SchemaMapper mapper;

    class Empty {
    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void typeTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(Empty.class);

        //then
        assertTrue(schema.has("type"));
        assertThat(schema.getString("type"), equalTo(TYPE_OBJECT));
    }

    @Test
    public void additionalPropertiesTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(Empty.class);

        //then
        assertTrue(schema.has("additionalProperties"));
        assertFalse(schema.has("properties"));
        assertFalse(schema.getBoolean("additionalProperties"));
    }

    @Test
    public void propertiesTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(Empty.class);

        //then
        assertFalse(schema.has("properties"));
    }
}
