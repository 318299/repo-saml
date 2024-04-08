package com.aemcx.core.servlets;


import com.aemcx.core.services.NewsletterService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;

//Issue 17 - Do not hardcode paths using String literals
//Added below line
@SuppressWarnings("CQRules:CQBP-71")
@Component(service = Servlet.class,
        property = {
            Constants.SERVICE_DESCRIPTION + "=NewsLetter Servlet",
            "sling.servlet.methods=" + HttpConstants.METHOD_POST,
            "sling.servlet.paths=" + "/bin/subscribe"
        }
)
public class NewsLetterServlet extends SlingAllMethodsServlet {
    private static final long serialVersionUID = -159625176093879129L;

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsLetterServlet.class);

    @Reference
    private transient NewsletterService newsletterService;

    @Override
    protected void doPost(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String firstName = req.getParameter("firstname");
        String lastName = req.getParameter("lastname");

        LOGGER.debug("Adding [{}] to subscription list", email);
        boolean status = newsletterService.subscribe(email,firstName,lastName);

        String refererHeader = req.getHeader("Referer");
        StringBuilder str = new StringBuilder(refererHeader);
        refererHeader = str.toString().replaceAll("(\\.(success|error))*.html", "." + (status ? "success" : "error") + ".html");

        resp.setContentType("text/html");
        resp.sendRedirect(refererHeader);
    }


}