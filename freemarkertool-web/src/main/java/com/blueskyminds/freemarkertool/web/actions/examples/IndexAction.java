package com.blueskyminds.freemarkertool.web.actions.examples;

import com.opensymphony.xwork2.ActionSupport;
import com.google.inject.Inject;
import com.blueskyminds.freemarkertool.examples.ExampleLoader;
import com.blueskyminds.freemarkertool.examples.ExampleBean;

import java.util.List;
import java.util.LinkedList;

/**
 * Returns a view with a list of examples
 *
 * Date Started: 19/01/2008
 * <p/>
 * History:
 */
public class IndexAction extends ActionSupport {

    private ExampleLoader exampleLoader;
    private List<ExampleBean> examples;

    @Override
    public String execute() throws Exception {
        //exampleLoader.listExamples();
        examples = new LinkedList<ExampleBean>();
        return SUCCESS;
    }

    @Inject
    public void setExampleFactory(ExampleLoader exampleLoader) {
        this.exampleLoader = exampleLoader;
    }

    public List<ExampleBean> getExamples() {
        return examples;
    }
}
