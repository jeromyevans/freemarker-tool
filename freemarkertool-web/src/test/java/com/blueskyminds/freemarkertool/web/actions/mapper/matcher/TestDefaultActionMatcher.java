package com.blueskyminds.freemarkertool.web.actions.mapper.matcher;

import junit.framework.TestCase;
import com.blueskyminds.freemarkertool.web.mapper.matcher.configuration.MockMatcherProvider;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.namespace.PlainTextNamespaceMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.action.PlainTextActionNameMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.configuration.MatcherProvider;
import com.blueskyminds.freemarkertool.web.actions.mapper.MockConfigurationFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionSelector;
import com.opensymphony.xwork2.config.Configuration;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

/**
 * Runs some tests through the DefaultActionMatcher
 *
 * Date Started: 29/01/2008
 * <p/>
 * History:
 */
public class TestDefaultActionMatcher extends TestCase {

    private MatcherProvider matcherProvider;
    private ActionMatcher actionMatcher;
    private Configuration configuration;

    protected void setUp() throws Exception {
        super.setUp();

        //setup the MatcherProvider.  The matchers control how to match an ActionConfig
        matcherProvider = new MockMatcherProvider();
        ((MockMatcherProvider) matcherProvider).addNamespaceMatcher(PlainTextNamespaceMatcher.DEFAULT_NAME, new PlainTextNamespaceMatcher());
        ((MockMatcherProvider) matcherProvider).addActionNameMatcher(PlainTextActionNameMatcher.DEFAULT_NAME, new PlainTextActionNameMatcher());

        configuration = MockConfigurationFactory.createConfiguration();

        actionMatcher = new DefaultActionMatcher(matcherProvider);
    }

    public void testActionMatcher() {
        MatchContext matchContext = new MatchContext();
        matchContext.put("path", "/example");
        matchContext.put("name", "example");

        ActionSelector actionSelector = new ActionSelector("${path}", "${name}", "execute");

        ActionMapping actionMapping = actionMatcher.match(actionSelector, matchContext, configuration);
        assertNotNull(actionMapping);
    }
}
