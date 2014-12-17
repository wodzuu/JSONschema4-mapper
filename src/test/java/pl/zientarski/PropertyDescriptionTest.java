package pl.zientarski;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PropertyDescriptionTest {

    private Field field;
    private Method setter;
    private Method getter;

    class TestClass {
        private List<String> text;

        public List<String> getText() {
            return text;
        }

        public void setText(final List<String> text) {
            this.text = text;
        }
    }

    @Before
    public void before() throws NoSuchFieldException, SecurityException, NoSuchMethodException {
        this.field = TestClass.class.getDeclaredField("text");
        this.setter = TestClass.class.getDeclaredMethod("setText", List.class);
        this.getter = TestClass.class.getDeclaredMethod("getText");
    }

    @Test
    public void setterGetterFieldTest() throws Exception {
        //when
        final PropertyDescription propertyDescription = new PropertyDescription("_", field, setter, getter, null);

        //then
        assertTrue(propertyDescription.hasField());
        assertTrue(propertyDescription.hasSetter());
        assertTrue(propertyDescription.hasGetter());
    }

    @Test(expected = MappingException.class)
    public void allNullsTest() throws Exception {
        //when
        new PropertyDescription("_", null, null, null, null);
    }
}
