package com.aemcx.core.services;

public interface NewsletterService {
    String SERVICE_USER = "aemcx-newsletterwrite-service";

    String BASE_PATH = "/etc/aemcx/subscriptions";

    boolean subscribe(String email, String firstName, String lastName);
}
