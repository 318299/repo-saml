package com.aemcx.core.utils;

import org.apache.sling.api.resource.Resource;

import java.util.Optional;
import java.util.stream.StreamSupport;

public class PageUtils {

    public static Optional<Resource> getRecursiveFirstResourceOfType(final Resource resource,
                                                                     final String resourceType) {

        return StreamSupport.stream(resource.getChildren().spliterator(), false)
                .map(childResource -> {
                    boolean isResourceType = childResource.isResourceType(resourceType);

                    if (isResourceType) {
                        return childResource;
                    } else {
                        Optional<Resource> res = getRecursiveFirstResourceOfType(childResource, resourceType);
                        return res.isPresent() ? res.get() : null;
                    }
                })
                .filter(childResource -> childResource != null)
                .findFirst();
    }
}
