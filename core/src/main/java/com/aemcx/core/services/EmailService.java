package com.aemcx.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface EmailService {
    String SERVICE_USER = "aemcx-emailread-service";

    boolean sendMail(String email);
}
