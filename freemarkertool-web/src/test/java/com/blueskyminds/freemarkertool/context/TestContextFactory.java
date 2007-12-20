package com.blueskyminds.freemarkertool.context;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

import com.blueskyminds.freemarkertool.context.ContextFactory;

/**
 * Date Started: 13/12/2007
 * <p/>
 * History:
 */
public class TestContextFactory extends TestCase {

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
    public void testMap() {
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
}
