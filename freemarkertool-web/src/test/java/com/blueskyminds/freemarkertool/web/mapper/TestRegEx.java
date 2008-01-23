package com.blueskyminds.freemarkertool.web.mapper;

import junit.framework.TestCase;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class TestRegEx extends TestCase {

    /**
     * Stores the compiled pattern and the variable names matches will correspond to.
     */
    public static class CompiledPattern {
        private Pattern pattern;
        private List<String> variableNames;


        public CompiledPattern(Pattern pattern, List<String> variableNames) {
            this.pattern = pattern;
            this.variableNames = variableNames;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public List<String> getVariableNames() {
            return variableNames;
        }
    }

    /**
     * Compiles the pattern.
     *
     * @param data The pattern, must not be null or empty
     * @return The compiled pattern, null if the pattern was null or empty
     */
    public CompiledPattern compilePattern(String data) {
        StringBuilder regex = new StringBuilder();
        if (data != null && data.length() > 0) {
            List<String> varNames = new ArrayList<String>();
            StringBuilder varName = null;
            for (int x=0; x<data.length(); x++) {
                char c = data.charAt(x);
                switch (c) {
                    case '{' :  varName = new StringBuilder(); break;
                    case '}' :  varNames.add(varName.toString());
                                regex.append("([^/]+)");
                                varName = null;
                                break;
                    default  :  if (varName == null) {
                                    regex.append(c);
                                } else {
                                    varName.append(c);
                                }
                }
            }
            return new CompiledPattern(Pattern.compile(regex.toString()), varNames);
        }
        return null;
    }


    public void testVariablePattern() {

        String input = "${path}";

        CompiledPattern pattern = compilePattern(input);
        // then substitute with values from the Match
    }
}
