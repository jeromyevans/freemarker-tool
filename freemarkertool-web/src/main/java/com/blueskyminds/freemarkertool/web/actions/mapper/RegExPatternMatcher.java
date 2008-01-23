package com.blueskyminds.freemarkertool.web.actions.mapper;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Performs a match using a compiled regular expression
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class RegExPatternMatcher implements PatternMatcher {

    private Pattern pattern;

    public RegExPatternMatcher(String regEx, boolean ignoreCase) {
        if (regEx != null) {
            if (ignoreCase) {
                this.pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
            } else {
                this.pattern = Pattern.compile(regEx);
            }
        } else {
            pattern = null;
        }
    }

    /**
     * Matches the inputSequence against the pattern
     *
     * @param inputSequence the sequence to check.  A null value can match a null pattern
     * @return a list of grouped matched, where:
     *         an empty list implies no match;
     *         list item zero contains the full match (as per java.util.regex.Matcher)
     *         list item 1..n contains group matches (as per java.util.regex.Matcher@group)
     *         <p/>
     *         The match value at index 0 implies a null value was matched to a null pattern
     *         <p/>
     *         The list itself is NEVER null
     */
    public List<String> matches(String inputSequence) {
        List<String> groups;

        if (inputSequence != null) {
            if (pattern != null) {
                Matcher matcher = pattern.matcher(inputSequence);

                if (matcher.find()) {
                    groups = new ArrayList<String>(matcher.groupCount()+1);

                    groups.add(matcher.group(0));

                    // map all of the groups into the result
                    if (matcher.groupCount() > 0) {
                        for (int group = 1; group <= matcher.groupCount(); group++) {
                            groups.add(matcher.group(group));
                        }
                    }
                } else {
                    // empty list
                    groups = new LinkedList<String>();
                }
            } else {
                // full match - one group
                groups = new LinkedList<String>();
                groups.add(inputSequence);
            }
        } else {
            if (pattern == null) {
                // matched a null input to a null value
                groups = new LinkedList<String>();
                groups.add(null);
            } else {
                // empty list
                groups = new LinkedList<String>();
            }
        }

        return groups;
    }
}
