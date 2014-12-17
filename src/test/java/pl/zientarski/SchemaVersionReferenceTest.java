package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class SchemaVersionReferenceTest {

    public static final String HTTP_JSON_SCHEMA_ORG_DRAFT_04_SCHEMA = "http://json-schema.org/draft-04/schema#";

    private SchemaMapper mapper;

    class Empty {
    }

    enum Enum {

    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void primitiveTest() throws Exception {
        //when
        JSONObject schema = mapper.toJsonSchema4(int.class);

        //then
        assertTrue(schema.has("$schema"));
        assertThat(schema.getString("$schema"), equalTo(HTTP_JSON_SCHEMA_ORG_DRAFT_04_SCHEMA));
    }

    @Test
    public void primitiveWrapperTest() throws Exception {
        //when
        JSONObject schema = mapper.toJsonSchema4(int.class);

        //then
        assertTrue(schema.has("$schema"));
        assertThat(schema.getString("$schema"), equalTo(HTTP_JSON_SCHEMA_ORG_DRAFT_04_SCHEMA));
    }

    @Test
    public void enumTest() throws Exception {
        //when
        JSONObject schema = mapper.toJsonSchema4(Enum.class);

        //then
        assertTrue(schema.has("$schema"));
        assertThat(schema.getString("$schema"), equalTo(HTTP_JSON_SCHEMA_ORG_DRAFT_04_SCHEMA));
    }

    @Test
    public void dateTest() throws Exception {
        //when
        JSONObject schema = mapper.toJsonSchema4(Date.class);

        //then
        assertTrue(schema.has("$schema"));
        assertThat(schema.getString("$schema"), equalTo(HTTP_JSON_SCHEMA_ORG_DRAFT_04_SCHEMA));
    }

    @Test
    public void classTest() throws Exception {
        //when
        JSONObject schema = mapper.toJsonSchema4(Empty.class);

        //then
        assertTrue(schema.has("$schema"));
        assertThat(schema.getString("$schema"), equalTo(HTTP_JSON_SCHEMA_ORG_DRAFT_04_SCHEMA));
    }

    @Test
    public void arrayTest() throws Exception {
        //when
        JSONObject schema = mapper.toJsonSchema4(Empty[].class);

        //then
        assertTrue(schema.has("$schema"));
        assertThat(schema.getString("$schema"), equalTo(HTTP_JSON_SCHEMA_ORG_DRAFT_04_SCHEMA));
    }
}
