package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PropertyDiscoveryTest {
    private SchemaMapper mapper;

    class C3P0 {
        private String get;

        public Boolean getNonexistent() {
            return null;
        }

        public String getGet() {
            return get;
        }

        public void setSet(final Boolean set) {
        }
    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void getterWithFieldTest() throws Exception {
        //given
        mapper.setPropertyDiscoveryMode(PropertyDiscoveryMode.GETTER_WITH_FIELD);

        //when
        final JSONObject schema = mapper.toJsonSchema4(C3P0.class);

        //then
        final JSONObject properties = schema.getJSONObject("properties");
        assertThat(properties.length(), equalTo(1));
        assertTrue(properties.has("get"));
    }

    @Test
    public void getterTest() throws Exception {
        //given
        mapper.setPropertyDiscoveryMode(PropertyDiscoveryMode.GETTER);

        //when
        final JSONObject schema = mapper.toJsonSchema4(C3P0.class);

        //then
        final JSONObject properties = schema.getJSONObject("properties");
        assertThat(properties.length(), equalTo(2));
        assertTrue(properties.has("get"));
        assertTrue(properties.has("nonexistent"));
    }
}
