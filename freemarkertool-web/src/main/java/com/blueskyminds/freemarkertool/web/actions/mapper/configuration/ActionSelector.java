package com.blueskyminds.freemarkertool.web.actions.mapper.configuration;

/**
 * Defines patterns and matchers for selecting an action
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class ActionSelector {

    private static final String DEFAULT_METHOD = "execute";
    private static final String DEFAULT_MATCHER = "plainText";

    private String namespaceMatcher;
    private String namespace;
    private String actionMatcher;
    private String action;
    private String method;

    /**
     *
     * @param namespaceMatcher   name of the namespace matcher to use
     * @param namespace          namespace pattern
     * @param actionMatcher      name of the action name matcher to use
     * @param action             action name pattern
     * @param method             action method to execute
     */
    public ActionSelector(String namespaceMatcher, String namespace, String actionMatcher, String action, String method) {
        this.namespaceMatcher = namespaceMatcher;
        this.namespace = namespace;
        this.actionMatcher = actionMatcher;
        this.action = action;
        this.method = method;
    }

    /**
     * Use the default namespace and action matcher
     *
     * @param namespace          namespace pattern
     * @param action             action name pattern
     * @param method             action method to execute
     */
    public ActionSelector(String namespace, String action, String method) {
        this.namespace = namespace;
        this.action = action;
        this.method = method;
    }

    public ActionSelector() {
    }

    public String getNamespaceMatcher() {
        return defaultIfNull(namespaceMatcher, DEFAULT_MATCHER);
    }

    public void setNamespaceMatcher(String namespaceMatcher) {
        this.namespaceMatcher = namespaceMatcher;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getActionMatcher() {
        return defaultIfNull(actionMatcher, DEFAULT_MATCHER);
    }

    public void setActionMatcher(String actionMatcher) {
        this.actionMatcher = actionMatcher;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return defaultIfNull(method, DEFAULT_METHOD);
    }

    public void setMethod(String method) {
        this.method = method;
    }

    private String defaultIfNull(String value, String defaultValue) {
        if ((value != null && (value.length() > 0))) {
            return value;
        } else {
            return defaultValue;
        }
    }

}
