package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class WildcardPropertyTest {
    private SchemaMapper mapper;

    class Generic {
        private Iterator<? extends GenericPropertyTest> property;

        public Iterator<? extends GenericPropertyTest> getProperty() {
            return property;
        }
    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void propertyTypeTest() throws Exception {
        //when
        final JSONObject schema = mapper.toJsonSchema4(Generic.class);

        //then
        final JSONObject property = schema.getJSONObject("properties").getJSONObject("property");
        final String ref = property.getString("$ref");

        assertThat(ref, equalTo("Iterator"));
    }
}
