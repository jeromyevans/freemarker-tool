package com.blueskyminds.freemarkertool.services;

import com.blueskyminds.framework.tools.FileTools;
import com.blueskyminds.framework.tools.ResourceLocator;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Date Started: 12/12/2007
 * <p/>
 * History:
 */
public class TestFreemakerTemplateService extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestFreemakerTemplateService.class);

    private TemplateService templateService;
    private Map<String, Object> context;
    private static final String TEMPLATE_PATH = "templates/";

    private String loadTemplate(String name) throws IOException {
        return new String(FileTools.readInputStream(ResourceLocator.locateResource(TEMPLATE_PATH+name).toURL().openStream()));
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        templateService = new FreemarkerTemplateServiceImpl();
        context = new HashMap<String, Object>();
    }

    public void testMergeTemplate() throws Exception {
        String open = loadTemplate("open-1.ftl");
        String close = loadTemplate("close-1.ftl");
        String body = "body-text";

        String result = templateService.mergeTemplate(context, open, close, body, true);
        LOG.info(result);
    }

}
