package com.blueskyminds.freemarkertool.web.actions;

import com.blueskyminds.framework.email.EmailerException;
import com.blueskyminds.framework.email.EmailService;
import com.blueskyminds.framework.template.TemplateProcessingException;
import com.blueskyminds.framework.template.TemplateManager;
import com.google.inject.Inject;
import com.opensymphony.xwork2.ActionSupport;

import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date Started: 1/03/2008
 * <p/>
 * History:
 */
public class ContactAction extends ActionSupport {

    private static final Log LOG = LogFactory.getLog(ContactAction.class);

    private static final String MIME_TEXT_HTML = "text/html";

    private TemplateManager templateManager;
    private EmailService emailService;
    private String fullName;
    private String emailAddress;
    private String message;

    private boolean okay;

    public String execute() throws Exception {
            // notify the system contact via email
        try {
            LOG.info("Sending email");
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("name", fullName);
            context.put("email", emailAddress);
            context.put("message", message);
            String content = templateManager.process("featureRequest.ftl", context);

            emailService.send(emailAddress, "jeromy.evans@blueskyminds.com.au", "FreeMarkerTool Request", content, MIME_TEXT_HTML);

            okay = true;
        } catch (
                EmailerException e) {
            LOG.error("Failed to send email", e);
        } catch (
                TemplateProcessingException e) {
            LOG.error("Failed to render freemarker template", e);
        }

        return SUCCESS;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Inject
    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Inject
    public void setTemplateManager(TemplateManager templateManager) {
        this.templateManager = templateManager;
    }

    public boolean isOkay() {
        return okay;
    }
}
