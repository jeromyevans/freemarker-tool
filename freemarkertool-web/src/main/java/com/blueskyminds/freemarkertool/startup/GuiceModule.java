package com.blueskyminds.freemarkertool.startup;

import com.blueskyminds.freemarkertool.services.TemplateService;
import com.blueskyminds.freemarkertool.services.FreemarkerTemplateServiceImpl;
import com.google.inject.AbstractModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Configures Guice Dependency Injection
 *
 * Date Started: 12/12/2007
 * <p/>
 * History:
 */
public class GuiceModule extends AbstractModule {

    private static final Log LOG = LogFactory.getLog(GuiceModule.class);

    public GuiceModule() {
    }

    protected void configure() {
        LOG.info("Setting up binding...");
        bind(TemplateService.class).to(FreemarkerTemplateServiceImpl.class);
    }
}