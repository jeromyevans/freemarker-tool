package com.blueskyminds.freemarkertool.examples;

import junit.framework.TestCase;
import com.blueskyminds.freemarkertool.examples.ExampleBean;
import com.blueskyminds.freemarkertool.examples.ExampleLoader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 18/12/2007
 * <p/>
 * History:
 */
public class TestExampleLoader extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestExampleLoader.class);

    /** Simply creates a hello world bean and converts it to XML - shows the schema */
    public void testWriteXML() {
        ExampleBean exampleBean = ExampleLoader.createHelloWorld();
        String xml = ExampleLoader.toXML(exampleBean);
        LOG.info(xml);
    }

    public void testReadHelloWorld() throws Exception {
        ExampleLoader exampleLoader = new ExampleLoader();
        ExampleBean bean = exampleLoader.loadExample("hello-world.xml");
        assertNotNull(bean);
    }

    public void testReadStruts2Div() throws Exception {
        ExampleLoader exampleLoader = new ExampleLoader();
        ExampleBean bean = exampleLoader.loadExample("struts2-div.xml");
        assertNotNull(bean);
    }

    public void testReadStruts2Checkbox() throws Exception {
        ExampleLoader exampleLoader = new ExampleLoader();
        ExampleBean bean = exampleLoader.loadExample("struts2-checkbox.xml");
        assertNotNull(bean);
    }
}
