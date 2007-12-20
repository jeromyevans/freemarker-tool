package com.blueskyminds.freemarkertool.examples;

import com.thoughtworks.xstream.XStream;
import com.blueskyminds.framework.tools.ResourceLocator;
import com.blueskyminds.framework.tools.ResourceTools;
import com.blueskyminds.freemarkertool.context.TemplateContextBean;

import java.net.URI;
import java.io.IOException;

/**
 * Users to load examples from XML
 *
 * Date Started: 18/12/2007
 * <p/>
 * History:
 */
public class ExampleLoader {

    private XStream xStream;

    private static final String EXAMPLES_PATH = "examples/";

    /** Ordered list of examples that can be referenced by index */
    private static final String[] EXAMPLES = {
        "",
        "hello-world.xml",
        "struts2-div.xml",
        "struts2-checkbox.xml"
         };

    public ExampleLoader() {
        this.xStream = setupXStream();
    }

    /** Load the ExampleBean with the specified name exactly */
    public ExampleBean loadExample(String name) throws IOException {
        ExampleBean exampleBean = null;
        URI uri = ResourceLocator.locateResource(EXAMPLES_PATH + name);
        if (uri != null) {
            exampleBean = (ExampleBean) xStream.fromXML(ResourceTools.openStream(uri));
        }
        return exampleBean;
    }

    /**
     *  Load the ExampleBean with the specified ID.
     **/
    public ExampleBean loadExample(int index) throws IOException {
        if ((index > 0) && (index < EXAMPLES.length)) {
            return loadExample(EXAMPLES[index]);
        } else {
            return null;
        }
    }

    /** Setup xstream with the aliases and converters */
    private static XStream setupXStream() {
        XStream xStream =  new XStream();
        xStream.alias("example", ExampleBean.class);
        xStream.addImplicitCollection(ExampleBean.class, "context");
        xStream.useAttributeFor(ExampleBean.class, "templateView");
        xStream.alias("contextField", TemplateContextBean.class);
        xStream.useAttributeFor(TemplateContextBean.class, "enabled");
        xStream.useAttributeFor(TemplateContextBean.class, "name");
        xStream.useAttributeFor(TemplateContextBean.class, "value");
        xStream.useAttributeFor(TemplateContextBean.class, "nullValue");
        return xStream;
    }

    /** Prepares a simple hello world example */
    public static ExampleBean createHelloWorld() {
        ExampleBean exampleBean = new ExampleBean();
        exampleBean.setTemplateView(true);
        exampleBean.setOpenTemplate("Hello ${firstName}, we hope you find this tool helpful.");
        TemplateContextBean item = new TemplateContextBean();
        item.setEnabled(true);
        item.setName("firstName");
        item.setNullValue(false);
        item.setValue("New User");
        exampleBean.addContextItem(item);
        return exampleBean;
    }

    /** Convert an instance into an XML representation */
    public static String toXML(ExampleBean exampleBean) {
        XStream xStream = setupXStream();
        return xStream.toXML(exampleBean);
    }
}
