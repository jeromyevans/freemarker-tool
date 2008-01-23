package com.blueskyminds.freemarkertool.web.actions.mapper.matcher;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * A simple representation of the contextual information associated with a Match
 *
 * The implementation is a simple Map of name=value pairs and with a separate list of order string values for
 *  group matches (regex groups)
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class MatchContext {

    private Map<String, String> context;
    private List<String> groups;
    private String namespace;

    public MatchContext() {
        context = new HashMap<String, String>();
        groups = new ArrayList<String>(10);
    }

    public void put(String key, String value) {
        context.put(key, value);
    }

    public String get(String key) {
        return context.get(key);
    }

    public void addGroup(String value) {
        groups.add(value);
    }

    public String getGroup(int index) {
        if ((index >= 0) && (index < groups.size())) {
            return groups.get(index);
        } else {
            return null;
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
        put(MatcherConstants.NAMESPACE, namespace);
    }
}
