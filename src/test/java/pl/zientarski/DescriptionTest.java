package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.Calendar;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class DescriptionTest {
    private SchemaMapper mapper;

    class R2D2 {
        private Calendar theCalendar;

        public Calendar getTheCalendar() {
            return theCalendar;
        }

    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void noDescriptionTest() {
        // when
        JSONObject schema = mapper.toJsonSchema4(R2D2.class);

        // then
        assertFalse(schema.has("description"));
    }

    @Test
    public void descriptionPropertyTest() throws Exception {
        // given
        mapper.setDescriptionProvider(Object::toString);

        // when
        JSONObject schema = mapper.toJsonSchema4(R2D2.class);

        // then
        assertTrue(schema.has("description"));
    }

    @Test
    public void descriptionPropertyValueTest() throws Exception {
        // given
        mapper.setDescriptionProvider(Type::getTypeName);

        // when
        JSONObject schema = mapper.toJsonSchema4(R2D2.class);

        // then
        String description = schema.getString("description");
        assertThat(description, equalTo("pl.zientarski.DescriptionTest$R2D2"));
    }
}
