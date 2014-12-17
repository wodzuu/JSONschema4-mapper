package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class InheritanceTest {
    private SchemaMapper mapper;

    class Parent {
        private int age;

        public int getAge() {
            return age;
        }
    }

    class Child extends Parent {
        private int deciduousTeeth;

        public int getDeciduousTeeth() {
            return deciduousTeeth;
        }
    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void propertiesKeyTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(Child.class);

        //then
        assertTrue(schema.has("properties"));
    }

    @Test
    public void propertiesValuesTest() throws Exception {
        //given

        //when
        final JSONObject schema = mapper.toJsonSchema4(Child.class);

        //then
        final JSONObject properties = schema.getJSONObject("properties");
        assertThat(properties.length(), equalTo(2));

        assertTrue(properties.has("deciduousTeeth"));
        assertTrue(properties.has("age"));
    }
}
