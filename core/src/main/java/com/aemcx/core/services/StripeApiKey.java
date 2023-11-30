package com.aemcx.core.services;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface StripeApiKey {
	public String getApiKey();

}
