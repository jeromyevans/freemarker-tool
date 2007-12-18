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

    private static final String PAGE = "Page Template";
    private static final String OPEN = "Open Template";
    private static final String CLOSE = "Close Template";

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
        templateLoader.putTemplate(PAGE, openTemplate);  // name's significant for error messsages
        templateLoader.putTemplate(OPEN, openTemplate);  // name's significant for error messsages
        templateLoader.putTemplate(CLOSE, closeTemplate);
        cfg.setTemplateLoader(templateLoader);

        try {
            if (closeTemplate != null) {
                template = cfg.getTemplate(OPEN);
                template.process(context, writer);

                writer.append(bodyText);

                template = cfg.getTemplate(CLOSE);
                template.process(context, writer);
            } else {
                template = cfg.getTemplate(PAGE);
                template.process(context, writer);
            }

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
