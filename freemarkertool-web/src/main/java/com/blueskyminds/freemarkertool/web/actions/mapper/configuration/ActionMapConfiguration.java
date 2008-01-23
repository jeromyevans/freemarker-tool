package com.blueskyminds.freemarkertool.web.actions.mapper.configuration;

import java.util.List;

/**
 * Provides the configuration of URIs to Action mappings
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public interface ActionMapConfiguration {

    /** Get list of URI to action mappings in order of precedence */
    List<ActionMapDefinition> getActionMappings();
}
