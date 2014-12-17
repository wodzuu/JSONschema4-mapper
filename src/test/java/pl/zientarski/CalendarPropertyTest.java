package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CalendarPropertyTest {
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
    public void calendarTest() {
        //when
        final JSONObject schema = mapper.toJsonSchema4(R2D2.class);

        //then
        final JSONObject properties = schema.getJSONObject("properties");
        final JSONObject calendar = properties.getJSONObject("theCalendar");

        final String ref = calendar.getString("$ref");
        assertThat(ref, equalTo(Calendar.class.getSimpleName()));
    }
}
