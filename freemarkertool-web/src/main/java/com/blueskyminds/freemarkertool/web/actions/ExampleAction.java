package com.blueskyminds.freemarkertool.web.actions;

import com.blueskyminds.housepad.web.plugin.actions.JSONActionSupport;
import com.google.inject.Inject;
import com.googlecode.jsonplugin.annotations.JSON;

/**
 * Provides the requested example
 *
 * Date Started: 18/12/2007
 * <p/>
 * History:
 */
public class ExampleAction extends JSONActionSupport {

    private Integer id;

    private ExampleLoader exampleLoader;
    private ExampleBean example;

    public void setId(Integer id) {
        this.id = id;
    }

    public String execute() {
        if (id != null) {
            example = exampleLoader.loadExample(id);
        }
        return SUCCESS;
    }

    @JSON(serialize = true)
    public ExampleBean getExample() {
        return example;
    }

    @Inject
    public void setExampleFactory(ExampleLoader exampleLoader) {
        this.exampleLoader = exampleLoader;
    }
}
