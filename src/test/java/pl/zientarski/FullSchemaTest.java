package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.Semaphore;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FullSchemaTest {
    private SchemaMapper mapper;

    class SomeType {
        private Semaphore semaphore;

        private Iterator<String> iteratorOfStrings;

        public Semaphore getSemaphore() {
            return semaphore;
        }

        public Iterator<String> getIteratorOfStrings() {
            return iteratorOfStrings;
        }

    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void definitionsPropertyPresenceTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(SomeType.class, true);

        //then
        assertTrue(schema.has("definitions"));
    }

    @Test
    public void definitionsPropertyLengthTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(SomeType.class, true);

        //then
        JSONObject definitions = schema.getJSONObject("definitions");
        assertThat(definitions.length(), equalTo(2));
    }

    @Test
    public void definitionsPropertySemaphoreTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(SomeType.class, true);

        //then
        final JSONObject definitions = schema.getJSONObject("definitions");
        assertTrue(definitions.has("Semaphore"));

        final String reference = schema.getJSONObject("properties").getJSONObject("semaphore").getString("$ref");
        assertThat(reference, equalTo("#/definitions/Semaphore"));
    }

    @Test
    public void definitionsPropertyGenericTypeTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(SomeType.class, true);

        //then
        final JSONObject definitions = schema.getJSONObject("definitions");
        assertTrue(definitions.has("Iterator<String>"));

        final String reference = schema.getJSONObject("properties").getJSONObject("iteratorOfStrings").getString("$ref");
        assertThat(reference, equalTo("#/definitions/Iterator%3CString%3E"));
    }
}
