package com.blueskyminds.freemarkertool.web.actions.mapper;

import junit.framework.TestCase;
import com.blueskyminds.framework.tools.LoggerTools;
import com.opensymphony.xwork2.config.Configuration;
import com.blueskyminds.freemarkertool.web.actions.mapper.PatternMatcherFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.RegExPatternMatcherFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.PatternActionMapper;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionMapConfiguration;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.configuration.MatcherProvider;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.URIMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.ActionMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.DefaultURIMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.DefaultActionMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.action.PlainTextActionNameMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.namespace.PlainTextNamespaceMatcher;
import com.blueskyminds.freemarkertool.web.mapper.matcher.configuration.MockMatcherProvider;
import com.blueskyminds.freemarkertool.web.mapper.configuration.SampleOrginalActionMapConfiguration;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

/**
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class TestPatternActionMapper extends TestCase {

    private Configuration configuration;
    private PatternMatcherFactory factory;
    private MatcherProvider matcherProvider;
    private URIMatcher uriMatcher;
    private ActionMatcher actionMatcher;
    private ActionMapConfiguration actionMapConfiguration;

    protected void setUp() throws Exception {
        super.setUp();
        LoggerTools.configure();

        // setup the PattenMatcherFactory.  The PatternMatcher defines how patterns are compiled and executed
        factory = new RegExPatternMatcherFactory();

        // the URI matcher implementation for matching URI patterns
        uriMatcher = new DefaultURIMatcher(factory);

        //setup the MatcherProvider.  The matchers control how to match an ActionConfig
        matcherProvider = new MockMatcherProvider();
        ((MockMatcherProvider) matcherProvider).addNamespaceMatcher(PlainTextNamespaceMatcher.DEFAULT_NAME, new PlainTextNamespaceMatcher());
        ((MockMatcherProvider) matcherProvider).addActionNameMatcher(PlainTextActionNameMatcher.DEFAULT_NAME, new PlainTextActionNameMatcher());

        actionMatcher = new DefaultActionMatcher(matcherProvider);

        // setup the url-mapping configuration
        actionMapConfiguration = new SampleOrginalActionMapConfiguration();

        configuration = MockConfigurationFactory.createConfiguration();
    }

    /**
     * match an action named 'example' in the /example namespace
     */
    public void testPackages() {
        PatternActionMapper actionMapper = new PatternActionMapper(actionMapConfiguration, uriMatcher, actionMatcher);
        ActionMapping mapping = actionMapper.getMapping(new ParsedURI("get", "/example/example.action", null), configuration);
        
        assertNotNull(mapping);
        assertEquals(MockConfigurationFactory.ACTION1_NAME, mapping.getName());
        assertEquals(MockConfigurationFactory.PACKAGE1_NAMESPACE, mapping.getNamespace());

        assertNull(actionMapper.getMapping(new ParsedURI("get", "/example/acb.action", null), configuration));
        assertNull(actionMapper.getMapping(new ParsedURI("get", "/example/.action", null), configuration));
        assertNull(actionMapper.getMapping(new ParsedURI("get", "/example/example", null), configuration));
        assertNull(actionMapper.getMapping(new ParsedURI("get", "/example/example/example.action", null), configuration));

        assertNotNull(actionMapper.getMapping(new ParsedURI("post", "/example/example.action", null), configuration));

        assertNotNull(actionMapper.getMapping(new ParsedURI("get", "/example.action", null), configuration));
        assertNull(actionMapper.getMapping(new ParsedURI("get", "/example2.action", null), configuration));
        assertNotNull(actionMapper.getMapping(new ParsedURI("get", "/example/example2.action", null), configuration));


    }

}