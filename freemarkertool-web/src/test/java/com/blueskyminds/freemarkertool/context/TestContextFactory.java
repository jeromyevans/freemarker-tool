package com.blueskyminds.freemarkertool.context;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.blueskyminds.freemarkertool.context.ContextFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 13/12/2007
 * <p/>
 * History:
 */
public class TestContextFactory extends TestCase {

    private static final Log LOG = LogFactory.getLog(TestContextFactory.class);

    private Map<String, String> context;
    private ContextFactory contextFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = new HashMap<String, String>();
        contextFactory = new ContextFactory();
    }

    /** Really simple text - puts some strings into the root object */
    public void testSimpleProperties() {
        context.put("test1", "a");
        context.put("test2", "b");
        context.put("test3", "c");

        Map<String, Object> result = contextFactory.createContext(context);

        assertEquals(3, result.size());
        assertEquals("a", result.get("test1"));
        assertEquals("b", result.get("test2"));
        assertEquals("c", result.get("test3"));
    }

    /** Create a map with two root objects.  The second is a map with two entries */
    public void testHash() {
        context.put("test1", "a");
        context.put("test2.one", "b");
        context.put("test2.two", "c");

        Map<String, Object> result = contextFactory.createContext(context);

        assertEquals(2, result.size());
        assertEquals("a", result.get("test1"));
        Map<String, Object> test2 = (Map<String, Object>) result.get("test2");
        assertEquals("b", test2.get("one"));
        assertEquals("c", test2.get("two"));
    }

    /** Create a map with a boolean value.  */
    public void testBoolean() {
        context.put("test1", "a");
        context.put("test2.one", "b");
        context.put("test2.two", "true");

        Map<String, Object> result = contextFactory.createContext(context);

        assertEquals(2, result.size());
        assertEquals("a", result.get("test1"));
        Map<String, Object> test2 = (Map<String, Object>) result.get("test2");
        assertEquals("b", test2.get("one"));
        assertEquals(true, test2.get("two"));
    }

    private void assertSplitEquals(String left, String right, String expression) {
        LOG.info(expression);
        String paths[] = contextFactory.splitExpression(expression);
        assertEquals("left :"+left,  "left :"+paths[0]);
        assertEquals("right:"+right, "right:"+paths[1]);
    }

    public void testSplitExpression() {
        assertSplitEquals("test1", null, "test1");
        assertSplitEquals("test1", "one", "test1.one");
        assertSplitEquals("test1", "[one]", "test1[one]");
        assertSplitEquals("test1", "[one].two", "test1[one].two");
        assertSplitEquals("test1", "one.two", "test1.one.two");
        assertSplitEquals("test1", "[\"one\"].two", "test1[\"one\"].two");
        assertSplitEquals("test1", "[\"one\"].two", "test1[\"one\"].two");
        assertSplitEquals("one", "two", "[one].two");
        assertSplitEquals("one", "two", "[\"one\"].two");
        assertSplitEquals("1", "two", "[1].two");
    }

    /** Create a sequence */
    public void testSequence() {
        context.put("test[1]", "a");
        context.put("test[2]", "b");
        context.put("test[3].one", "c");
        context.put("test[3].two", "true");

        Map<String, Object> result = contextFactory.createContext(context);

        assertEquals(1, result.size());
        List test = (List) result.get("test");
        assertEquals("a", test.get(1));
        Map<String, Object> test2 = (Map<String, Object>) test.get(3);
        assertEquals("c", test2.get("one"));
        assertEquals(true, test2.get("two"));
    }

    public void testHashAlternative() {
        context.put("test1", "a");
        context.put("test2[one]", "b");
        context.put("test2[\"two\"]", "c");

        Map<String, Object> result = contextFactory.createContext(context);

        assertEquals(2, result.size());
        assertEquals("a", result.get("test1"));
        Map<String, Object> test2 = (Map<String, Object>) result.get("test2");
        assertEquals("b", test2.get("one"));
        assertEquals("c", test2.get("two"));
    }

    public void testMixedHashAndSequence() {
        context.put("test1", "a");
        context.put("test2[list][1]", "b");
        context.put("test2[list][2]", "c");
        context.put("test2[two]", "d");

        Map<String, Object> result = contextFactory.createContext(context);

        assertEquals(2, result.size());
        assertEquals("a", result.get("test1"));
        Map<String, Object> test2 = (Map<String, Object>) result.get("test2");
        List list = (List) test2.get("list");
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
        assertEquals("d", test2.get("two"));
    }

    public void testMixedHashAndSequence2() {
        context.put("test1", "a");
        context.put("test2[list][1].name", "b");
        context.put("test2[list][2].name", "c");
        context.put("test2[two]", "d");

        Map<String, Object> result = contextFactory.createContext(context);

        assertEquals(2, result.size());
        assertEquals("a", result.get("test1"));
        Map<String, Object> test2 = (Map<String, Object>) result.get("test2");
        List list = (List) test2.get("list");
        assertEquals("b", ((Map) list.get(1)).get("name"));
        assertEquals("c", ((Map) list.get(2)).get("name"));
        assertEquals("d", test2.get("two"));
    }

    /**
     * In this test, we make the factory think we're writing to a sequence, but then we address it by
     *  a named element.  It should convert the assumed sequence to a hash
     */
    public void testRefactorFromSequenceToMap() {
        context.put("test[1].name", "a");
        context.put("test[2].name", "b");

        // looks like a sequence
        Map<String, Object> result1 = contextFactory.createContext(context);

        assertTrue(List.class.isAssignableFrom(result1.get("test").getClass()));
        assertEquals(3, ((List) result1.get("test")).size());  // note one entry is null

        // but it's not any more...
        context.put("test[three].name", "c");

        Map<String, Object> result2 = contextFactory.createContext(context);

        assertTrue(Map.class.isAssignableFrom(result2.get("test").getClass()));
        assertEquals(3, ((Map) result2.get("test")).size());
    }
}
