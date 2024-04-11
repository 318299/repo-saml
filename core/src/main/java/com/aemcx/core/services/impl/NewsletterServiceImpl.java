package com.aemcx.core.services.impl;

import com.aemcx.core.services.EmailService;
import com.aemcx.core.services.NewsletterService;
import com.aemcx.core.servlets.NewsLetterServlet;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import org.apache.sling.api.resource.*;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Component(service = NewsletterService.class, immediate = true)
public class NewsletterServiceImpl implements NewsletterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NewsLetterServlet.class);

    @Reference
    QueryBuilder queryBuilder;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    @Reference
    private transient EmailService emailService;

    /**
     *
     * @param email
     * @return
     */
    public boolean subscribe(String email, String firstName, String lastName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("path", BASE_PATH);
        //Issue 13 - Use constant NT_UNSTRUCTURED from interface com.day.cq.commons.jcr.JcrConstants instead of hardcoded value.
		// Commented out below line and added new line
        //map.put("type", "nt:unstructured");
		map.put("type", JcrConstants.NT_UNSTRUCTURED);
        map.put("property", "email");
        map.put("property.value", email);
        map.put("p.limit", "-1");

        Map<String, Object> userServiceParam = new HashMap<>();
        userServiceParam.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);
        ResourceResolver serviceResourceResolver = null;

        try {
            serviceResourceResolver = this.resourceResolverFactory.getServiceResourceResolver(userServiceParam);
            Query query = queryBuilder.createQuery(PredicateGroup.create(map), serviceResourceResolver.adaptTo(Session.class));

            SearchResult searchResult = query.getResult();
            long match = searchResult.getTotalMatches();

            if (match == 0) {
                Node node = this.getBaseResource(serviceResourceResolver);
                //Issue 14 - Use constant NT_UNSTRUCTURED from interface com.day.cq.commons.jcr.JcrConstants instead of hardcoded value.
				// Commented out below line and added new line
                //Node newNode = node.addNode(email, "nt:unstructured");
				if (node == null) {
					throw new RepositoryException("node is null");
				}
				Node newNode = node.addNode(email, JcrConstants.NT_UNSTRUCTURED);
                newNode.setProperty("email", email);
                newNode.setProperty("firstName", firstName);
                newNode.setProperty("lastName", lastName);
                serviceResourceResolver.commit();

                boolean success = emailService.sendMail(email);

                if (success) {
                    newNode.setProperty("lastEmailSent", LocalDateTime.now().toString());
                    serviceResourceResolver.commit();
                }
            }
            return true;
        } catch (LoginException e) {
            LOGGER.error("An error occurred while finding out if the user is already subscribed: {0}", e.getMessage());
        } catch (RepositoryException | PersistenceException ex) {
            LOGGER.error("An exception occurred while trying to save the email address when subscribing: {0}", ex.getMessage());
        } finally {
            if (serviceResourceResolver != null) {
                serviceResourceResolver.close();
            }
        }
        return false;
    }

    private Node getBaseResource(ResourceResolver serviceResourceResolver) {
        Node usergeneratedNode;
        Node subscriptionsNode = null;
        Resource resource = serviceResourceResolver.getResource(NewsletterService.BASE_PATH);
        try {
            if (null == resource) {
                Resource usergeneratedResource = serviceResourceResolver.getResource("/content/aemcx/usergenerated");
                if (null == usergeneratedResource) {
                    Resource contentResource = serviceResourceResolver.getResource("/content/aemcx");
                    Node contentNode = contentResource.adaptTo(Node.class);
                    usergeneratedNode = contentNode.addNode("usergenerated", JcrConstants.NT_UNSTRUCTURED);
                    subscriptionsNode = usergeneratedNode.addNode("subscriptions", JcrConstants.NT_UNSTRUCTURED);
                    serviceResourceResolver.commit();
                    return subscriptionsNode;
                }
                usergeneratedNode = usergeneratedResource.adaptTo(Node.class);
                subscriptionsNode = usergeneratedNode.addNode("subscriptions", JcrConstants.NT_UNSTRUCTURED);
                serviceResourceResolver.commit();
                return subscriptionsNode;

            }
            subscriptionsNode = resource.adaptTo(Node.class);

        } catch (RepositoryException | PersistenceException e) {
			NewsletterServiceImpl.LOGGER.error("Error while creating user generated code ()", e.getMessage());
		}
        return subscriptionsNode;
    }
}
