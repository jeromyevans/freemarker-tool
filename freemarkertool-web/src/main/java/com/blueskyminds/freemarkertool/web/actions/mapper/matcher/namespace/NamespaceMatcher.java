package com.blueskyminds.freemarkertool.web.actions.mapper.matcher.namespace;

import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.MatchContext;

/**
 * Matches a Struts2 package namespace against a pattern
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public interface NamespaceMatcher {

    /**
     * Determines whether the input namespace matches the candidate namespace
     *
     * @param inputNamespace        the input namespace (eg. from a matched URI)
     * @param candidateNamespace    the candidate namespace (eg. for a package)
     * @param matchContext          the context may be used and/or updated if relevant
     * @return
     */
    boolean match(String inputNamespace, String candidateNamespace, MatchContext matchContext);
}
