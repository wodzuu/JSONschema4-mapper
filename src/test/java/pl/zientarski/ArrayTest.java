package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static pl.zientarski.JsonSchema4.TYPE_ARRAY;
import static pl.zientarski.JsonSchema4.TYPE_BOOLEAN;

public class ArrayTest {
    private SchemaMapper mapper;

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void primitivesArrayTypeTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(int[].class);

        //then
        assertThat(schema.getString("type"), equalTo(TYPE_ARRAY));
    }

    @Test
    public void primitivesArrayItemsTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(int[].class);

        //then
        assertTrue(schema.has("items"));

        final JSONObject items = schema.getJSONObject("items");
        assertTrue(items.has("$ref"));
    }

    @Test
    public void booleanArrayItemsTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(boolean[].class);

        //then
        assertTrue(schema.has("items"));

        final JSONObject items = schema.getJSONObject("items");
        assertTrue(items.has("type"));

        final String type = items.getString("type");
        assertThat(type, equalTo(TYPE_BOOLEAN));
    }

    @Test
    public void objectsArrayTypeTest() throws Exception {
        //given
        class Smoke {
        }

        //when
        final JSONObject schema = mapper.toJsonSchema4(Smoke[].class);

        //then
        assertThat(schema.getString("type"), equalTo(TYPE_ARRAY));
    }

    @Test
    public void objectsArrayItemsTest() throws Exception {
        //given
        class Smoke {
        }

        //when
        final JSONObject schema = mapper.toJsonSchema4(Smoke[].class);

        //then
        assertTrue(schema.has("items"));

        final JSONObject items = schema.getJSONObject("items");
        assertTrue(items.has("$ref"));

        final String type = items.getString("$ref");
        assertThat(type, equalTo(Smoke.class.getSimpleName()));
    }

    @Test
    public void objectsMultidimentionalArrayItemsTest() throws Exception {
        //given
        class Smoke {
        }

        //when
        final JSONObject schema = mapper.toJsonSchema4(Smoke[][].class);

        //then
        assertTrue(schema.has("items"));

        final JSONObject items = schema.getJSONObject("items");
        assertTrue(items.has("type"));

        final String itemsType = items.getString("type");
        assertThat(itemsType, equalTo(TYPE_ARRAY));

        assertTrue(items.has("items"));

        final JSONObject itemsItems = items.getJSONObject("items");
        assertTrue(itemsItems.has("$ref"));

        final String itemsItemsType = itemsItems.getString("$ref");
        assertThat(itemsItemsType, equalTo(Smoke.class.getSimpleName()));
    }
}
