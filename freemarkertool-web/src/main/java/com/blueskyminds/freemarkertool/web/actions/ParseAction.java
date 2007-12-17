package com.blueskyminds.freemarkertool.web.actions;

import com.blueskyminds.freemarkertool.services.TemplateService;
import com.blueskyminds.housepad.web.plugin.actions.JSONActionSupport;
import com.google.inject.Inject;
import com.googlecode.jsonplugin.annotations.JSON;
import com.opensymphony.xwork2.Preparable;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;

/**
 * Parses a freemarker template
 *
 * Date Started: 12/12/2007
 * <p/>
 * History:
 */
public class ParseAction extends JSONActionSupport implements Preparable {

    private boolean ignoreErrors;
    private String openTemplate;
    private String bodyText;
    private String closeTemplate;
    //private Map<String, String> inputContext;
    private List<TemplateContextBean> inputContext;
    private String result;
    private TemplateService templateService;
    private ContextFactory contextFactory;
    private Map<String, Object> templateContext;

    public void prepare() {
        //inputContext = new HashMap<String, String>();
        inputContext = new LinkedList<TemplateContextBean>();
    }

    /** Merges the context and template and sets the result */
    public String execute() {
        if (openTemplate == null) {
            openTemplate = "";
        }
        if (closeTemplate == null) {
            closeTemplate = "";
        }
        if (bodyText == null) {
            bodyText = "";
        }

        templateContext = contextFactory.createContext(inputContext);

        result = templateService.mergeTemplate(templateContext, openTemplate, closeTemplate, bodyText, ignoreErrors);
        return SUCCESS;
    }
    
    public void setIgnoreErrors(boolean ignoreErrors) {
        this.ignoreErrors = ignoreErrors;
    }

    @JSON(serialize = false)
    public String getOpenTemplate() {
        return openTemplate;
    }

    public void setOpenTemplate(String openTemplate) {
        this.openTemplate = openTemplate;
    }

    @JSON(serialize = false)
    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    @JSON(serialize = false)
    public String getCloseTemplate() {
        return closeTemplate;
    }

    public void setCloseTemplate(String closeTemplate) {
        this.closeTemplate = closeTemplate;
    }

//    @JSON(serialize = false)
//    public Map<String, String> getContext() {
//        return inputContext;
//    }
//
//    /** The name=value pairs to used to define the context */
//    public void setContext(Map<String, String> inputContext) {
//        this.inputContext = inputContext;
//    }
    @JSON(serialize = false)
    public List<TemplateContextBean> getContext() {
        return inputContext;
    }

    public void setContext(List<TemplateContextBean> inputContext) {
        this.inputContext = inputContext;
    }

    /** The object graph prepared from the name=value pairs */
    public Map<String, Object> getTemplateContext() {
        return templateContext;
    }

    public String getResult() {
        return result;
    }

    @Inject
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Inject
    public void setContextFactory(ContextFactory contextFactory) {
        this.contextFactory = contextFactory;
    }
}

