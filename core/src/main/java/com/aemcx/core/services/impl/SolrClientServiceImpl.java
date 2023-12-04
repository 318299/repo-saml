package com.aemcx.core.services.impl;

import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import com.aemcx.core.services.SolrClientService;

@Component(service = SolrClientService.class)
@Designate(ocd = SolrClientServiceImpl.Config.class)
public class SolrClientServiceImpl implements SolrClientService{

    //Declare solrClientURL
    private String solrClientUrl;
    private String collectionName;

    /* Config for solr server
     * connection. Run mode dependent */
    @ObjectClassDefinition(
        name = "Solr Client Url",
        description = "Depending on run mode, solr client may change"
    )

    @interface Config {
        @AttributeDefinition(
            name = "Solr Client URL",
            description = "Used to connect to solr server"
        )
        String solrUrl();

        @AttributeDefinition(
            name = "Collection Name",
            description = "The collection to be indexed in Solr"
        )
        String collectionString();
    }

    @Activate
    protected void activate(Config config) {
        this.solrClientUrl = config.solrUrl();
        this.collectionName = config.collectionString();
    }

    @Override
    public HttpSolrClient getSolrClient() {
        return new HttpSolrClient.Builder(solrClientUrl)
                .withConnectionTimeout(50000)
                .withSocketTimeout(50000)
                .build();
    }
    @Override
    public String getSolrCollectionName() {
        return collectionName;
    }

}
