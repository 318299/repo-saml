package com.aemcx.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aemcx.core.services.PageService;
import com.aemcx.core.services.SolrClientService;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Solr",
		"sling.servlet.methods={ GET, POST}", "sling.servlet.paths=" + "/bin/solr" })
public class SolrSearch extends SlingAllMethodsServlet {
	private static final long serialVersionUID = -2781149517615924050L;

	// LOGGER CLASS
	private static final Logger LOGGER = LoggerFactory.getLogger(SolrSearch.class);

	@Reference
	private transient PageService pageService;

	@Reference
	private transient SolrClientService solrClientService;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {
		LOGGER.debug("------- SEARCH SOLR START -------");

		// search text
		String searchText = req.getParameter("query-string");
		LOGGER.debug("The search text is: {}", searchText);

		if (searchText == null) {
			resp.getWriter().write("Oops an error happened! query string was empty");
		} else {
			// query string to be passed to service to search solr docs
			String query = "content:" + searchText;

			pageService.fecthDocs(query);

			LOGGER.debug("------- SEARCH SOLR END -------");

			resp.getWriter().write(pageService.fecthDocs(query).toString());

		}
		resp.setContentType("application/json");

	}
}