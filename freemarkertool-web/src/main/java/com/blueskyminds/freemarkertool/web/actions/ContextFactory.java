package com.blueskyminds.freemarkertool.web.actions;

import org.apache.commons.lang.StringUtils;

import java.util.*;


/**
 * Converts name=value pairs into an object graph (of maps)
 *
 * eg. "parameter.id"="val" is { "parameter": { "id": "val" }}
 *
 * Date Started: 13/12/2007
 * <p/>
 * History:
 */
public class ContextFactory {

    /** Converts the context defined by name=value pairs into an object graph of maps (todo: and collections) */
    public Map<String, Object> createContext(Map<String, String> context) {
        String expression;
        Map<String, Object> root = new HashMap<String, Object>();
        if (context != null) {
            for (Map.Entry<String, String> entry : context.entrySet()) {
                expression = entry.getKey();
                processExpression(expression, root, entry.getValue());
            }
        }
        return root;
    }

    /** Converts the context defined by beans into an object graph of maps (todo: and collections) */
    public Map<String, Object> createContext(List<TemplateContextBean> context) {
        String expression;
        Map<String, Object> root = new HashMap<String, Object>();
        if (context != null) {
            for (TemplateContextBean entry : context) {
                if (entry.isEnabled()) {
                    expression = entry.getName();
                    if (!entry.isNullValue()) {
                        processExpression(expression, root, entry.getValue());
                    } else {
                        processExpression(expression, root, null);
                    }
                }
            }
        }
        return root;
    }

    /**
     * Process an expression by:
     *   splitting it into up to two parts based on the location of the dot '.'
     *   If there's only one part left of the dot, it uses this key to set a value in this node
     *   If there's two parts, a new map is created using the key, and the text after the dot is
     *     processed as an expression for that map (recursive)
     *
     * @param expression
     * @param node
     * @param value
     */
    private void processExpression(String expression, Map<String, Object> node, String value) {
        String key;
        String subExpression = null;
        Object child;
        if (expression != null) {
            String[] paths = StringUtils.split(expression, ".", 2);

            if (paths.length > 0) {
                key = paths[0];
                if (StringUtils.isNotBlank(key)) {
                    if (paths.length == 2) {
                        subExpression = paths[1];
                    }

                    if (StringUtils.isBlank(subExpression)) {
                        // set this value
                        setValue(node, key, value);
                    } else {
                        child = node.get(key);
                        if (!isMap(child)) {
                            // create/replace child context
                            child = (Map<String, Object>) createChildNode(key, node, ContextNodeType.Map);
                        }
                        processExpression(subExpression, (Map<String, Object>) child, value);
                    }
                }
            }
        }
    }

    private void setValue(Map<String, Object> node, String key, String value) {
        node.put(key, value);
    }

    /** Determine whether the specified object is a Map not not */
    private boolean isMap(Object node) {
        if (node != null) {
            return Map.class.isAssignableFrom(node.getClass());
        } else {
            return false;
        }
    }

    /**
     * Creates a new node that can contain other values
     * @param key
     * @param parent
     * @param type
     */
    private Object createChildNode(String key, Map<String, Object> parent, ContextNodeType type) {
        Object node = null;
        
        switch (type) {
            case List:
                node = new LinkedList();
                break;
            case Map:
                node = new HashMap();
                break;
            case Set:
                node = new HashSet();
                break;
        }

        parent.put(key, node);

        return node;
    }
}
