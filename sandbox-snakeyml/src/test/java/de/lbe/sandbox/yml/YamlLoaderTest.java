package de.lbe.sandbox.yml;

import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import com.cartelsol.commons.lib.test.junit.AbstractJUnitTest;
import com.cartelsol.commons.lib.test.junit.rules.TestFilesRule;

/**
 * @author lars.beuster
 */
public class YamlLoaderTest extends AbstractJUnitTest {

    @Rule
    public TestFilesRule testFiles = new TestFilesRule();

    /**
     *
     */
    @Test
    public void testWithoutResolving() throws Exception {
        Yaml yml = new Yaml();
        TestBean bean = yml.loadAs(getClass().getResourceAsStream("/test.yml"), TestBean.class);
        assertEquals("${test}", bean.getProperty());
    }

    /**
     * 
     */
    @Test
    public void loadAsMap() throws Exception {
        Yaml yml = new Yaml();
        Map<String, String> properties = yml.loadAs(getClass().getResourceAsStream("/test.yml"), Map.class);
        System.out.println(properties);
    }

    /**
     *
     */
    public static class TestBean {

        private String property;

        public String getProperty() {
            return this.property;
        }

        public void setProperty(String property) {
            this.property = property;
        }
    }
}
