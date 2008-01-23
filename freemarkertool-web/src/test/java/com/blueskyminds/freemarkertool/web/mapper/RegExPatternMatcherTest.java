package com.blueskyminds.freemarkertool.web.mapper;

import junit.framework.TestCase;
import com.blueskyminds.freemarkertool.web.actions.mapper.PatternMatcherFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.RegExPatternMatcherFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.PatternMatcher;

import java.util.List;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Unit tests for the default RegEx PatternMatcher
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class RegExPatternMatcherTest extends TestCase {

    private static final Log LOG = LogFactory.getLog(RegExPatternMatcherTest.class);

    private PatternMatcherFactory factory;

    protected void setUp() throws Exception {
        super.setUp();
        factory = new RegExPatternMatcherFactory();
    }

    /** Match everything - no groups */
    public void testMatchNotNull() {
        PatternMatcher matcher = factory.create(".*", false);
        assertNotNull(matcher.matches(""));
        assertEquals(1, matcher.matches("").size());
        assertEquals(1, matcher.matches("abc").size());
        assertEquals(0, matcher.matches(null).size());
    }

    /** Null pattern always succeeds */
    public void testMatchAll() {
        PatternMatcher matcher = factory.create(null, false);
        assertNotNull(matcher.matches(""));
        assertEquals(1, matcher.matches("").size());
        assertEquals(1, matcher.matches("abc").size());
        assertEquals(1, matcher.matches(null).size());
    }

    /** A commonly used path */
    public void testMatchPath() {
        PatternMatcher matcher = factory.create("/example/.+", false);
        assertEquals(0, matcher.matches("").size());  // no match
        assertEquals(0, matcher.matches("/").size());  // no match
        assertEquals(0, matcher.matches("/example").size());  // no match
        assertEquals(0, matcher.matches("/example/").size());  // no match
        assertEquals(1, matcher.matches("/example/action").size()); // match
        assertEquals(1, matcher.matches("/example/path/").size()); // match
        assertEquals(1, matcher.matches("/example/path/action").size()); // match
    }

    /** Another commonly used path */
    public void testMatchPath2() {
        PatternMatcher matcher = factory.create("/example/.*", false);
        assertEquals(0, matcher.matches("").size());  // no match
        assertEquals(0, matcher.matches("/").size());  // no match
        assertEquals(0, matcher.matches("/example").size());  // no match
        assertEquals(1, matcher.matches("/example/").size());  // match
        assertEquals(1, matcher.matches("/example/action").size());  // match
        assertEquals(1, matcher.matches("/example/path/").size()); // match
        assertEquals(1, matcher.matches("/example/path/action").size()); // match
    }

    private String toString(String[] array) {
        boolean first = true;
        if (array != null) {
            StringBuilder sb = new StringBuilder("[");

            for (String value : array) {
                if (!first) {
                    sb.append(",");
                } else {
                    first = false;
                }
                sb.append(value);
            }
            sb.append("]");
            return sb.toString();
        } else {
            return null;
        }
    }
    private String toString(Collection<String> collection) {
        boolean first = true;
        if (collection != null) {
            StringBuilder sb = new StringBuilder("[");
            for (String aCollection : collection) {
                if (!first) {
                    sb.append(",");
                } else {
                    first = false;
                }
                sb.append(aCollection);
            }
            sb.append("]");
            return sb.toString();
        } else {
            return null;
        }
    }

    private boolean groupsMatch(List<String> groupResults, String[] groupValues) {

        if (groupResults == null) {
            LOG.error("groupResults is null");
            return false;
        }

        if (groupValues == null) {
            LOG.error("groupValues is null");
            return false;
        }

        if (groupResults.size() != groupValues.length) {
            LOG.error("groupResults contains "+groupResults.size()+" values, groupValues contains "+groupValues.length);
            LOG.error("groupResults: "+toString(groupResults));
            LOG.error("groupValues: "+toString(groupValues));
            return false;
        }

        boolean okay = true;
        int index = 0;
        for (String groupResult : groupResults) {
            if (groupResult != null) {
                if (!groupResult.equals(groupValues[index])) {
                    okay = false;
                    break;
                }
            } else {
                if (groupValues[index] != null) {
                    okay = false;
                    break;
                }
            }
            index++;
        }

        if (!okay) {
            LOG.error("groupResults do not match groupValues:");
            LOG.error("groupResults: "+toString(groupResults));
            LOG.error("groupValues: "+toString(groupValues));
        }
        return okay;
    }

    /** Matches any path containing .action */
    public void testGroup1() {
        PatternMatcher matcher = factory.create("(.*)/{0,1}(.*)\\.action$", false);
        assertEquals(0, matcher.matches("").size());  // no match
        assertEquals(0, matcher.matches("/").size());  // no match
        assertEquals(0, matcher.matches("/example").size());  // no match
        assertEquals(0, matcher.matches("/example/").size());  // no match
        assertTrue(groupsMatch(matcher.matches("example.action"), new String[] {"example.action", "", "example"}));
        assertTrue(groupsMatch(matcher.matches(".action"), new String[] {".action", "", ""}));
        assertTrue(groupsMatch(matcher.matches("/.action"), new String[] {"/.action", "/", ""}));
        assertTrue(groupsMatch(matcher.matches("/example.action"), new String[] {"/example.action", "/", "example"}));
        assertTrue(groupsMatch(matcher.matches("/example/.action"), new String[] {"/example/.action", "/example/", ""}));
        assertTrue(groupsMatch(matcher.matches("/example/path/example.action"), new String[] {"/example/path/example.action", "/example/path/", "example"}));
        assertTrue(groupsMatch(matcher.matches("/example/path/.action"), new String[] {"/example/path/.action", "/example/path/", ""}));
    }
}
