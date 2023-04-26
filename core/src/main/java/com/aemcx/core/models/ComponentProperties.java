package com.aemcx.core.models;

import com.aemcx.core.services.CatalogComponentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Model(adaptables = {Resource.class, SlingHttpServletRequest.class})
public class ComponentProperties {
    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name="reference", injectionStrategy= InjectionStrategy.OPTIONAL)
    private String reference;

    @ValueMapValue(name="propertiesType", injectionStrategy= InjectionStrategy.OPTIONAL)
    private String propertiesType;

    @SlingObject
    private Resource currentResource;

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private CatalogComponentService catalogComponentService;

    private List authoredProperties;
    private List<Map> dialogProperties;


    @PostConstruct
    protected void init() {
        Resource placeholder = resourceResolver.getResource(currentResource, reference);
        if (placeholder != null && placeholder.hasChildren()) {
            Resource catalogComponent = placeholder.getChildren().iterator().next();

            this.authoredProperties = new ArrayList();
            this.authoredProperties.add(catalogComponent.getValueMap());

            String dialogPath = catalogComponentService.getDialogPath(catalogComponent);
            if (StringUtils.isNotEmpty(dialogPath)) {
                this.dialogProperties = catalogComponentService.findProperties(dialogPath);
            }
        }
    }

    public List getProperties() {
        return "authored".equals(this.propertiesType) ? this.authoredProperties : this.dialogProperties;
    }
}
