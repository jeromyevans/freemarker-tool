package com.blueskyminds.freemarkertool.web.actions.mapper.matcher;

import junit.framework.TestCase;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.URIPattern;
import com.blueskyminds.freemarkertool.web.actions.mapper.*;

/**
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class URIPatternTest extends TestCase {

    private PatternMatcher methodMatcher;
    private PatternMatcher pathMatcher;
    private DefaultURIMatcher uriMatcher;

    protected void setUp() throws Exception {
        super.setUp();
        //factory = new RegExPatternMatcherFactory();

//        uriMatcher = new DefaultURIMatcher(new RegExPatternMatcher(), new RegExPatternMatcher());
    }

  /*  public void testMatch() {
        URIPattern uriPattern = new URIPattern("get", ".*");

        assertTrue(uriMatcher.matchesMethod("get", uriPattern));
        assertTrue(uriMatcher.matchesMethod("GET"));
        assertFalse(uriMatcher.matchesMethod("post"));
        assertFalse(uriMatcher.matchesMethod(null));

        assertNotNull(uriMatcher.matchesPath(new ParsedURI("/"), null));
        assertNotNull(uriMatcher.matchesPath(""));
        assertNotNull(uriMatcher.matchesPath("/example"));
        assertNotNull(uriMatcher.matchesPath("/example/"));
    }

    public void testMatchMethods() {
       URIPattern uriPattern = new URIPattern("get|post", ".*", factory);
       assertTrue(uriPattern.matchesMethod("get"));
       assertTrue(uriPattern.matchesMethod("GET"));
       assertTrue(uriPattern.matchesMethod("post"));
       assertFalse(uriPattern.matchesMethod(null));
   }


    public void testMatchPath() {
        URIPattern uriPattern = new URIPattern("get", "/example/.+", factory);

        assertFalse(uriPattern.matchesPath("/"));
        assertFalse(uriPattern.matchesPath(""));
        assertFalse(uriPattern.matchesPath("/example"));
        assertFalse(uriPattern.matchesPath("/example/"));
        assertTrue(uriPattern.matchesPath("/example/action"));
    }*/
}
