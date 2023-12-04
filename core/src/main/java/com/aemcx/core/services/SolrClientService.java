package com.aemcx.core.services;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

public interface SolrClientService {
    public HttpSolrClient getSolrClient();
    public String getSolrCollectionName();
}
