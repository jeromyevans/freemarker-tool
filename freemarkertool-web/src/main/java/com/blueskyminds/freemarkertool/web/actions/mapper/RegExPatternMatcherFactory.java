package com.blueskyminds.freemarkertool.web.actions.mapper;

/**
 * Creates a PatternMatcher that uses RegEx
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class RegExPatternMatcherFactory implements PatternMatcherFactory {
    
    /** Compiles a new RegEx pattern */
    public PatternMatcher create(String pattern, boolean ignoreCase) {
        return new RegExPatternMatcher(pattern, ignoreCase);
    }
}
