package com.aemcx.core.servlets;


import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aemcx.core.services.StripeApiKey;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.exception.StripeException;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(
        resourceTypes = "aemcx/components/registrationform", //Resource Type of your Component
        methods = HttpConstants.METHOD_GET,
        extensions = "html",
        selectors = "stripePayment" //This can be any selector you'd like
)


@ServiceDescription("Stripe Check-out Session")

public class StripePayment extends SlingSafeMethodsServlet {
	
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(StripePayment.class);
    
    private String getPropertyValue(final ValueMap properties, final String propertyName) {
    	return properties.containsKey(propertyName) ? properties.get(propertyName, String.class) : StringUtils.EMPTY;
    }
    
    //OSGi Service
    @Reference
    StripeApiKey stripeApiKey;

    @Override
    protected void doGet( final SlingHttpServletRequest req,
                          final SlingHttpServletResponse res) throws IOException {

    	Stripe.apiKey = stripeApiKey.getApiKey();
    	
    	//Functionality to make Domain URL dynamic
    	URL url = new URL(req.getRequestURL().toString());
    	String host = url.getHost();
    	String protocol = url.getProtocol();
    	int port = url.getPort();
    	String portNumber =  Integer.toString(port);
    	
    	//Functionality for dynamic item, price, name, and description
    	String selectedValue = req.getParameter("events");
    	String selectedValuePrice = req.getParameter("price-val");
    	String selectedValueDesc = req.getParameter("desc-val");
    	
    	//dotNotation for price
    	Float decimalPrice = Float.parseFloat(selectedValuePrice) * 100;
    	long unitPrice = decimalPrice.longValue() ;
    	
        String YOUR_DOMAIN = protocol + "://" + host + ":" + portNumber;
        
        //Dynamic success and cancel URLs
        Resource resource = req.getResource();
        ValueMap resourceMap = resource.adaptTo(ValueMap.class);
        String successUrl = getPropertyValue(resourceMap, "successURL");
        String cancelUrl = getPropertyValue(resourceMap, "cancelURL");
        
        
        //Stripe Checkout Session Builder
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("usd")
                                                        .setUnitAmount(unitPrice)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName(selectedValue)
                                                                        .setDescription(selectedValueDesc)
                                                                        .build())
                                                        .build())
                                        .setQuantity(1L)
                                        .build())
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(YOUR_DOMAIN + successUrl)
                        .setCancelUrl(YOUR_DOMAIN + cancelUrl)
                        .build();
        LOGGER.info("abc");
        LOGGER.info(params.toString());
        Session session = null;
        try{
             session = Session.create(params);
             LOGGER.info("Hello" + session);
             LOGGER.info(YOUR_DOMAIN);
             LOGGER.info(Stripe.apiKey);
             LOGGER.info(selectedValue);
             LOGGER.info(selectedValuePrice);
             LOGGER.info(successUrl);
        } catch (StripeException se) {
        	LOGGER.error("Stripe: Exception {}", se);

        }

        res.sendRedirect(session.getUrl());
    }
}
