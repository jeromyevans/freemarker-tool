package com.blueskyminds.freemarkertool.examples.model;

import com.blueskyminds.framework.AbstractEntity;

import javax.persistence.*;

/**
 * Persistent context for an example
 *
 * Date Started: 21/01/2008
 * <p/>
 * History:
 */
@Entity
@Table(name="ExampleContext")
public class ExampleContext extends AbstractEntity {

    private Example example;
    private boolean enabled;
    private String name;
    private String value;
    private boolean nullValue;

    public ExampleContext(Example example, boolean enabled, String name, String value, boolean nullValue) {
        this.example = example;
        this.enabled = enabled;
        this.name = name;
        this.value = value;
        this.nullValue = nullValue;
    }

    /** For ORM */
    protected ExampleContext() {
    }

    @ManyToOne
    @JoinColumn(name="ExampleId")
    public Example getExample() {
        return example;
    }

    public void setExample(Example example) {
        this.example = example;
    }

    @Basic
    @Column(name="Enabled")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Basic
    @Column(name="Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name="Value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Basic
    @Column(name="NullValue")
    public boolean isNullValue() {
        return nullValue;
    }

    public void setNullValue(boolean nullValue) {
        this.nullValue = nullValue;
    }
}
