package com.aemcx.core.models;

import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
//import org.apache.sling.commons.osgi.PropertiesUtil;
import org.apache.jackrabbit.oak.commons.PropertiesUtil;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class})
public class UserStatusModel {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatusModel.class);
    private static final String FIRST_NAME = "./profile/firstName";

    @SlingObject
    ResourceResolver resourceResolver;

    private String userDisplay;

    @PostConstruct
    protected void init() {
        try {
            User user = resourceResolver.adaptTo(User.class);
            Value[] firstName = user.getProperty(FIRST_NAME);
            this.userDisplay = PropertiesUtil.toString(firstName, user.getID());
        } catch(RepositoryException e) {
            LOGGER.error("An exception ocurred while retrieving authenticated user details: {0}", e.getMessage());
        }
    }

    public String getUserDisplay() {
        return userDisplay;
    }
}
