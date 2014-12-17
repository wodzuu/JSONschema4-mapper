package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static pl.zientarski.JsonSchema4.*;

@RunWith(Parameterized.class)
public class GenericCollectionPropertyTest {
    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"booleanCollection", "type", TYPE_BOOLEAN},
                {"primitiveCollection", "$ref", int.class.getSimpleName()},
                {"objectCollection", "$ref", R2D2.class.getSimpleName()},
                {"stringCollection", "type", TYPE_STRING},
                {"multiCollection", "type", TYPE_ARRAY}
        });
    }

    @Parameter(value = 0)
    public String propertyName;

    @Parameter(value = 1)
    public String referenceType;

    @Parameter(value = 2)
    public String referenceValue;

    private SchemaMapper mapper;

    class R2D2 {
        private Collection<Boolean> booleanCollection;
        private Collection<Integer> primitiveCollection;
        private Collection<R2D2> objectCollection;
        private Collection<String> stringCollection;
        private Collection<Collection<String>> multiCollection;

        public Collection<Boolean> getBooleanCollection() {
            return booleanCollection;
        }

        public Collection<Integer> getPrimitiveCollection() {
            return primitiveCollection;
        }

        public Collection<R2D2> getObjectCollection() {
            return objectCollection;
        }

        public Collection<String> getStringCollection() {
            return stringCollection;
        }

        public Collection<Collection<String>> getMultiCollection() {
            return multiCollection;
        }

    }

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void arrayTest() {
        //when
        final JSONObject schema = mapper.toJsonSchema4(R2D2.class);

        //then
        final JSONObject properties = schema.getJSONObject("properties");
        final JSONObject primitiveArray = properties.getJSONObject(propertyName);

        final String type = primitiveArray.getString("type");
        assertThat(propertyName + " should be of type " + TYPE_ARRAY, type, equalTo(TYPE_ARRAY));

        final String itemsType = primitiveArray.getJSONObject("items").getString(referenceType);
        assertThat(propertyName + " items should be of type " + referenceValue, itemsType, equalTo(referenceValue));
    }
}
