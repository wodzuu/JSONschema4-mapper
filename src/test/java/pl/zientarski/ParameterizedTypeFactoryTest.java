package pl.zientarski;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import pl.zientarski.util.ParameterizedTypeFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

public class ParameterizedTypeFactoryTest {
    class Generic<T> {
        T property;

        public T getProperty() {
            return property;
        }
    }

    @Test
    public void typeTest() throws Exception {
        //when
        final Type type = ParameterizedTypeFactory.create(Generic.class, String.class);

        //then
        assertThat(type, instanceOf(ParameterizedType.class));
    }

    @Test
    public void rawTypeTest() throws Exception {
        //when
        final ParameterizedType type = ParameterizedTypeFactory.create(Generic.class, String.class);

        //then
        assertThat(type.getRawType(), equalTo(Generic.class));
    }

    @Test
    public void parameterTypeTest() throws Exception {
        //when
        final ParameterizedType type = ParameterizedTypeFactory.create(Generic.class, String.class);

        //then
        assertThat(type.getActualTypeArguments()[0], equalTo(String.class));
    }

    @Test
    public void schemaTest() throws Exception {
        //when
        final JSONObject schema = new SchemaMapper().toJsonSchema4(ParameterizedTypeFactory.create(Generic.class, String.class));

        //then
        final JSONObject property = schema.getJSONObject("properties").getJSONObject("property");
        final String propertyType = property.getString("type");

        Assert.assertThat(propertyType, equalTo("string"));
    }
}
