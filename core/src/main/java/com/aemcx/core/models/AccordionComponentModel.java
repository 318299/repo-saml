package com.aemcx.core.models;
 
import java.util.ArrayList;
import java.util.List;
 
import javax.annotation.PostConstruct;
import javax.inject.Inject;
 
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import com.aemcx.core.models.AccordionComponent;
 
@Model(adaptables = { SlingHttpServletRequest.class, Resource.class })
public class AccordionComponentModel {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(AccordionComponentModel.class);
 
    /**
     * Tabs title multifield value.
     */
    @Optional
    @Inject
    @Via("resource")
    private List<Resource> myUserSubmenu;
 
    private List<AccordionComponent> submenuItems;
 
    /**
     * Init Method of Model.
     */
    @PostConstruct
    public final void init() {
        populateMultiFieldItems(myUserSubmenu);
    }
 
    /**
    * Method to get Multi field data
    * @return submenuItems
    */
    private void populateMultiFieldItems(List<Resource> resourceList) {       
        if (null != resourceList && !resourceList.isEmpty()) {
            submenuItems = new ArrayList<AccordionComponent>();
            for (Resource item : resourceList) {
                if (item != null) {
                    AccordionComponent menuItem = new AccordionComponent();
                    ValueMap vm = item.getValueMap();
                    String title = getPropertyValue(vm, "title");
                    String message = getPropertyValue(vm, "message");
                    String link = getPropertyValue(vm, "link");
                    String imageLink = getPropertyValue(vm, "linkURL");
                    String restaurant = getPropertyValue(vm, "restaurant");
                    menuItem.setTitle(title);
                    menuItem.setMessage(message);
                    menuItem.setLink(link);
                    menuItem.setImageLink(imageLink);
                    menuItem.setRestaurant(restaurant);
                    submenuItems.add(menuItem);
                     
                } else {
                    LOGGER.info("ValueMap not found for resource  : {}", item);
                }
            }
        }
    }
     
    private String getPropertyValue(final ValueMap properties, final String propertyName) {
        return properties.containsKey(propertyName) ? properties.get(propertyName, String.class) : StringUtils.EMPTY;
    }
     
    public List<AccordionComponent> getMultiFieldItems() {
        return this.submenuItems;
    }
}