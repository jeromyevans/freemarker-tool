package com.blueskyminds.freemarkertool.web.actions.mapper.matcher;

import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionSelector;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.namespace.NamespaceMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.action.ActionNameMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.configuration.MatcherProvider;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ActionConfig;

import java.util.Map;

/**
 * Matches an ActionSelector to an action within a Struts2 Configuration using the matchers provided by the
 * injected MatcherProvider
 *
 * Date Started: 22/01/2008
 * <p/>
 * History:
 */
public class DefaultActionMatcher implements ActionMatcher {

    private static final Log LOG = LogFactory.getLog(DefaultActionMatcher.class);

    private MatcherProvider matcherProvider;

    public DefaultActionMatcher(MatcherProvider matcherProvider) {
        this.matcherProvider = matcherProvider;
    }

    /** Create a new ActionMapper.  A MatcherProvider needs to be injected */
    public DefaultActionMatcher() {
    }

    /**
     * Searches for an action in the configuration that matches ActionSelector
     */
    public ActionMapping match(ActionSelector actionSelector, MatchContext matchContext, Configuration configuration) {
       ActionMapping actionMapping = null;
        String actionName;
        String packageNamespace;
        PackageConfig packageConfig;
        ActionConfig actionConfig;

        // iterate through all the package configs...
        Map<String, PackageConfig> packageConfigs = configuration.getPackageConfigs();
        for (Map.Entry<String, PackageConfig> packageEntry : packageConfigs.entrySet()) {
            packageConfig = packageEntry.getValue();
            packageNamespace = packageConfig.getNamespace();

            // check if the namespace matches...
            if (matchNamespace(actionSelector, matchContext, packageNamespace)) {
                Map<String, ActionConfig> actionsConfigs = packageConfig.getActionConfigs();

                // check if any of the actions match...
                for (Map.Entry<String, ActionConfig> actionEntry : actionsConfigs.entrySet()) {
                    actionName = actionEntry.getKey();
                    actionConfig = actionEntry.getValue();

                    actionMapping = matchActionName(actionConfig, actionSelector, matchContext, actionName, packageConfig);
                    if (actionMapping != null) {
                        break;
                    }
                }
            }

            // break out on the first matching action
            if (actionMapping != null) {
                break;
            }
        }

        return actionMapping;
    }

    public String substituteVariables(String pattern, MatchContext matchContext) {
        String result = matchContext.evaluateExpression(pattern);
        if (result.contains("$")) {
            // allow a level of nesting - this will evaluate expressions in the params of the URIMatch
            result = matchContext.evaluateExpression(result);
        }
        return result;
    }

    /**
     * Match the namespace pattern if defined
     *
     * @return true if there's a match or the pattern isn't defined
     **/
    protected boolean matchNamespace(ActionSelector actionSelector, MatchContext matchContext, String candidateNamespace) {
        String namespacePattern = actionSelector.getNamespace();
        if (namespacePattern != null) {
            NamespaceMatcher namespaceMatcher = matcherProvider.getNamespaceMatcher(actionSelector.getNamespaceMatcher());
            if (namespaceMatcher != null) {
                String substituted = substituteVariables(namespacePattern, matchContext);
                boolean matched = namespaceMatcher.match(substituted, candidateNamespace, matchContext);
                if (LOG.isDebugEnabled()) {
                    if (matched) {
                        LOG.debug("Matched namespace:"+candidateNamespace+" to pattern "+substituted+" using "+actionSelector.getNamespaceMatcher());
                    }
                }
                return matched;
            } else {
                LOG.error("Unknown NamespaceMatcher specified in ActionSelector: "+ actionSelector.getNamespaceMatcher());
                return false;
            }
        } else {
            return true;
        }
    } 

    /**
     * Match the actionName pattern if defined
     * @return true if there's a match or the pattern isn't defined
     **/
    protected ActionMapping matchActionName(ActionConfig actionConfig, ActionSelector actionSelector, MatchContext matchContext, String actionName, PackageConfig packageConfig) {
        ActionMapping actionMapping = null;
        String actionNamePattern = actionSelector.getAction();
        if (actionNamePattern != null) {
            ActionNameMatcher actionNameMatcher = matcherProvider.getActionNameMatcher(actionSelector.getActionMatcher());
            if (actionNameMatcher != null) {
                // substitute variables into the pattern if required
                String substituted = substituteVariables(actionNamePattern, matchContext);
                // and match to the action config...
                actionMapping = actionNameMatcher.match(substituted, actionName, actionConfig, actionSelector, matchContext, packageConfig);

                if (LOG.isDebugEnabled()) {
                    if (actionMapping != null) {
                        LOG.debug("Matched action:"+actionName+" to pattern "+substituted+" using "+actionSelector.getActionMatcher());
                    } else {
                        LOG.debug("No match for action:"+actionName+" against pattern "+substituted+" using "+actionSelector.getActionMatcher());
                    }
                }
            } else {
                LOG.error("Unknown ActionNameMatcher specified in ActionSelector: "+ actionSelector.getActionMatcher());
            }
        } else {
            LOG.error("Null action name specified in ActionSelector: "+ actionSelector.getNamespaceMatcher());
        }
        return actionMapping;
    }

    /**
     * Set the MatcherProvider implementation to use
     *
     * @param matcherProvider  a MatcherProvider implementation
     */
    public void setMatcherProvider(MatcherProvider matcherProvider) {
        this.matcherProvider = matcherProvider;
    }
}
