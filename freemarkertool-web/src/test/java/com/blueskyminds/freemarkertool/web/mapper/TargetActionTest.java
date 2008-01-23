package com.blueskyminds.freemarkertool.web.mapper;

import junit.framework.TestCase;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.impl.MockConfiguration;
import com.blueskyminds.freemarkertool.web.actions.Example1Action;
import com.blueskyminds.freemarkertool.web.actions.Example2Action;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionSelector;
import com.blueskyminds.freemarkertool.web.actions.mapper.RegExPatternMatcherFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.PatternMatcherFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.ActionMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.DefaultActionMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.action.PlainTextActionNameMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.namespace.PlainTextNamespaceMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.configuration.MatcherProvider;
import com.blueskyminds.freemarkertool.web.mapper.matcher.configuration.MockMatcherProvider;

/**
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class TargetActionTest extends TestCase {

    private Configuration configuration;
    private PatternMatcherFactory factory;
    private MatcherProvider matcherProvider;

    private String DEFAULT_PACKAGE_NAME = "default";
    private String DEFAULT_PACKAGE_NAMESPACE = "/";
    private String PACKAGE1_NAME = "example";
    private String PACKAGE1_NAMESPACE = "/example";
    private String ACTION1_NAME = "example";
    private String ACTION2_NAME = "example2";
    private static final String PLAIN_TEXT = "plainText";

    protected void setUp() throws Exception {
        super.setUp();
        // setup the PattenMatcherFactory.  The PatternMatcher defines how patterns are compiled and executed
        factory = new RegExPatternMatcherFactory();               

        //setup the MatcherProvider.  The matchers control how to match an ActionConfig
        matcherProvider = new MockMatcherProvider();
        ((MockMatcherProvider) matcherProvider).addNamespaceMatcher(PLAIN_TEXT, new PlainTextNamespaceMatcher());
        ((MockMatcherProvider) matcherProvider).addActionNameMatcher(PLAIN_TEXT, new PlainTextActionNameMatcher());

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

    /**
     * match an action named 'example' in the /example namespace
     */
    public void testPackages() {
        ActionSelector actionSelector = new ActionSelector("/example", "example", "execute");
        ActionMatcher actionMatcher = new DefaultActionMatcher();
//        ActionMapping mapping = actionSelector.match(uriMatch, configuration);
//        assertNotNull(mapping);
//        assertEquals(ACTION1_NAME, mapping.getName());
//        assertEquals(PACKAGE1_NAMESPACE, mapping.getNamespace());
    }

    /**
     * match an action named 'example' in the / namespace
     */
    public void testPackages2() {
        ActionSelector targetAction = new ActionSelector("^/$", "example", "execute");
        ActionMatcher actionMatcher = new DefaultActionMatcher();
//        ActionMapping mapping = targetAction.match(uriMatch, configuration);
//        assertNotNull(mapping);
//        assertEquals(ACTION1_NAME, mapping.getName());
//        assertEquals(DEFAULT_PACKAGE_NAMESPACE, mapping.getNamespace());
    }
}
