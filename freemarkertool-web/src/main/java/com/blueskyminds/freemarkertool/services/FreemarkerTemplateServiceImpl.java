package com.blueskyminds.freemarkertool.services;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Date Started: 12/12/2007
 * <p/>
 * History:
 */
public class FreemarkerTemplateServiceImpl implements TemplateService {

    private static final String OPEN = "open";
    private static final String CLOSE = "close";

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
    public String mergeTemplate(Map<String, Object> context, String openTemplate, String closeTemplate, String bodyText, boolean ignoreErrors) {
        String result = null;
        StringWriter writer = new StringWriter();
        Template template;
        Configuration cfg = new Configuration();

        if (ignoreErrors) {
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        }

        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate(OPEN, openTemplate);
        templateLoader.putTemplate(CLOSE, closeTemplate);
        cfg.setTemplateLoader(templateLoader);

        try {
            template = cfg.getTemplate(OPEN);
            template.process(context, writer);

            writer.append(bodyText);

            template = cfg.getTemplate(CLOSE);
            template.process(context, writer);

            result = writer.toString();
        } catch (TemplateException e) {
            result = trimStacktrace(writer.toString());       // includes backtrace
        } catch (IOException e) {
            result = e.getMessage();
        }
        return result;
    }

    private String trimStacktrace(String stacktrace) {
        int index = stacktrace.indexOf("at com.blueskyminds");
        if (index >= 0) {
            return stacktrace.substring(0, index)+"...";
        } else {
            return stacktrace;
        }        
    }
}
