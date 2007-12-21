package com.blueskyminds.freemarkertool.context;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.regex.Pattern;

import com.blueskyminds.framework.tools.text.StringTools;


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

    private static final Log LOG = LogFactory.getLog(ContextFactory.class);

    private static final Pattern SEQUENCE_PATTERN = Pattern.compile("(.*)\\[(.+)\\]");

    /** Converts the context defined by name=value pairs into an object graph of maps (todo: and collections) */
    public Map<String, Object> createContext(Map<String, String> context) {
        String expression;
        Map<String, Object> root = new HashMap<String, Object>();
        if (context != null) {
            for (Map.Entry<String, String> entry : context.entrySet()) {
                expression = entry.getKey();
                processExpression(expression, root, entry.getValue(), null);
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
                    if (StringUtils.isNotBlank(expression)) {
                        if (!entry.isNullValue()) {
                            processExpression(expression, root, entry.getValue(), null);
                        } else {
                            processExpression(expression, root, null, null);
                        }
                    }
                }
            }
        }
        return root;
    }

    /** Trims leading . */
    private String trimExpression(String expression) {
        String result = expression;
        while (result.startsWith(".")) {
            result = result.substring(1);
        } 
        if (StringUtils.isNotBlank(result)) {
            return result;
        } else {
            return null;
        }
    }
    /**
     * Split the expression into two parts where the first part is the first expression and the
     *  second is the remainder
     *
     * @param expression
     * @return
     */
    protected String[] splitExpression(String expression) {
        String left;
        String right = null;

        expression = trimExpression(expression);

        int dotSeparator = expression.indexOf(".");
        int leftBracket = expression.indexOf("[");
        int rightBracket = expression.indexOf("]");
        int leftStart = -1;
        int leftEnd = -2;
        int rightStart = -1;

        if (leftBracket == 0) {
            if (rightBracket >= 0) {
                leftStart = 1;
                leftEnd = rightBracket-1;
                rightStart = rightBracket+1;
            } else {
                // invalid
                leftStart = -1;
            }
        } else {
            leftStart = 0;
            if (dotSeparator >= 0) {
                if (leftBracket >= 0) {
                    leftEnd = Math.min(dotSeparator, leftBracket)-1;
                } else {
                    leftEnd = dotSeparator-1;
                }
                rightStart = leftEnd+1;
            } else {
                if (leftBracket >= 0) {
                    leftEnd = leftBracket-1;
                    rightStart = leftBracket;
                } else {
                    left = expression;
                    leftEnd = expression.length()-1;
                    rightStart = -1;
                }
            }
        }
        leftStart = Math.max(0, leftStart);
        leftEnd = Math.max(leftStart-1, Math.min(leftEnd, expression.length()-1));
                        
        left = StringTools.stripQuotes(expression.substring(leftStart, leftEnd+1));
        if (rightStart >= 0) {
            if (rightStart < expression.length()) {
                right = trimExpression(expression.substring(rightStart, expression.length()));
            }
        }
        return new String[]{left, right};
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
    private void processExpression(String expression, Object node, String value, Object parent) {
        String[] paths;
        String key;
        String subExpression;
        Object child;
        if (expression != null) {            
            paths = splitExpression(expression);
            key = paths[0];
            subExpression = paths[1];

            if (StringUtils.isNotBlank(key)) {
                if (StringUtils.isBlank(subExpression)) {
                    // set the scalar value of this node
                    setValue(node, key, value);
                } else {
                    // lookup or create the child node
                    child = getChildNode(node, key, subExpression, parent);
                    processExpression(subExpression, child, value, node);
                }
            }
        }
    }

    /**
     * Determines whether an expression starts with an index into an array
     *
     * @param expression
     * @return
     */
    private boolean isArrayIndex(String expression) {
        String[] parts = splitExpression(expression);
        return StringUtils.isNumeric(parts[0]);
    }

    /**
     * Transfers entries in a sequence into a hash.
     * If the value is null it is not transferred
     **/
    private Map copyToHash(List sequence) {
        Map<String, Object> hash = new HashMap<String, Object>();
        Object value;
        for (int index = 0; index < sequence.size(); index++) {
            value = sequence.get(index);
            if (value != null) {
                hash.put(Integer.toString(index), value);
            }
        }
        return hash;
    }

    /** Converts the specified Sequence to a hash with keys equal to the existing indexes */
    private Map transformSequenceToHash(Object parent, List sequence) {
        Map newHash = null;

        if (isHash(parent)) {
            Map hash = (Map) parent;
            String key = null;

            // reverse lookup of the entry in the parent hash...
            for (Map.Entry entry : (Set<Map.Entry>) hash.entrySet()) {
                if (entry.getValue() == sequence) {   // instance comparison
                    key = (String) entry.getKey();
                    break;
                }
            }

            if (key != null) {
                newHash = copyToHash(sequence);
                // replace the sequence with the equivalent hash
                hash.put(key, newHash);
            } else {
                LOG.error("Attempted to transform a Sequence to a Hash but the sequence instance wasn't found in the parent hash");
            }
        } else {
            if (isSequence(parent)) {
                List list = (List) parent;
                int index = -1;

                // reverse lookup of the entry in the parent list...
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i) == sequence) {    // instance comparison
                        index = i;
                        break;
                    }
                }

                if (index >= 0) {
                    newHash = copyToHash(sequence);
                    // replace the sequence with the equivalent list
                    list.set(index, newHash);
                } else {
                    LOG.error("Attempted to transform a Sequence to a Hash but the sequence instance wasn't found in the parent sequence");
                }
            }
        }
        return newHash;
    }

    /**
     * Automatically typecasts to boolean for true or false values
     * @param value
     * @return
     */
    private Object typecastValue(String value) {
        Object result;

        if ("true".equals(value)) {
           result = true;
        } else {
            if ("false".equals(value)) {
                result = false;
            } else {
                result = value;
            }
        }
        return result;
    }

    /**
     * Sets the value of a node.
     * @param node
     * @param key
     * @param value
     */
    private void setValue(Object node, String key, String value) {
        if (isHash(node)) {
            ((Map) node).put(key, typecastValue(value));
        } else {
            if (isSequence(node)) {
                if (isArrayIndex(key)) {
                    List sequence = (List) node;
                    int index = Integer.parseInt(key);
                    if (index >= sequence.size()) {
                        // grow the list
                        growSequence(sequence, index);
                        sequence.add(value);
                    } else {
                        if (index >= 0) {
                            sequence.set(index, value);
                        }
                    }
                } else {
                    LOG.error("Attempted to address sequence using key");
                }
            }
        }
    }

    /** Grows a sequence to the specified length */
    private void growSequence(List sequence, int length) {
        for (int i = sequence.size(); i < length; i++) {
            sequence.add(null); // placeholder node
        }
    }

    /** Creates a new empty child node of a type derived from the subExpression */
    private Object createChildNode(String subExpression) {
        Object child;

        // create a new node
        if (isArrayIndex(subExpression)) {
            child = new LinkedList();
        } else {
            child = new HashMap();

            // enable the keySet method
            ((Map) child).put("keySet", new KeySetMethod((Map) child));
        }
        return child;
    }

    /**
     * Gets a child node
     * If the node doesn't exist, a new know is created with a type derived from the subExpression
     *
     * @param node
     * @param key
     * @param subExpression
     */
    private Object getChildNode(Object node, String key, String subExpression, Object parent) {
        Object child = null;

        if (isHash(node)) {
            Map hash = (Map) node;
            child = hash.get(key);
            if ((child == null) || (isScalar(child))) {
                // create a new node
                child = createChildNode(subExpression);
                hash.put(key, child);
            }
        } else {
            if (isSequence(node)) {
                if (isArrayIndex(key)) {
                    List sequence = (List) node;
                    int index = Integer.parseInt(key);

                    if (index < sequence.size()) {
                        child = sequence.get(index);
                        if (child == null) {
                            // replace the placeholder with a new child node of the correct type
                            child = createChildNode(subExpression);
                            sequence.set(index, child);
                        }
                    } else {
                        // need to create a new node
                        child = createChildNode(subExpression);
                        growSequence(sequence, index+1);                        
                        sequence.set(index, child);
                    }
                } else {

                    // this node is a sequence but we're addressing it with a key.  It must have been intended
                    // as a Hash with some numeric keys - convert it to a map
                    List sequence = (List) node;
                    Map hash = transformSequenceToHash(parent, sequence);
                    child = createChildNode(subExpression);
                    hash.put(key, child);
                }
            } else {
                LOG.error("Not sure how to get value from this type");
            }
        }
        return child;
    }

    /** Determine whether the specified object is a Hash or not */
    private boolean isHash(Object node) {
        if (node != null) {
            return Map.class.isAssignableFrom(node.getClass());
        } else {
            return false;
        }
    }

    /** Determine whether the specified object is a Sequence or not */
    private boolean isSequence(Object node) {
        if (node != null) {
            return List.class.isAssignableFrom(node.getClass());
        } else {
            return false;
        }
    }

    private boolean isScalar(Object node) {
        return ((!isHash(node)) && (!isSequence(node)));
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
            case Sequence:
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
