package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class EnumPropertyTest {
    private SchemaMapper mapper;

    enum HangarType {
        SMALL,
        MEDIUM,
        BIG
    }

    class Hangar {
        private HangarType hangarType;

        public HangarType getHangarType() {
            return hangarType;
        }

    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void enumRefTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(Hangar.class);

        //then
        final JSONObject hangarType = schema.getJSONObject("properties").getJSONObject("hangarType");
        assertTrue(hangarType.has("$ref"));

        final String hangarTypeRef = hangarType.getString("$ref");
        assertThat(hangarTypeRef, equalTo(HangarType.class.getSimpleName()));
    }
}
