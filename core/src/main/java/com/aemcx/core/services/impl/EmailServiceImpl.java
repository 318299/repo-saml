package com.aemcx.core.services.impl;

import com.aemcx.core.services.EmailService;
import com.day.cq.commons.mail.MailTemplate;
import com.day.cq.mailer.MessageGateway;
import com.day.cq.mailer.MessageGatewayService;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component(service = EmailService.class,
        immediate = true,
        property = {
                "template.path" + "=/conf/aemcx/settings/wcm/templates/emails/en.html",
                "sender.email" + "=aemcx.ntt@gmail.com"
        })
public class EmailServiceImpl implements EmailService{

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final String SUBJECT = "Welcome to AEM CX";

    @Reference
    private MessageGatewayService messageGatewayService;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    private String templatePath;

    private String senderEmail;


    @Activate
    public void activate(final ComponentContext context) {
        templatePath = PropertiesUtil.toString(context.getProperties().get("template.path"), "/conf/aemcx/settings/wcm/templates/emails/en.html");
        senderEmail = PropertiesUtil.toString(context.getProperties().get("sender.email"), "aemcx.ntt@gmail.com");
    }

    /**
     *
     * @param emailTo
     * @return
     */
    public boolean sendMail(String emailTo) {
        // send email method to be rewritten to be more generic in case we want to accomodate different email types
        Map<String, Object> userServiceParam = new HashMap();
        userServiceParam.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
        ResourceResolver serviceResourceResolver = null;

        try {
            serviceResourceResolver = this.resourceResolverFactory.getServiceResourceResolver(userServiceParam);

            MailTemplate mailTemplate = MailTemplate.create(templatePath, serviceResourceResolver.adaptTo(Session.class));

            final Map templateParams = new HashMap<String, String>();
            Email email = mailTemplate.getEmail(StrLookup.mapLookup(templateParams), HtmlEmail.class);
            email.setSubject(SUBJECT);
            email.addTo(emailTo);
            email.setFrom(senderEmail);

            MessageGateway messageGateway = messageGatewayService.getGateway(email.getClass());
            messageGateway.send(email);
            return true;
        } catch (LoginException loginException) {
            LOGGER.error("Error obtaining service resource resolver: {}", loginException.getMessage());
        } catch (EmailException | IOException | MessagingException e) {
            LOGGER.error("Error sending email to " + emailTo, e);
        } finally {
            if (serviceResourceResolver != null) {
                serviceResourceResolver.close();
            }
        }

        return false;
    }

}

