package com.blueskyminds.freemarkertool.web.actions.mapper;

import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.URIPattern;

import java.util.List;
import java.util.LinkedList;

/**
 * A successful match to a URI
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class URIMatch {

    private ParsedURI uri;
    private URIPattern pattern;
    private List<String> groups;

    public URIMatch() {
        groups = new LinkedList<String>();
    }

    public URIMatch(ParsedURI uri, URIPattern pattern) {
        this.uri = uri;
        this.pattern = pattern;
        groups = new LinkedList<String>();
    }

    public String getMethod() {
        return uri.getMethod();
    }

    public String getPath() {
        return uri.getPath();
    }

    public String getQuery() {
        return uri.getQuery();
    }

    public String getFile() {
        return uri.getFile();
    }

    public String getExtension() {
        return uri.getExtension();
    }

    public URIPattern getPattern() {
        return pattern;
    }

    public void addGroupMatch(String match) {
        groups.add(match);
    }

    /** Get the group match specified by the index */
    public String getGroup(int index) {
        if (index < 0) {
            return null;
        }
        if (index < groups.size()) {
            return groups.get(index);
        } else {
            return null;
        }
    }
}
