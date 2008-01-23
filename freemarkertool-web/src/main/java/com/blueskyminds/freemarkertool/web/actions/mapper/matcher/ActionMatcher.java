package com.blueskyminds.freemarkertool.web.actions.mapper.matcher;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionSelector;
import com.opensymphony.xwork2.config.Configuration;

/**
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public interface ActionMatcher {

    /**
     * Searches for an action in the configuration that matches ActionSelector within the given context
     */
    ActionMapping match(ActionSelector actionSelector, MatchContext matchContext, Configuration configuration);
}
