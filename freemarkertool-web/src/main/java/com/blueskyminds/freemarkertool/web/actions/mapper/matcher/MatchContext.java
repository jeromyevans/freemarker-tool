package com.blueskyminds.freemarkertool.web.actions.mapper.matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * A simple representation of the contextual information associated with a Match
 *
 * The implementation is a simple Map of name=value pairs and with a separate list of order string values for
 *  group matches (regex groups)
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class MatchContext {

    private static final Log LOG = LogFactory.getLog(MatchContext.class);

    private Map<String, String> params;
    private List<String> groups;
    private String namespace;

    public MatchContext() {
        params = new HashMap<String, String>();
        groups = new ArrayList<String>(10);
    }

    public void put(String key, String value) {
        params.put(key, value);
    }

    public String get(String key) {
        return params.get(key);
    }

    public void addGroup(String value) {
        groups.add(value);
    }

    public String getGroup(int index) {
        if ((index >= 0) && (index < groups.size())) {
            return groups.get(index);
        } else {
            return null;
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
        put(MatcherConstants.NAMESPACE, namespace);
    }

    /** State of the variable name substitution */
    private static enum MatcherState {
          IN_TEXT,
          IN_VAR_START,
          IN_PARAM_NAME,
          IN_GROUP
    }

    /**
     * Substitute values in the context into variables in the input expression.
     * This will substitute values of the form ${name} and $n where
     *   name is the name of a parameter in the MatchContext; or
     *   n is a group number in the MatchContext
     *
     * Implementation is a simple state machine.
     * */
    public String evaluateExpression(String expression) {
        StringBuilder result = new StringBuilder();
        StringBuilder group = null;
        StringBuilder param = null;
        if (expression != null) {
            char[] chars = expression.toCharArray();
            MatcherState state = MatcherState.IN_TEXT;
            MatcherState nextState = state;
            boolean charUsed;

            for (char ch : chars) {
                charUsed = false;
                switch (state) {
                    case IN_TEXT:
                        if (ch == '$') {
                            nextState = MatcherState.IN_VAR_START;
                            charUsed = true;
                        }
                        break;
                    case IN_PARAM_NAME:
                        if (ch == '}') {
                            nextState = endParamName(expression, result, param);
                            charUsed = true;
                        } else {
                            param.append(ch);
                            charUsed = true;
                        }
                        break;
                    case IN_VAR_START:
                        if (Character.isDigit(ch)) {
                            // start of a group number reference
                            group = new StringBuilder();
                            group.append(ch);
                            charUsed = true;
                            nextState = MatcherState.IN_GROUP;
                        } else {
                            if (ch == '{') {
                                // start a new param name
                                param = new StringBuilder();
                                nextState = MatcherState.IN_PARAM_NAME;
                                charUsed = true;
                            } else {
                                // invalid - ignore it
                                charUsed = true;
                            }
                        }
                        break;
                    case IN_GROUP:
                        if (Character.isDigit(ch)) {
                            group.append(ch);
                            charUsed = true;
                        } else {
                            nextState = endGroup(expression, result, group);
                        }
                        break;
                }

                if (!charUsed) {
                    result.append(ch);
                }
                state = nextState;
            }

            // tidy up at end of string
            switch (state) {
                case IN_GROUP:
                    endGroup(expression, result, group);
                    break;
                case IN_PARAM_NAME:
                    endParamName(expression, result, param);
                    break;
            }
            
            return result.toString();
        } else {
            return null;
        }
    }

    private MatcherState endParamName(String pattern, StringBuilder result, StringBuilder param) {
        MatcherState nextState;// end of parameter name...look it up
        String value = get(param.toString());
        result.append(value != null ? value : "");
        nextState = MatcherState.IN_TEXT;
        return nextState;
    }

    private MatcherState endGroup(String pattern, StringBuilder result, StringBuilder group) {
        MatcherState nextState;// end of group number
        try {
            int index = Integer.parseInt(group.toString());
            String value = getGroup(index);
            result.append(value != null ? value : "");
        } catch (NumberFormatException e) {
            LOG.error("Pattern contains an invalid group reference (group="+group.toString()+"):"+ pattern);
        }

        nextState = MatcherState.IN_TEXT;
        return nextState;
    }

    public String toString() {
        boolean first;

        StringBuilder result = new StringBuilder();
        result.append("\n{\n");
        result.append("  params = {\n");
        first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                result.append(",\n");
            } else {
                first = false;
            }
            result.append("    "+entry.getKey()+":"+entry.getValue());
        }
        result.append("\n  },\n");
        result.append("  groups = [");

        first = true;
        for (String entry : groups) {
            if (!first) {
                result.append(",");
            } else {
                first = false;
            }
            result.append(entry);
        }
        result.append("]\n");
        result.append("}\n");
        return result.toString();
    }
}
