package com.aemcx.core.models;

import com.aemcx.core.utils.TagUtil;
import com.day.cq.tagging.Tag;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Model(adaptables = {Resource.class, SlingHttpServletRequest.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FilterModel {

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue(name = "tags")
    private String[] tags;

    private List<Tag> filters;

    //Issue - Return a copy of "filters".
	List<Tag> cloned_list;

    @PostConstruct
    protected void init() {
        this.filters = Arrays.stream(tags)
                .map(tag -> TagUtil.getTagManager(resourceResolver).resolve(tag))
                .collect(Collectors.toList());

                //Issue - Return a copy of "filters".
		this.cloned_list 
        = filters.stream() 
              .collect(Collectors.toList());
    }

    public List<Tag> getTags() {
        //Issue - Return a copy of "filters".
        //return this.filters;
		return this.cloned_list;
    }
}
