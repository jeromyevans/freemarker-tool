package com.blueskyminds.freemarkertool.startup;

import com.blueskyminds.freemarkertool.services.TemplateService;
import com.blueskyminds.freemarkertool.services.FreemarkerTemplateServiceImpl;
import com.blueskyminds.framework.persistence.jpa.JpaHelper;
import com.blueskyminds.framework.jpa.ThreadLocalEntityManagerProvider;
import com.google.inject.AbstractModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.blueskyminds.framework.tools.PropertiesContext;
import com.blueskyminds.framework.guice.providers.JNDIProvider;
import com.blueskyminds.framework.guice.providers.EmailServiceProvider;
import com.blueskyminds.framework.guice.providers.TemplateManagerProvider;
import com.blueskyminds.framework.template.TemplateManager;
import com.blueskyminds.framework.email.EmailService;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.mail.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * Configures Guice Dependency Injection
 *
 * Date Started: 12/12/2007
 * <p/>
 * History:
 */
public class GuiceModule extends AbstractModule {

    private static final Log LOG = LogFactory.getLog(GuiceModule.class);

    private static final String PROPERTIES_FILE = "freemarkertool.properties";
    private static final String PERSISTENCE_UNIT_PROPERTY = "persistenceUnit";

    protected void configure() {
        // Bind Context to the default InitialContext.
        bind(Context.class).to(InitialContext.class);

        // eagerly create the heavy-weight entity manager factory
        //PropertiesContext properties = new PropertiesContext(PROPERTIES_FILE);
        //EntityManagerFactory emf = JpaHelper.createEntityManagerFactory(properties.getProperty(PERSISTENCE_UNIT_PROPERTY));

        //bind(EntityManagerFactory.class).toInstance(emf);
        //bind(EntityManager.class).toProvider(ThreadLocalEntityManagerProvider.class);

        bind(TemplateService.class).to(FreemarkerTemplateServiceImpl.class);

        bind(Session.class).toProvider(JNDIProvider.fromJndi(Session.class, "java:comp/env/mail/MyMailSession"));
        bind(TemplateManager.class).toProvider(TemplateManagerProvider.class).asEagerSingleton();
        bind(EmailService.class).toProvider(EmailServiceProvider.class);
    }
}