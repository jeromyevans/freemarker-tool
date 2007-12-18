package com.blueskyminds.freemarkertool.web.actions;

/**
 * Date Started: 18/12/2007
 * <p/>
 * History:
 */
public class ExampleLoader {

    public ExampleBean loadExample(int id) {
        return createHelloWorld();
    }

    private ExampleBean createHelloWorld() {
        ExampleBean exampleBean = new ExampleBean();
        exampleBean.setTemplateView(true);
        exampleBean.setOpenTemplate("Hello ${firstName}, we hope you find this tool helpful.");
        TemplateContextBean item = new TemplateContextBean();
        item.setEnabled(true);
        item.setName("firstName");
        item.setNullValue(false);
        item.setValue("New User");
        exampleBean.addContextItem(item);
        return exampleBean;
    }
}
