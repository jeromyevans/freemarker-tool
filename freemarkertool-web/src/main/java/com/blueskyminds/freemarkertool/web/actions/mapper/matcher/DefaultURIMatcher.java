package com.blueskyminds.freemarkertool.web.actions.mapper.matcher;

import com.blueskyminds.freemarkertool.web.actions.mapper.ParsedURI;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.URIPattern;
import com.blueskyminds.freemarkertool.web.actions.mapper.PatternMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.PatternMatcherFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Default implementation of the URIMatch.  Matches a URI to a URIPattern
 *
 * This implementation gets PatternMatchers from a PatternMatcherFactory lazily (when encountered) and
 *  caches the PatternMatcher instance in case it's referenced again.  For RegEx patterns, this means they are
 *  compiled when  encountered and cached in this URIMatcher instance.
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class DefaultURIMatcher implements URIMatcher {

    /** Cached PatternMatchers keyed by URIPattern ID */
    private Map<String, PatternMatcher> cachedMethodMatchers;
    /** Cached PatternMatchers keyed by URIPattern ID */
    private Map<String, PatternMatcher> cachedPathMatchers;

    private PatternMatcherFactory patternMatcherFactory;

    /** Createa new URIMatcher.  The pattern matcher factory implementations must be injected */
    public DefaultURIMatcher() {
    }

    /** Createa new URIMatcher.  The pattern matcher factory implementations must be injected */
    public DefaultURIMatcher(PatternMatcherFactory patternMatcherFactory) {
        this.patternMatcherFactory = patternMatcherFactory;
        this.cachedMethodMatchers = new HashMap<String, PatternMatcher>();
        this.cachedPathMatchers = new HashMap<String, PatternMatcher>();
    }

    /**
     * Matches the method and path components of the URI to the pattern
     *
     * @param uri         the URI parsed into its components
     * @param pattern     the pattern being checked
     * @return a MatchContext if successful, otherwise null
     */
    public MatchContext match(ParsedURI uri, URIPattern pattern) {
        MatchContext match = null;

        if (matchesMethod(uri, pattern)) {
            match = matchesPath(uri, pattern);
        }

        return match;
    }

    /**
     * Matches the method component of the URI to the pattern
     *
     * @param uri           the URI parsed into its components
     * @param pattern       the pattern being checked
     * @return true if this pattern matches the method
     */
    protected boolean matchesMethod(ParsedURI uri, URIPattern pattern) {
        PatternMatcher methodMatcher = prepareMethodMatcher(pattern);
        if (methodMatcher != null) {
            List<String> groupMatches = methodMatcher.matches(uri.getPath());
            return groupMatches.size() > 0;
        } else {
            return true;
        }
    }

    /**
     * Matches a plain text path to the URIPattern
     *
     * @param getPath           a simple path for a simulated GET request
     * @return true if this pattern matches the path component of the URI
     */
    protected MatchContext matchesPath(String getPath, URIPattern pattern) {
        return matchesPath(new ParsedURI("get", getPath, null), pattern);
    }

    /**
     * Matches the path component of the URI to the pattern
     *
     * @param uri           the URI parsed into its components
     * @param pattern       the pattern being checked
     * @return true if this pattern matches the method
     **/
    protected MatchContext matchesPath(ParsedURI uri, URIPattern pattern) {
        MatchContext match = null;
        PatternMatcher pathMatcher = preparePathMatcher(pattern);
        if (pathMatcher != null) {
            List<String> groupMatches = pathMatcher.matches(uri.getPath());
            if (groupMatches.size() > 0) {
                match = prepareMatchContext(uri, pattern);

                for (String groupMatch : groupMatches) {
                    match.addGroup(groupMatch);
                }
            }
        } else {
            // always match
            match = prepareMatchContext(uri, pattern);
        }
        return match;
    }

    /**
     * Instantiates a PatternMatcher and caches it for reuse
     *
     * @param uriPattern
     * @return
     */
    private PatternMatcher preparePathMatcher(URIPattern uriPattern) {
        PatternMatcher patternMatcher = cachedPathMatchers.get(uriPattern.getId());
        if (patternMatcher == null) {
            patternMatcher = patternMatcherFactory.create(uriPattern.getPath(), false);
            cachedPathMatchers.put(uriPattern.getId(), patternMatcher);
        }
        return patternMatcher;
    }

    /**
     * Instantiates a PatternMatcher and caches it for reuse
     *
     * @param uriPattern
     * @return
     */
    private PatternMatcher prepareMethodMatcher(URIPattern uriPattern) {
        PatternMatcher patternMatcher = cachedMethodMatchers.get(uriPattern.getId());
        if (patternMatcher == null) {
            patternMatcher = patternMatcherFactory.create(uriPattern.getPath(), false);
            cachedMethodMatchers.put(uriPattern.getId(), patternMatcher);
        }
        return patternMatcher;
    }

    /**
     * Puts some useful information into the context
     **/
    private MatchContext prepareMatchContext(ParsedURI uri, URIPattern pattern) {
        MatchContext matchContext = new MatchContext();
        matchContext.put(MatcherConstants.METHOD, uri.getMethod());
        matchContext.put(MatcherConstants.PATH, uri.getPath());
        matchContext.put(MatcherConstants.FILE, uri.getFile());
        matchContext.put(MatcherConstants.EXTENSION, uri.getExtension());
        matchContext.put(MatcherConstants.QUERY, uri.getQuery());        
        return matchContext;
    }

    public void setPatternMatcherFactory(PatternMatcherFactory patternMatcherFactory) {
        this.patternMatcherFactory = patternMatcherFactory;
    }
}
