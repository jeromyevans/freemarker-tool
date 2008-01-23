package com.blueskyminds.freemarkertool.examples.model;

import javax.persistence.*;
import java.util.List;
import java.util.LinkedList;
import com.blueskyminds.framework.AbstractEntity;

/**
 * Contains all the data for an example
 *
 * Date Started: 18/12/2007
 * <p/>
 * History:
 */
@Entity
@Table(name="Example")
public class Example extends AbstractEntity {

    private String displayName;
    private String description;
    private boolean templateView;
    private String openTemplate;
    private String bodyText;
    private String closeTemplate;
    private List<ExampleContext> context;

    public Example() {
        context = new LinkedList<ExampleContext>();
    }

    @Basic
    @Column(name="DisplayName")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Basic
    @Column(name="Description", length = 4096)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name="TemplateView")
    public boolean isTemplateView() {
        return templateView;
    }

    public void setTemplateView(boolean templateView) {
        this.templateView = templateView;
    }

    @Basic
    @Column(name="OpenTemplate", length = 8192)
    public String getOpenTemplate() {
        return openTemplate;
    }

    public void setOpenTemplate(String openTemplate) {
        this.openTemplate = openTemplate;
    }

    @Basic
    @Column(name="BodyText", length = 8192)
    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    @Basic
    @Column(name="CloseTemplate", length = 8192)
    public String getCloseTemplate() {
        return closeTemplate;
    }

    public void setCloseTemplate(String closeTemplate) {
        this.closeTemplate = closeTemplate;
    }

    @OneToMany(mappedBy = "example")
    public List<ExampleContext> getContext() {
        return context;
    }

    public void setContext(List<ExampleContext> context) {
        this.context = context;
    }

    public void addContextItem(ExampleContext bean) {
        context.add(bean);
    }
}