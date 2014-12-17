package pl.zientarski;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

public class DependenciesTest {
    private SchemaMapper mapper;

    @Before
    public void before() {
        mapper = new SchemaMapper();
    }

    @Test
    public void directTest() throws Exception {
        //given
        class WithDependency {
            private Semaphore semaphore;

            public Semaphore getSemaphore() {
                return semaphore;
            }
        }
        mapper.toJsonSchema4(WithDependency.class);

        //when
        final Iterator<Type> dependenciesIterator = mapper.getDependencies();

        //then
        final ArrayList<Type> dependencies = Lists.newArrayList(dependenciesIterator);
        assertThat(dependencies.size(), equalTo(1));
        assertThat(dependencies, hasItem(Semaphore.class));
    }

    @Test
    public void exceptionTest() throws Exception {
        //given
        class WithExceptionDependency {
            private Exception exception;

            public Exception getException() {
                return exception;
            }
        }

        mapper.toJsonSchema4(WithExceptionDependency.class);

        //when
        final Iterator<Type> dependenciesIterator = mapper.getDependencies();

        //then
        final ArrayList<Type> dependencies = Lists.newArrayList(dependenciesIterator);
        assertThat(dependencies.size(), equalTo(1));
        assertThat(dependencies, hasItem(Exception.class));
    }

    @Test
    public void callTwiceTest() throws Exception {
        //given
        class WithDependency {
            private Semaphore semaphore;

            public Semaphore getSemaphore() {
                return semaphore;
            }
        }

        mapper.toJsonSchema4(WithDependency.class);
        mapper.toJsonSchema4(WithDependency.class);

        //when
        final Iterator<Type> dependenciesIterator = mapper.getDependencies();

        //then
        final ArrayList<Type> dependencies = Lists.newArrayList(dependenciesIterator);
        assertThat(dependencies.size(), equalTo(1));
        assertThat(dependencies, hasItem(Semaphore.class));
    }

    @Test
    public void arrayTest() throws Exception {
        //given
        class WithDependency {
            private Semaphore[] semaphores;

            public Semaphore[] getSemaphores() {
                return semaphores;
            }
        }
        mapper.toJsonSchema4(WithDependency.class);

        //when
        final Iterator<Type> dependenciesIterator = mapper.getDependencies();

        //then
        final ArrayList<Type> dependencies = Lists.newArrayList(dependenciesIterator);
        assertThat(dependencies.size(), equalTo(1));
        assertThat(dependencies, hasItem(Semaphore.class));
    }

    @Test
    public void genericTest() throws Exception {
        //given
        class WithDependency {
            private List<Semaphore> semaphores;

            public List<Semaphore> getSemaphores() {
                return semaphores;
            }
        }

        mapper.toJsonSchema4(WithDependency.class);

        //when
        final Iterator<Type> dependenciesIterator = mapper.getDependencies();

        //then
        final ArrayList<Type> dependencies = Lists.newArrayList(dependenciesIterator);
        assertThat(dependencies.size(), equalTo(1));
        assertThat(dependencies, hasItem(Semaphore.class));
    }

    @Test
    public void nestedTest() throws Exception {
        //given
        class WithDependency {
            private Iterator<Semaphore> semaphores;

            public Iterator<Semaphore> getSemaphores() {
                return semaphores;
            }
        }

        mapper.toJsonSchema4(WithDependency.class);

        //when
        final Iterator<Type> dependenciesIterator = mapper.getDependencies();

        //then
        final ArrayList<Type> dependencies = Lists.newArrayList(dependenciesIterator);
        assertThat(dependencies.size(), equalTo(1));
        assertThat(dependencies, hasItem(WithDependency.class.getMethod("getSemaphores").getGenericReturnType()));
    }

    @Test
    public void nestedResolutionTest() throws Exception {
        //given
        class HamsterWrapper<T> {
            private T hamster;

            public T getHamster() {
                return hamster;
            }

        }

        class Dependency {
            private int size;

            public int getSize() {
                return size;
            }

        }

        class WithDependency {
            private HamsterWrapper<Dependency> dependencies;

            public HamsterWrapper<Dependency> getDependencies() {
                return dependencies;
            }

        }

        mapper.toJsonSchema4(WithDependency.class);
        mapper.toJsonSchema4(Lists.newArrayList(mapper.getDependencies()).get(0));

        //when
        final Iterator<Type> dependenciesIterator = mapper.getDependencies();

        //then
        final ArrayList<Type> dependencies = Lists.newArrayList(dependenciesIterator);
        assertThat(dependencies.size(), equalTo(2));
        assertThat(dependencies, hasItem(Dependency.class));
        assertThat(dependencies, hasItem(WithDependency.class.getMethod("getDependencies").getGenericReturnType()));
    }

}
