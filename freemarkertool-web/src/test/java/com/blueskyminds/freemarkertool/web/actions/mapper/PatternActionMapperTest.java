package com.blueskyminds.freemarkertool.web.actions.mapper;

import junit.framework.TestCase;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.impl.MockConfiguration;
import com.blueskyminds.freemarkertool.web.actions.mapper.PatternMatcherFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.RegExPatternMatcherFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.PatternActionMapper;
import com.blueskyminds.freemarkertool.web.actions.Example1Action;
import com.blueskyminds.freemarkertool.web.actions.Example2Action;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

/**
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class PatternActionMapperTest extends TestCase {

    private Configuration configuration;
    private PatternMatcherFactory factory;

    private String DEFAULT_PACKAGE_NAME = "default";
    private String DEFAULT_PACKAGE_NAMESPACE = "/";
    private String PACKAGE1_NAME = "example";
    private String PACKAGE1_NAMESPACE = "/example";
    private String ACTION1_NAME = "example";
    private String ACTION2_NAME = "example2";

    protected void setUp() throws Exception {
        super.setUp();
        factory = new RegExPatternMatcherFactory();
        configuration = new MockConfiguration();

        PackageConfig defaultPackage = new PackageConfig(DEFAULT_PACKAGE_NAME, DEFAULT_PACKAGE_NAMESPACE, false);

        // Example1Action is used in the default package as well as package1.  The only difference is namespace
        ActionConfig default1Action = new ActionConfig();
        default1Action.setClassName(Example1Action.class.getName());
        defaultPackage.addActionConfig(ACTION1_NAME, default1Action);

        PackageConfig example1Package = new PackageConfig(PACKAGE1_NAME, PACKAGE1_NAMESPACE, false);
        ActionConfig example1Action = new ActionConfig();
        example1Action.setClassName(Example1Action.class.getName());
        example1Package.addActionConfig(ACTION1_NAME, example1Action);

        // Example2Action is used only in package1
        ActionConfig example2Action = new ActionConfig();
        example1Action.setClassName(Example2Action.class.getName());
        example1Package.addActionConfig(ACTION2_NAME, example2Action);

        configuration.addPackageConfig(DEFAULT_PACKAGE_NAME, defaultPackage);
        configuration.addPackageConfig(PACKAGE1_NAME, example1Package);
    }

    public void testMapping() {
        PatternActionMapper actionMapper = new PatternActionMapper();
        //actionMapper.setPatternMatcherFactory(factory);

//        ActionMapping mapping = actionMapper.getMapping("get", "/example/example.action", null, configuration);
//        assertNotNull(mapping);
    }

}
