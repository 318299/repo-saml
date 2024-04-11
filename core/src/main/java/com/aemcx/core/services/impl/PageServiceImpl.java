package com.aemcx.core.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aemcx.core.beans.ResponseBean;
import com.aemcx.core.beans.SearchResultBean;
import com.aemcx.core.services.PageService;
import com.aemcx.core.services.SolrClientService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component(service = PageService.class, immediate = true)
public class PageServiceImpl implements PageService {

	@Reference
	ResourceResolverFactory resourceResolverFactory;

	@Reference
	SolrClientService solrClientService;

	private static final String SUBSERVICE = "read-page-details-service";
	private static final String METADATA_CONSTANT = "/jcr:content";

	private static final Logger LOGGER = LoggerFactory.getLogger(PageServiceImpl.class);

	@Override
	public void addSolrDoc(String currentPagePath) {
		try {

			Map<String, Object> param = new HashMap<>();
			param.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);

			ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);

			// GET PARENT RESOURCE path = "/content/aemcx/us/en"
			Resource parentResource = resourceResolver.getResource(currentPagePath);

			// GET JCR CONTENT path = "/content/aemcx/us/en/jcr:content"
			// GET TITLE AND DESCRIPTION
			Resource res = resourceResolver.getResource(currentPagePath + METADATA_CONSTANT);
			ValueMap properties = res.adaptTo(ValueMap.class);

			List<SolrInputDocument> documents = new ArrayList<>();

			// Propertires to be added into document
			String path = parentResource.getPath();
			String title = properties.get(com.day.cq.commons.jcr.JcrConstants.JCR_TITLE, String.class);
			String name = parentResource.getName();
			String description = properties.get(com.day.cq.commons.jcr.JcrConstants.JCR_DESCRIPTION, String.class);

			// JSON Object being added into SOLR as a STRING
			// Object includes: Title, Description, and Page Name
			JsonObject contentObject = new JsonObject();
			contentObject.addProperty("title", title);
			contentObject.addProperty("description", description);
			contentObject.addProperty("name", name);
			LOGGER.info("the object added is: {}", contentObject.toString());

			// Add page level data in document
			// ID, Path, and Content are the three fields in SOLR
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", parentResource.getPath().replaceAll("/", "-"));
			doc.addField("path", path);
			doc.addField("content", contentObject.toString());

			// Add doc to documents list
			LOGGER.info(doc.toString());
			documents.add(doc);

			// Solr client info and Collection Name
			SolrClient solrClient = solrClientService.getSolrClient();
			String collection = solrClientService.getSolrCollectionName();

			// add all documents to practice collection
			solrClient.add(collection, documents);

			// save all documents in Solr
			UpdateResponse response = solrClient.commit(collection);

			if (response.getStatus() == 200) {
				// LOGGER HERE!
				LOGGER.info("Data Successfully Updated!");
			}
		} catch (SolrServerException | IOException | LoginException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public void deleteSolrDoc(String docName) {
		try {
			SolrClient solrClient = solrClientService.getSolrClient();
			String collection = solrClientService.getSolrCollectionName();

			solrClient.deleteById(collection, docName);

			solrClient.commit(collection);

		} catch (SolrServerException | IOException e) {
			throw new IllegalArgumentException(e);
		}

	}

	@Override
	public String fecthDocs(String parameterString) {
		ResponseBean responseBean = new ResponseBean();
		// Is this the best to do this?
		String responseJsonString = "";
		try {
			// USING SOLR SERVICE TO GET CLIENT and Collection
			SolrClient solrClient = solrClientService.getSolrClient();
			String collection = solrClientService.getSolrCollectionName();

			// SETTING QUERY TO SEARCH IN SOLR
			SolrQuery solrQuery = new SolrQuery();
			solrQuery.setQuery(parameterString);
			LOGGER.info("The string query is == {}", parameterString);

			QueryResponse response = solrClient.query(collection, solrQuery);
			SolrDocumentList documents = response.getResults();

			LOGGER.info("\n Found {} documents", documents.getNumFound());

			ArrayList<SearchResultBean> searchResultBeanList = new ArrayList<>();

			for (SolrDocument document : documents) {
				SearchResultBean searchResultBean = new SearchResultBean();

				String path = document.getFirstValue("path").toString();

				// Using path to get title from valuemap
				Map<String, Object> param = new HashMap<>();
				param.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);
				ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(param);
				Resource res = resourceResolver.getResource(path + METADATA_CONSTANT);
				ValueMap properties = res.adaptTo(ValueMap.class);
				String title = properties.get(com.day.cq.commons.jcr.JcrConstants.JCR_TITLE, String.class);

				// JAVA BEANS INTEGRATION
				searchResultBean.setPath(path);
				searchResultBean.setTitle(title);

				searchResultBeanList.add(searchResultBean);

				// searchResults.add(result);
				LOGGER.info("\n ---------- properties added to searchResultBean ----------- {}",
						searchResultBean.toString());
				LOGGER.info("\n ========Documents added to the list: ========= {}", searchResultBeanList.toString());
			}
			// LOGGER.info(searchResults.toString());
			responseBean.setNumFound(documents.getNumFound());
			responseBean.setResponseSearchResultBean(searchResultBeanList);
			responseJsonString = new Gson().toJson(responseBean);
			LOGGER.info("\n ------ The data being returned is: {}", responseJsonString);

		} catch (SolrServerException | IOException | LoginException exception) {
			LOGGER.error("\n Error while searching - {}", exception.getMessage());
		}
		// Returning in order to display on UI
		return responseJsonString;

	}
}
