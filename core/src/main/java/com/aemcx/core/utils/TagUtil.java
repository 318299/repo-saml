package com.aemcx.core.utils;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

public class TagUtil {

    /**
     *
     * @param path
     * @param resourceResolver
     * @return
     */
    public static Tag[] getTags(String path, ResourceResolver resourceResolver) {
        Resource page = resourceResolver.getResource(path);

        return getTagManager(resourceResolver).getTags(page);
    }

    /**
     *
     * @param resourceResolver
     * @return
     */
    public static TagManager getTagManager(ResourceResolver resourceResolver) {
        return resourceResolver.adaptTo(TagManager.class);
    }
}
