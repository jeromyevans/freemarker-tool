package com.blueskyminds.freemarkertool.web.actions.mapper.configuration.digester;

import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionMapConfiguration;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionMapDefinition;

import java.util.List;
import java.util.LinkedList;

/**
 * Contains all the configuration for the URI to Action mapping read from an XML file
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class DigesterActionMapConfiguration implements ActionMapConfiguration {

    private List<ActionMapDefinition> actionMappings;

    public DigesterActionMapConfiguration() {
        actionMappings = new LinkedList<ActionMapDefinition>();
    } 

    public void addDefinition(ActionMapDefinition definition) {
        actionMappings.add(definition);
    }

    public List<ActionMapDefinition> getActionMappings() {
        return actionMappings;
    }
}
