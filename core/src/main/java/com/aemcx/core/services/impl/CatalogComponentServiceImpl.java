package com.aemcx.core.services.impl;

import com.aemcx.core.services.CatalogComponentService;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component(service = CatalogComponentService.class, immediate = true)
public class CatalogComponentServiceImpl implements CatalogComponentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogComponentServiceImpl.class);

    @Reference
    private QueryBuilder queryBuilder;

    @Reference
    private ResourceResolverFactory resourceResolverFactory;

    /**
     * Find the dialog path of a resource/component.
     * @param resource
     * @return resource's dialog path or null
     */
    public String getDialogPath(Resource resource) {
        if (resource == null) {
            return null;
        }
        com.day.cq.wcm.api.components.Component component = resource.adaptTo(com.day.cq.wcm.api.components.Component.class);

        if (component != null) {
            Iterable<Resource> children = resource.getChildren();

            Optional<Resource> dialog = StreamSupport.stream(children.spliterator(), false)
                    .filter(child -> child.getName().equals(CQ_DIALOG))
                    .findFirst();

            if (dialog.isPresent()) {
                return dialog.get().getPath();
            }
        }

        ResourceResolver serviceResourceResolver = null;
        try {
            serviceResourceResolver = this.getResourceResolver();
            Resource resourceComponent = serviceResourceResolver.getResource(resource.getResourceType());

            String resourceDialog = getDialogPath(resourceComponent);

            if (StringUtils.isEmpty(resourceDialog)) {
                resourceComponent = serviceResourceResolver.getResource(resource.getResourceSuperType());
                return getDialogPath(resourceComponent);
            }
            return resourceDialog;
        } catch (LoginException e) {
            LOGGER.error("An exception occurred while finding the dialog of a resource: {}", e.getMessage());
        } finally {
            if (serviceResourceResolver != null) {
                serviceResourceResolver.close();
            }
        }

        return null;
    }

    /**
     * Recursively find all properties for a dialog.
     * Currently it does not support inherited properties.
     * @param dialogPath
     * @return
     */
    public List<Map> findProperties(String dialogPath) {
        Map<String, String> map = new HashMap<String, String>();

        map.put("path", dialogPath);
        map.put("property", "name");
        map.put("property.operation", "exists");
        map.put("property.value", "true");
        map.put("p.limit", "-1");

        ResourceResolver serviceResourceResolver = null;

        try {
            serviceResourceResolver = this.getResourceResolver();
            Query query = this.queryBuilder.createQuery(PredicateGroup.create(map), serviceResourceResolver.adaptTo(Session.class));

            SearchResult result = query.getResult();
            List<Hit> hits = result.getHits();

            List<Map> propsList = hits.stream().map(hit -> {
                try {
                    Map<String, Object> properties = new HashMap();
                    for (Map.Entry<String, Object> entry : hit.getProperties().entrySet()) {
                        properties.put(entry.getKey(), entry.getValue());
                    }
                    return properties;
                } catch (RepositoryException e) {
                    LOGGER.error("An error occurred while trying to retrieve dialog items properties", e.getMessage());
                }
                return null;
            }).filter(props -> props != null)
              .collect(Collectors.toList());

            return propsList;
        } catch (LoginException e) {
            LOGGER.error("An error occurred while trying query the dialog properties", e.getMessage());
        } finally {
            if (serviceResourceResolver != null) {
                serviceResourceResolver.close();
            }
        }
        return null;
    }

    /**
     * Get the service resource resolver.
     * @return
     * @throws LoginException
     */
    private ResourceResolver getResourceResolver() throws LoginException {
        Map<String, Object> userServiceParam = new HashMap();
        userServiceParam.put(ResourceResolverFactory.SUBSERVICE, SERVICE_USER);

        return this.resourceResolverFactory.getServiceResourceResolver(userServiceParam);
    }
}
