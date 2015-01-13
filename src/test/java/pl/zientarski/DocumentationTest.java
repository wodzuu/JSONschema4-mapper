package pl.zientarski;

import com.google.common.collect.Lists;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import pl.zientarski.typehandler.TypeHandler;
import pl.zientarski.util.ParameterizedTypeReference;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.*;

/**
 * If any of following tests fails then project documentation should be updated
 */
public class DocumentationTest {
    private SchemaMapper schemaMapper;

    @Before
    public void before() {
        schemaMapper = new SchemaMapper();
    }

    @Test
    public void introTest() throws Exception {
        //given
        final String expectedSchema = "{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"mindBlowing\":{\"type\":\"boolean\"}},\"required\":[\"mindBlowing\"]}";

        //and
        class Demo {
            private boolean mindBlowing;

            @SuppressWarnings("unused")
            public boolean isMindBlowing() {
                return mindBlowing;
            }

        }

        //when
        final JSONObject schema = schemaMapper.toJsonSchema4(Demo.class);

        //then
        assertEquals(expectedSchema, schema.toString());
    }

    @Test
    public void dependenciesTest() throws Exception {
        //given
        final String expectedSchema = "{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"exception\":{\"$ref\":\"Exception\"},\"string\":{\"type\":\"string\"},\"thread\":{\"$ref\":\"Thread\"}}}";

        //and
        class Demo {
            private Exception exception;
            private Thread thread;
            private String string;

            public String getString() {
                return string;
            }

            public Exception getException() {
                return exception;
            }

            public Thread getThread() {
                return thread;
            }

        }

        //when
        final JSONObject schema = schemaMapper.toJsonSchema4(Demo.class);

        //then
        assertEquals(expectedSchema, schema.toString());

        //and
        final ArrayList<Type> dependencies = Lists.newArrayList(schemaMapper.getDependencies());
        assertThat(dependencies.size(), equalTo(2));
        assertThat(dependencies, hasItem(Exception.class));
        assertThat(dependencies, hasItem(Thread.class));

    }

    @Test
    public void fullSchemaTest() throws Exception {
        //given
        class Dependency<T> {
            private T name;

            public T getName() {
                return name;
            }
        }

        //and
        class Demo {
            private Dependency<String> stringDependency;

            public Dependency<String> getStringDependency() {
                return stringDependency;
            }
        }

        //when
        final JSONObject schema = schemaMapper.toJsonSchema4(Demo.class, true);

        //then
        final JSONObject definitions = schema.getJSONObject("definitions");
        assertTrue(definitions.has("Dependency<String>"));

        final String reference = schema.getJSONObject("properties").getJSONObject("stringDependency").getString("$ref");
        assertThat(reference, equalTo("#/definitions/Dependency%3CString%3E"));

        assertThat(schema.toString(), equalTo("{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"definitions\":{\"Dependency<String>\":{\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"string\"}}}},\"properties\":{\"stringDependency\":{\"$ref\":\"#/definitions/Dependency%3CString%3E\"}}}"));
    }

    interface Demo {
        int getNumber();
    }

    @Test
    public void getterDiscoveryTest() throws Exception {
        //given
        schemaMapper.setPropertyDiscoveryMode(PropertyDiscoveryMode.GETTER);

        //when
        final JSONObject schema = schemaMapper.toJsonSchema4(Demo.class);

        //then
        final JSONObject definitions = schema.getJSONObject("properties");
        assertTrue(definitions.has("number"));

        assertThat(schema.toString(), equalTo("{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"number\":{\"$ref\":\"int\"}},\"required\":[\"number\"]}"));
    }

    @Test
    public void descriptionProviderTest() throws Exception {

        //given
        schemaMapper.setDescriptionProvider(Object::toString);

        //and
        class Demo {
            private int field;

            public int getField() {
                return field;
            }
        }

        //when
        final JSONObject schema = schemaMapper.toJsonSchema4(Demo.class);

        //then
        assertTrue(schema.has("description"));

        assertThat(schema.toString(), equalTo("{\"$schema\":\"http://json-schema.org/draft-04/schema#\",\"description\":\"class pl.zientarski.DocumentationTest$4Demo\",\"additionalProperties\":false,\"type\":\"object\",\"properties\":{\"field\":{\"$ref\":\"int\"}},\"required\":[\"field\"]}"));
    }

    @Test
    public void typeHandlerTest() throws Exception {
        //given
        class DeathStar {
        }

        //and
        schemaMapper.addTypeHandler(new TypeHandler() {
            @Override
            public boolean accepts(Type type) {
                return type.equals(DeathStar.class);
            }

            @Override
            public JSONObject process(TypeDescription typeDescription, MapperContext mapperContext) {
                final JSONObject result = new JSONObject();
                result.put("construction", "completed");
                return result;
            }
        });

        //when
        final JSONObject schema = schemaMapper.toJsonSchema4(DeathStar.class);

        //then
        assertTrue(schema.has("construction"));

        assertThat(schema.toString(), equalTo("{\"construction\":\"completed\"}"));
    }

    @Test(expected = MappingException.class)
    public void withoutSpecifiedGenericParameterTest() throws Exception {
        class Generic<T> {
            private T field;

            public T getField() {
                return field;
            }
        }
        schemaMapper.toJsonSchema4(Generic.class);
    }

    @Test
    public void withSpecifiedGenericParameterTest() throws Exception {
        class Generic<T> {
            private T field;

            public T getField() {
                return field;
            }
        }
        Type type = new ParameterizedTypeReference<Generic<String>>(){}.getType();
        schemaMapper.toJsonSchema4(type);
    }
}
