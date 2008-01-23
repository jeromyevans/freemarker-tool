package com.blueskyminds.freemarkertool.web.mapper.configuration;

import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionMapConfiguration;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionMapDefinition;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.URIPattern;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionSelector;

import java.util.List;
import java.util.LinkedList;

/**
 * Sets up an ActionMapConfiguration for testing 
 *
 * <p/>
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class SampleActionMapConfiguration implements ActionMapConfiguration {

    private List<ActionMapDefinition> actionMapDefinitions;

    public SampleActionMapConfiguration() {
        init();
    }

    private void init() {
        actionMapDefinitions = new LinkedList<ActionMapDefinition>();

        ActionMapDefinition jsonMapping = new ActionMapDefinition("default");
        jsonMapping.addURIPattern(new URIPattern("1", ".*", "(.*)/(*.)\\.action$"));
        jsonMapping.addActionSelector(new ActionSelector("plainText", "$1", "$2", "plainText", "execute"));
        actionMapDefinitions.add(jsonMapping);
    }

    /**
     * Get list of URI to action mappings in order of precedence
     */
    public List<ActionMapDefinition> getActionMappings() {
        return actionMapDefinitions;
    }
}
