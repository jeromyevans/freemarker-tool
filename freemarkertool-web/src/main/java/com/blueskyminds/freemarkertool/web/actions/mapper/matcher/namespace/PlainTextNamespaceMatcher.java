package com.blueskyminds.freemarkertool.web.actions.mapper.matcher.namespace;

import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.MatchContext;

/**
 * A namespace matcher that uses plain text equality
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class PlainTextNamespaceMatcher implements NamespaceMatcher {

    /** Create a namespace matcher that uses exact plain text equality */
    public PlainTextNamespaceMatcher() {
    }

    /**
     * Performs an exact comparison between the input and targetnamespace
     *
     *
     * @param input
     * @param targetNamespace
     * @param matchContext      the context is not updated
     * @return
     */
    public boolean match(String input, String targetNamespace, MatchContext matchContext) {
        if (input != null) {
            return (input.equals(targetNamespace));
        } else {
            return false;
        }
    }
}