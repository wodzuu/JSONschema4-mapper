package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.RetentionPolicy;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CustomReferenceNameProviderTest {
    private SchemaMapper mapper;

    class Empty {
        private Empty reference;
        private RetentionPolicy policy;

        public Empty getReference() {
            return reference;
        }

        public RetentionPolicy getPolicy() {
            return policy;
        }

        public void setPolicy(final RetentionPolicy policy) {
            this.policy = policy;
        }

    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void defaultReferenceTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(Empty.class);

        //then
        final String ref = schema.getJSONObject("properties").getJSONObject("reference").getString("$ref");
        assertThat(ref, equalTo(Empty.class.getSimpleName()));
    }

    @Test
    public void customReferenceTest() throws Exception {
        //given
        final String prefix = "c>";
        mapper.setReferenceNameProvider(new DefaultReferenceNameProvider() {
            @Override
            protected String getPrefix() {
                return prefix;
            }
        });

        //when
        final JSONObject schema = mapper.toJsonSchema4(Empty.class);

        //then
        final String ref = schema.getJSONObject("properties").getJSONObject("reference").getString("$ref");
        assertThat(ref, equalTo(prefix + Empty.class.getSimpleName()));
    }

    @Test
    public void enumReferenceTest() throws Exception {
        //given
        final String prefix = "c>";
        mapper.setReferenceNameProvider(new DefaultReferenceNameProvider() {
            @Override
            protected String getPrefix() {
                return prefix;
            }
        });

        //when
        final JSONObject schema = mapper.toJsonSchema4(Empty.class);

        //then
        final String enumRef = schema.getJSONObject("properties").getJSONObject("policy").getString("$ref");
        assertThat(enumRef, equalTo(prefix + RetentionPolicy.class.getSimpleName()));
    }
}
