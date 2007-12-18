package com.blueskyminds.freemarkertool.context;

/**
 * Used to set a value in the template context
 *
 * Date Started: 16/12/2007
 * <p/>
 * History:
 */
public class TemplateContextBean {

    private boolean enabled;
    private String name;
    private String value;
    private boolean nullValue;

    public TemplateContextBean() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNullValue() {
        return nullValue;
    }

    public void setNullValue(boolean nullValue) {
        this.nullValue = nullValue;
    }
}
