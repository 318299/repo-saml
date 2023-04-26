package com.aemcx.core.models;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.aemcx.core.utils.PageUtils;
import com.aemcx.core.utils.TagUtil;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardPanelModel {

    private static String TAGS_PARAM = "tags";
    private static String CARD_ITEM = "cardItem";
    private static String CARD_TAGS = "cardTags";

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private SlingHttpServletRequest request;

    @Inject
    private ListItem item;

    @ValueMapValue(name = "tagCategory")
    private String tagCategory;

    private Resource imageResource;

    private Boolean filter = true;

    @PostConstruct
    protected void init() {
        request.setAttribute(CARD_ITEM, item);
        request.setAttribute(CARD_TAGS, tagCategory);

        String[] tagsFilters = request.getParameterValues(TAGS_PARAM);
        if (tagsFilters != null) {
            String itemPath = item.getPath();

            Tag[] tags = TagUtil.getTags(itemPath + "/jcr:content", resourceResolver);
            this.filter = Arrays.stream(tagsFilters)
                    .map(tagFilter -> TagUtil.getTagManager(resourceResolver).resolveByTitle(tagFilter))
                    .anyMatch(tagFilter -> Arrays.stream(tags).anyMatch(tag -> tag.getPath().equals(tagFilter.getPath())));
        }
    }

    public Boolean getFilter() {
        return filter;
    }
}
