package com.blueskyminds.freemarkertool.web.actions;

import java.util.List;
import java.util.LinkedList;

/**
 * Contains all the data for an example
 *
 * Date Started: 18/12/2007
 * <p/>
 * History:
 */
public class ExampleBean {

    private boolean templateView;
    private String openTemplate;
    private String bodyText;
    private String closeTemplate;
    private List<TemplateContextBean> context;

    public ExampleBean() {
        context = new LinkedList<TemplateContextBean>();
    }

    public boolean isTemplateView() {
        return templateView;
    }

    public void setTemplateView(boolean templateView) {
        this.templateView = templateView;
    }

    public String getOpenTemplate() {
        return openTemplate;
    }

    public void setOpenTemplate(String openTemplate) {
        this.openTemplate = openTemplate;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public String getCloseTemplate() {
        return closeTemplate;
    }

    public void setCloseTemplate(String closeTemplate) {
        this.closeTemplate = closeTemplate;
    }

    public List<TemplateContextBean> getContext() {
        return context;
    }

    public void setContext(List<TemplateContextBean> context) {
        this.context = context;
    }

    public void addContextItem(TemplateContextBean bean) {
        context.add(bean);
    }
}
