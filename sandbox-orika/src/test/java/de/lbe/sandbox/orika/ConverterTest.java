package de.lbe.sandbox.orika;

import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

/**
 * @author lbeuster
 */
public class ConverterTest extends AbstractOrikaTest {

    /**
     *
     */
    @Test
    public void testCopyStringListPropertyToComplexListProperty() {

        class TestConverter extends CustomConverter<String, ComplexBean> {

            static final String KEY = "TestConverter";

            @Override
            public ComplexBean convert(String source, Type<? extends ComplexBean> destinationType, MappingContext mappingContext) {
                return new ComplexBean(source);
            }
        }

        String value1 = "hallo";

        // prepare the objects
        TestBeanWithStringListProperty source = new TestBeanWithStringListProperty();
        source.setListProperty(Collections.singletonList(value1));
        TestBeanWithComplexListProperty target = new TestBeanWithComplexListProperty();

        // prepare
        MapperFactory mapperFactory = strictMapperFactory();
        mapperFactory.getConverterFactory().registerConverter(TestConverter.KEY, new TestConverter());
        mapperFactory.classMap(TestBeanWithStringListProperty.class, TestBeanWithComplexListProperty.class).fieldMap("listProperty").converter(TestConverter.KEY).add().register();

        // copy
        mapperFactory.getMapperFacade(TestBeanWithStringListProperty.class, TestBeanWithComplexListProperty.class, false).map(source, target);

        // assert
        List<ComplexBean> targetList = target.getListProperty();
        assertNotSame(source.getListProperty(), target.getListProperty());
        assertThat(targetList, hasSize(1));
        assertEquals(value1, targetList.get(0).getValue());
    }

    /**
     * 
     */
    public class TestBeanWithStringListProperty {

        private List<String> listProperty = new ArrayList<>();

        public List<String> getListProperty() {
            return this.listProperty;
        }

        public void setListProperty(List<String> listProperty) {
            this.listProperty = listProperty;
        }
    }

    /**
     * 
     */
    public class TestBeanWithComplexListProperty {

        private List<ComplexBean> listProperty = new ArrayList<>();

        public List<ComplexBean> getListProperty() {
            return this.listProperty;
        }

        public void setListProperty(List<ComplexBean> listProperty) {
            this.listProperty = listProperty;
        }
    }

    /**
     * 
     */
    public class ComplexBean {

        private String value;

        public ComplexBean(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
