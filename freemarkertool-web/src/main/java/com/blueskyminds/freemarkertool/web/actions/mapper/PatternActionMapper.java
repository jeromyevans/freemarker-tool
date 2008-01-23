package com.blueskyminds.freemarkertool.web.actions.mapper;

import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletRequest;

import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.inject.Inject;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionMapDefinition;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionMapConfiguration;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.URIPattern;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionSelector;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.URIMatcher;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.MatchContext;
import com.blueskyminds.freemarkertool.web.actions.mapper.matcher.ActionMatcher;

import java.util.Iterator;

/**
 * A Struts2 ActionMapper that uses patterns
 *
 * methods
 * url
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
public class PatternActionMapper implements ActionMapper {

    private ActionMapConfiguration mappingConfiguration;
    private URIMatcher uriMatcher;
    private ActionMatcher actionMatcher;

    public PatternActionMapper() {
    }

    /**
     * Derives an ActionMapping for the current request using the mappingConfiguration
     *
     * @param httpServletRequest     The servlet request
     * @param configurationManager   The current configuration manager
     * @return The appropriate action mapping or null of no match
     */
    public ActionMapping getMapping(HttpServletRequest httpServletRequest, ConfigurationManager configurationManager) {
        String method = httpServletRequest.getMethod();
        String path = httpServletRequest.getRequestURI();
        String query = httpServletRequest.getQueryString();
        Configuration configuration = configurationManager.getConfiguration();

        ParsedURI uri = new ParsedURI(method, path, query);

        return getMapping(uri, configuration);
    }

    /**
     * Creates an ActionMapping based on the URI and Struts2 Configuration
     **/
    protected ActionMapping getMapping(ParsedURI uri, Configuration configuration) {
        ActionMapping actionMapping = null;

        if (mappingConfiguration != null) {
            // check the action map definitions in order
            for (ActionMapDefinition actionMapDefinition : mappingConfiguration.getActionMappings()) {

                // check each URI pattern...
                Iterator<URIPattern> uriPatternIterator = actionMapDefinition.getPatterns().iterator();
                while ((actionMapping == null) && (uriPatternIterator.hasNext())) {
                    MatchContext matchContext = uriMatcher.match(uri, uriPatternIterator.next());
                    if (matchContext != null) {
                        // The URI matches, check each action selector...
                        Iterator<ActionSelector> selectorIterator = actionMapDefinition.getActionSelectors().iterator();
                        while ((actionMapping == null) && (selectorIterator.hasNext())) {
                            actionMapping = actionMatcher.match(selectorIterator.next(), matchContext, configuration);                            
                        }
                    }
                }
            }
        }

        return actionMapping;
    }

    public String getUriFromActionMapping(ActionMapping actionMapping) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Inject("mapper.actionMapConfiguration")
    public void setMappingConfiguration(ActionMapConfiguration mappingConfiguration) {
        this.mappingConfiguration = mappingConfiguration;
    }

    @Inject("mapper.URIMatcher")
    public void setURIMatcher(URIMatcher uriMatcher) {
        this.uriMatcher = uriMatcher;
    }

    @Inject("mapper.ActionMatcher")
    public void setActionMatcher(ActionMatcher actionMatcher) {
        this.actionMatcher = actionMatcher;
    }
}
