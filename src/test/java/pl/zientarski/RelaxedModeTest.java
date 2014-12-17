package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class RelaxedModeTest {
    private SchemaMapper mapper;

    class Patient {
        private Map<String, Object> someMap;

        public Map<String, Object> getSomeMap() {
            return someMap;
        }

    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test(expected = MappingException.class)
    public void defaultModeTest() throws Exception {
        //when
        mapper.toJsonSchema4(Patient.class).toString(4);
    }

    @Test(expected = MappingException.class)
    public void strictModeTest() throws Exception {
        //given
        mapper.setRelaxedMode(false);

        //when
        mapper.toJsonSchema4(Patient.class);
    }

    @Test
    public void relaxedTest() throws Exception {
        //given
        mapper.setRelaxedMode(true);

        //when
        final JSONObject schema = mapper.toJsonSchema4(Patient.class);

        //then
        final JSONObject someMapProperty = schema.getJSONObject("properties").getJSONObject("someMap");
        final String type = someMapProperty.getString("type");
        assertThat(type, equalTo("any"));
    }
}
