package com.aemcx.core.models;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.aemcx.core.utils.PageUtils;
import com.aemcx.core.utils.TagUtil;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import java.util.Arrays;
import java.util.Optional;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;


@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardModel {

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

    private Tag tag;

    @PostConstruct
    protected void init() {
        this.item = (ListItem) request.getAttribute(CARD_ITEM);
        request.removeAttribute(CARD_ITEM);
        String tagCategoryFilter = (String) request.getAttribute(CARD_TAGS);
        request.removeAttribute(CARD_TAGS);

        String itemPath = this.item.getPath();

        this.imageResource = this.getFirstImage(itemPath);

        Tag filterTag = TagUtil.getTagManager(resourceResolver).resolve(tagCategoryFilter);
        Tag[] tags = TagUtil.getTags(itemPath + "/jcr:content", resourceResolver);
        if (tags != null) {
            Optional<Tag> optionalTag = Arrays.stream(tags)
                    .filter(tag -> filterTag != null ? tag.getPath().contains(filterTag.getPath()) : true)
                    .findFirst();
            if (optionalTag.isPresent()) {
                this.tag = optionalTag.get();
            }
        }
    }

    public ListItem getItem() {
        return this.item;
    }

    public Resource getImageResource() {
        return this.imageResource;
    }

    public Tag getTag() {
        return tag;
    }

    private Resource getFirstImage(String path) {
        Resource page = resourceResolver.getResource(path);

        Optional<Resource> image = PageUtils.getRecursiveFirstResourceOfType(page, "aemcx/components/image");

        return image.isPresent() ? image.get() : null;
    }
}
