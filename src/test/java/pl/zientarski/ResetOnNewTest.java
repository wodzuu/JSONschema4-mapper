package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class ResetOnNewTest {
    private SchemaMapper mapper;

    class Empty {
        private Empty reference;

        public Empty getReference() {
            return reference;
        }

    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void customReferenceTest() throws Exception {
        //given
        mapper.setReferenceNameProvider(new DefaultReferenceNameProvider() {
            @Override
            protected String getPrefix() {
                return "zzz";
            }
        });
        mapper = new SchemaMapper();

        //when
        final JSONObject schema = mapper.toJsonSchema4(Empty.class);

        //then
        final String ref = schema.getJSONObject("properties").getJSONObject("reference").getString("$ref");
        assertThat(ref, equalTo(Empty.class.getSimpleName()));
    }
}
