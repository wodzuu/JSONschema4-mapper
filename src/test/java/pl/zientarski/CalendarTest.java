package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static pl.zientarski.JsonSchema4.FORMAT_DATE_TIME;
import static pl.zientarski.JsonSchema4.TYPE_STRING;

public class CalendarTest {
    private SchemaMapper mapper;

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void calendarTypeTest() {
        //when
        final JSONObject schema = mapper.toJsonSchema4(Calendar.class);

        //then
        assertTrue(schema.has("type"));
        assertThat(schema.getString("type"), equalTo(TYPE_STRING));
    }

    @Test
    public void calendarFormatTest() {
        //when
        final JSONObject schema = mapper.toJsonSchema4(Calendar.class);

        //then
        assertTrue(schema.has("format"));
        assertThat(schema.getString("format"), equalTo(FORMAT_DATE_TIME));
    }

    @Test
    public void dateTypeTest() {
        //when
        final JSONObject schema = mapper.toJsonSchema4(Date.class);

        //then
        assertTrue(schema.has("type"));
        assertThat(schema.getString("type"), equalTo(TYPE_STRING));
    }

    @Test
    public void dateFormatTest() {
        //when
        final JSONObject schema = mapper.toJsonSchema4(Date.class);

        //then
        assertTrue(schema.has("format"));
        assertThat(schema.getString("format"), equalTo(FORMAT_DATE_TIME));
    }

    @Test
    public void dateFormatSettingDateTest() {
        //given
        final String format = "frog";
        mapper.setDateTimeFormat(format);

        //when
        final JSONObject schema = mapper.toJsonSchema4(Date.class);

        //then
        assertTrue(schema.has("format"));
        assertThat(schema.getString("format"), equalTo(format));
    }

    @Test
    public void dateFormatSettingCalendarTest() {
        //given
        final String format = "frog";
        mapper.setDateTimeFormat(format);

        //when
        final JSONObject schema = mapper.toJsonSchema4(Calendar.class);

        //then
        assertTrue(schema.has("format"));
        assertThat(schema.getString("format"), equalTo(format));
    }
}
