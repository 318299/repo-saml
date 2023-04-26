package com.aemcx.core.servlets;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
//Issue 5 - Do not use Sling servlet paths to register servlet
//Added below line
@SuppressWarnings("CQRules:CQBP-75")
@Component(service = Servlet.class,
        property = {
                Constants.SERVICE_DESCRIPTION + "= Authentication checker.",
                "sling.servlet.paths=" + "/bin/permissioncheck"
        }
)
public class AuthcheckerServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthcheckerServlet.class);

    public void doHead(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            //retrieve the requested URL
            String uri = request.getParameter("uri");
            //obtain the session from the request
            Session session = request.getResourceResolver().adaptTo(javax.jcr.Session.class);
            //perform the permissions check
            try {
                session.checkPermission(uri, Session.ACTION_READ);
                //LOGGER.info("auth_checker says OK");
				LOGGER.debug("auth_checker says OK");
                response.setStatus(SlingHttpServletResponse.SC_OK);
            } 
            //catch(Exception e) 
			catch(RepositoryException | RuntimeException e){
                //LOGGER.info("auth_checker says READ access DENIED!");
				LOGGER.error("auth_checker says READ access DENIED!");
                response.setStatus(SlingHttpServletResponse.SC_FORBIDDEN);
            }
        } 
        //catch(Exception e) 
		catch(RuntimeException e) {
            LOGGER.error("authchecker servlet exception: " + e.getMessage());
        }
    }
}