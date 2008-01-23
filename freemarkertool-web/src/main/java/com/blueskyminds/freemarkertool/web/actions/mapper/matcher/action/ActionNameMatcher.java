package com.blueskyminds.freemarkertool.web.actions.mapper.matcher.action;

import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.MatchContext;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionSelector;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

/**
 * Matches an action name to an ActionConfig
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public interface ActionNameMatcher {

   /**
     * Determines whether the input action name matches the candidate action name
     *
     * @param inputName        the input action name (eg. from a matched URI with variables substituted)
     * @param actionName       the candidate action name specified in the PackageConfig
     * @param actionConfig     the candidate action's config
     * @param actionSelector   the selector currently being used
     * @param matchContext     the match context
    *  @param packageConfig    the config for the package that contains the candidate action
    *
     * @return and ActionMapping derived from the ActionConfig and matchContext
     */
    ActionMapping match(String inputName, String actionName, ActionConfig actionConfig, ActionSelector actionSelector, MatchContext matchContext, PackageConfig packageConfig);
}
