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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class})
public class UserStatusModel {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStatusModel.class);

    private static final String FIRST_NAME = "./profile/familyName";
    private static final String GIVEN_NAME = "./profile/givenName";
    private static final String EMAIL = "./profile/email";


    @SlingObject
    ResourceResolver resourceResolver;

    @Inject
    private SlingHttpServletRequest request;

    private String userDisplay;

    private String familyName;

    private String givenName;

    private String email;

    private boolean isSuccess;

    private boolean isFailure;

    @PostConstruct
    protected void init() {
        try {
            User user = resourceResolver.adaptTo(User.class);
            Value[] firstName = user.getProperty(FIRST_NAME);
            Value[] gName = user.getProperty(GIVEN_NAME);
            Value[] email = user.getProperty(EMAIL);
            this.userDisplay = PropertiesUtil.toString(firstName, user.getID());
            this.givenName = PropertiesUtil.toString(gName, user.getID());
            this.familyName = PropertiesUtil.toString(firstName, user.getID());
            this.email = PropertiesUtil.toString(email, user.getID());
            if (request != null) {
                List<String> selectors = Arrays.asList(request.getRequestPathInfo().getSelectors());
                isSuccess = selectors.contains("success");
                isFailure = selectors.contains("error");

            }
        } catch(RepositoryException e) {
            LOGGER.error("An exception ocurred while retrieving authenticated user details: {0}", e.getMessage());
        }
    }

    public String getUserDisplay() {
        return userDisplay;
    }


    public String getFamilyName() {
        return familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public String getEmail() {
        return email;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public boolean isFailure() {
        return isFailure;
    }

}
