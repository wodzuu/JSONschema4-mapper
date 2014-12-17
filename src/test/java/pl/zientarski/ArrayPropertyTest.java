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
public class ArrayPropertyTest {
    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"booleanArray", "type", TYPE_BOOLEAN},
                {"primitiveArray", "$ref", int.class.getSimpleName()},
                {"objectArray", "$ref", R2D2.class.getSimpleName()},
                {"stringArray", "type", TYPE_STRING},
                {"multiArray", "type", TYPE_ARRAY}
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
        private boolean[] booleanArray;
        private int[] primitiveArray;
        private R2D2[] objectArray;
        private String[] stringArray;
        private String[][] multiArray;

        public boolean[] getBooleanArray() {
            return booleanArray;
        }

        public int[] getPrimitiveArray() {
            return primitiveArray;
        }

        public R2D2[] getObjectArray() {
            return objectArray;
        }

        public String[] getStringArray() {
            return stringArray;
        }

        public String[][] getMultiArray() {
            return multiArray;
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
