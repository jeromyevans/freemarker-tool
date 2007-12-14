package com.blueskyminds.freemarkertool.web;

import com.blueskyminds.framework.template.FreemarkerTemplateLoader;
import com.blueskyminds.framework.tools.FileTools;
import com.blueskyminds.framework.tools.LoggerTools;
import com.blueskyminds.framework.tools.ResourceLocator;
import com.blueskyminds.framework.tools.text.StringTools;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Tests reading a freemarker template and rendering the output
 *
 * Date Started: 12/12/2007
 * <p/>
 * History:
 */
public class TemplateTest extends TestCase {

    private static final Log LOG = LogFactory.getLog(TemplateTest.class);

    protected void setUp() throws Exception {
        super.setUp();
        LoggerTools.configure();
    }

    public void testFreemarkerTemplate() throws Exception {
        Configuration cfg = new Configuration();
        cfg.setTemplateLoader(new FreemarkerTemplateLoader("templates"));
        Template template = cfg.getTemplate("ftl-1.ftl");
        StringWriter writer = new StringWriter();
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "Jeromy Evans");
        context.put("email", "jeromy.evans@blueskyminds.com.au");
        context.put("message", "This is a test message");
        template.process(context, writer);
        String result = writer.toString();
        LOG.info(result);

        String expectedResult = StringUtils.join(FileTools.readTextFile(ResourceLocator.locateResource("result-1.txt")), ' ');

        assertEquals(expectedResult, StringTools.stripConsecutiveWhitespace(result));
        
    }

    /** The string template loader allows string templates to include other string templates */
    public void testStringTemplateLoader() throws Exception {
        Configuration cfg = new Configuration();
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate("default", "Hello ${name}");
        cfg.setTemplateLoader(templateLoader);

        Template template = cfg.getTemplate("default");

        StringWriter writer = new StringWriter();
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "Jeromy Evans");
        template.process(context, writer);
        String result = writer.toString();

        LOG.info(result);

        assertEquals("Hello Jeromy Evans", StringTools.stripConsecutiveWhitespace(result));

    }
}