package com.blueskyminds.freemarkertool.web.mapper.matcher.configuration;

import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.configuration.MatcherProvider;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.namespace.NamespaceMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.action.ActionNameMatcher;

import java.util.Map;
import java.util.HashMap;

/**
 * Simple implementation of a MatcherProvider that holds matcher instances Map keyed by id
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class MockMatcherProvider implements MatcherProvider {

    private Map<String, NamespaceMatcher> namespaceMatchers;
    private Map<String, ActionNameMatcher> actionNameMatchers;

    public MockMatcherProvider() {
        namespaceMatchers = new HashMap<String, NamespaceMatcher>();
        actionNameMatchers = new HashMap<String, ActionNameMatcher>();
    }

    public void addNamespaceMatcher(String id, NamespaceMatcher namespaceMatcher) {
        namespaceMatchers.put(id, namespaceMatcher);
    }

    public void addActionNameMatcher(String id, ActionNameMatcher actionNameMatcher) {
        actionNameMatchers.put(id, actionNameMatcher);
    }

    /**
     * Provides the NamespaceMatcher with the specified Id
     *
     * @param id unique id of this matcher.
     */
    public NamespaceMatcher getNamespaceMatcher(String id) {
        return namespaceMatchers.get(id);
    }

    /**
     * Provides the ActionNameMatcher with the specified Id
     *
     * @param id unique id of this matcher.
     */
    public ActionNameMatcher getActionNameMatcher(String id) {
        return actionNameMatchers.get(id);
    }
}
