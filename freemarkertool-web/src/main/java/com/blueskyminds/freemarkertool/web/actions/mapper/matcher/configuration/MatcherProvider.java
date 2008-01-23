package com.blueskyminds.freemarkertool.web.actions.mapper.matcher.configuration;

import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.namespace.NamespaceMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.action.ActionNameMatcher;

/**
 * Provides the matcher instances used by the ActionMatcher
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public interface MatcherProvider {

    /**
     * Provides the NamespaceMatcher with the specified Id
     * @param id        unique id of this matcher.
     * */
    NamespaceMatcher getNamespaceMatcher(String id);

    /**
     * Provides the ActionNameMatcher with the specified Id
     * @param id        unique id of this matcher.
     * */
    ActionNameMatcher getActionNameMatcher(String id);
}
