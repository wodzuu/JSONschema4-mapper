package pl.zientarski;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static pl.zientarski.TestUtils.toStringSet;

public class EnumTest {
    private SchemaMapper mapper;

    enum HangarType {
        SMALL,
        MEDIUM,
        BIG
    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void enumPropertyTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(HangarType.class);

        //then
        assertTrue(schema.has("enum"));
    }

    @Test
    public void enumElementsTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(HangarType.class);

        //then
        final JSONArray hangarTypes = schema.getJSONArray("enum");
        assertThat(hangarTypes.length(), equalTo(3));

        final Set<String> types = toStringSet(hangarTypes);

        assertThat(types, hasItem(HangarType.BIG.name()));
        assertThat(types, hasItem(HangarType.SMALL.name()));
        assertThat(types, hasItem(HangarType.MEDIUM.name()));
    }

}
