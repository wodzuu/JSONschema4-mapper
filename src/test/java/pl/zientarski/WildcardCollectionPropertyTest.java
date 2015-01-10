package pl.zientarski;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static pl.zientarski.JsonSchema4.TYPE_ARRAY;
import static pl.zientarski.JsonSchema4.TYPE_BOOLEAN;
import static pl.zientarski.JsonSchema4.TYPE_STRING;

@RunWith(Parameterized.class)
public class WildcardCollectionPropertyTest {
    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"booleanCollection", "type", TYPE_BOOLEAN},
                {"primitiveCollection", "$ref", int.class.getSimpleName()},
                {"objectCollection", "$ref", R2D2.class.getSimpleName()},
                {"stringCollection", "type", TYPE_STRING},
                {"multiCollection", "type", TYPE_ARRAY}
        });
    }

    @Parameterized.Parameter(value = 0)
    public String propertyName;

    @Parameterized.Parameter(value = 1)
    public String referenceType;

    @Parameterized.Parameter(value = 2)
    public String referenceValue;

    private SchemaMapper mapper;

    class R2D2 {
        private Collection<? extends Boolean> booleanCollection;
        private Collection<? extends Integer> primitiveCollection;
        private Collection<? extends R2D2> objectCollection;
        private Collection<? extends String> stringCollection;
        private Collection<? extends Collection<String>> multiCollection;

        public Collection<? extends Boolean> getBooleanCollection() {
            return booleanCollection;
        }

        public Collection<? extends Integer> getPrimitiveCollection() {
            return primitiveCollection;
        }

        public Collection<? extends R2D2> getObjectCollection() {
            return objectCollection;
        }

        public Collection<? extends String> getStringCollection() {
            return stringCollection;
        }

        public Collection<? extends Collection<String>> getMultiCollection() {
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
