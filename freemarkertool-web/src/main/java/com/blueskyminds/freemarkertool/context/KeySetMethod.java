package com.blueskyminds.freemarkertool.context;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

import java.util.*;

/**
 * Allows the keyset method to be called on a map
 *
 * Date Started: 20/12/2007
 * <p/>
 * History:
 */
public class KeySetMethod implements TemplateMethodModel {

    private Map map;

    public KeySetMethod(Map map) {
        this.map = map;
    }

    public Object exec(List args) throws TemplateModelException {
        Collection keySet = new HashSet();
        if (map != null) {
            // don't include methods in the key set
            for (Map.Entry entry : (Set<Map.Entry>) map.entrySet()) {
                Object value = entry.getValue();
                if (value != null) {
                    if (!TemplateMethodModel.class.isAssignableFrom(value.getClass())) {
                        keySet.add(entry.getKey());
                    }
                } else {
                    keySet.add(entry.getKey());
                }
            }
        }
        return keySet;
    }
}
