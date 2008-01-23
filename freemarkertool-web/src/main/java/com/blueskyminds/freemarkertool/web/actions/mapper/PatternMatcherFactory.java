package com.blueskyminds.freemarkertool.web.actions.mapper;

/**
 * Creates PatternMatcher instances
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public interface PatternMatcherFactory {

    PatternMatcher create(String pattern, boolean ignoreCase);
}
