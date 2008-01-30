package com.blueskyminds.freemarkertool.web.actions.mapper.matcher;

import junit.framework.TestCase;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.URIPattern;
import com.blueskyminds.freemarkertool.web.actions.mapper.*;

/**
 * Runs some unit tests through the DefaultURIMatcher
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class TestDefaultURIMatcher extends TestCase {

    private PatternMatcherFactory factory;
    private DefaultURIMatcher uriMatcher;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new RegExPatternMatcherFactory();

        uriMatcher = new DefaultURIMatcher(factory);
    }

    public void testMatch() {
        URIPattern uriPattern = new URIPattern("1", "get", ".*");

        assertTrue(uriMatcher.matchesMethod(new ParsedURI("get", "/", null), uriPattern));
        assertFalse(uriMatcher.matchesMethod(new ParsedURI("GET", "/", null), uriPattern));
        assertFalse(uriMatcher.matchesMethod(new ParsedURI("post", "/", null), uriPattern));
        assertFalse(uriMatcher.matchesMethod(new ParsedURI(null, "/", null), uriPattern));

        assertNotNull(uriMatcher.matchesPath(new ParsedURI("get", "/", null), uriPattern));
        assertNotNull(uriMatcher.matchesPath(new ParsedURI("get", "", null), uriPattern));
        assertNotNull(uriMatcher.matchesPath(new ParsedURI("get", "/example", null), uriPattern));
        assertNotNull(uriMatcher.matchesPath(new ParsedURI("get", "/example/", null), uriPattern));
    }

}
