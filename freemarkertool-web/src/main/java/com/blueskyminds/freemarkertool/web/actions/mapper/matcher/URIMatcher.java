package com.blueskyminds.freemarkertool.web.actions.mapper.matcher;

import com.blueskyminds.freemarkertool.web.actions.mapper.ParsedURI;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.URIPattern;

/**
 * The URIMatch matches a URI to a pattern
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public interface URIMatcher {

    /**
     * Matches the method and path components of the URI to the pattern
     *
     * Returns a MatchContext for a successful match.  The MatchContext contains data that may be relevant to
     *  the ActionMatcher such as components of the URI and RegEx groups
     *
     * @param uri
     * @param pattern
     * @return a MatchContext if successful, otherwise null
     */
    MatchContext match(ParsedURI uri, URIPattern pattern);
}
