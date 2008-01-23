package com.blueskyminds.freemarkertool.web.actions.mapper.configuration;

import java.util.List;
import java.util.LinkedList;

/**
 * Contains a mapping of URIPattern's to one or more action selectors
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class ActionMapDefinition {

    private String id;
    private List<URIPattern> patterns;
    private List<ActionSelector> actionSelectors;

    public ActionMapDefinition(String id) {
        this.id = id;
        patterns = new LinkedList<URIPattern>();
        actionSelectors = new LinkedList<ActionSelector>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean addURIPattern(URIPattern uriPattern) {
        return patterns.add(uriPattern);
    }

    public boolean addActionSelector(ActionSelector actionSelector) {
        return actionSelectors.add(actionSelector);
    }

    public List<URIPattern> getPatterns() {
        return patterns;
    }

    public List<ActionSelector> getActionSelectors() {
        return actionSelectors;
    }
}
