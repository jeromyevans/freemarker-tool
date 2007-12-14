package com.blueskyminds.freemarkertool.services;

import java.util.Map;

/**
 * Date Started: 12/12/2007
 * <p/>
 * History:
 */
public interface TemplateService {

     /**
     * Merge the context into the templates.
     * Templates are split into open and close surrounding bodyText
     * BodyText is not parsed
     *
     * @param context
     * @param openTemplate
     * @param closeTemplate
     * @param bodyText
     * @return
     */
    String mergeTemplate(Map<String, Object> context, String openTemplate, String closeTemplate, String bodyText, boolean ignoreErrors);
}
