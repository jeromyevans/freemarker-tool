package com.blueskyminds.freemarkertool.web.actions.mapper.matcher.namespace;

import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.MatchContext;

/**
 * The NamedVariableNamespaceMatcher allows
 *
 * Date Started: 29/01/2008
 * <p/>
 * History:
 */
public class NamedVariableNamespaceMatcher implements NamespaceMatcher {

    /**
     * Determines whether the input namespace matches the candidate namespace
     *
     * @param inputNamespace     the input namespace (eg. from a matched URI)
     * @param candidateNamespace the candidate namespace (eg. for a package)
     * @param matchContext       the context may be used and/or updated if relevant
     * @return
     */
    public boolean match(String inputNamespace, String candidateNamespace, MatchContext matchContext) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
