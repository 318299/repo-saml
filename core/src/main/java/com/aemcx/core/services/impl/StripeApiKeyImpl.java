package com.aemcx.core.services.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.aemcx.core.services.StripeApiKey;

@Component(service = {StripeApiKey.class})
@Designate(ocd = StripeApiKeyImpl.Config.class)
public class StripeApiKeyImpl implements StripeApiKey {
	@ObjectClassDefinition(
		name = "Stripe Api Key",
		description = "Depending on run mode, a different API key will be used."
	)

	@interface Config {

		@AttributeDefinition(
			name = "Stripe API Key",
			description = "API Key Provided by Stripe to create checkout session"
		)
		
		String api_key();
	}
	
	//Initialize StripeApiKey
	private String StripeApiKey;


	@Activate
	protected void activate(Config config) {
		this.StripeApiKey = config.api_key();
	}

	@Override
	public String getApiKey() {
		return StripeApiKey;
	}
	
}
