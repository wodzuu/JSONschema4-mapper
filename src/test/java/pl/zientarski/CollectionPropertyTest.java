package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static pl.zientarski.JsonSchema4.TYPE_ANY;
import static pl.zientarski.JsonSchema4.TYPE_ARRAY;

public class CollectionPropertyTest {
    private SchemaMapper mapper;

    class R2D2 {
        @SuppressWarnings("rawtypes")
        private Collection theCollection;

        @SuppressWarnings("rawtypes")
        public Collection getTheCollection() {
            return theCollection;
        }

    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void collectionTest() {
        //when
        final JSONObject schema = mapper.toJsonSchema4(R2D2.class);

        //then
        final JSONObject properties = schema.getJSONObject("properties");
        final JSONObject colection = properties.getJSONObject("theCollection");

        final String type = colection.getString("type");
        assertThat(type, equalTo(TYPE_ARRAY));

        final String itemsType = colection.getJSONObject("items").getString("type");
        assertThat(itemsType, equalTo(TYPE_ANY));
    }
}
