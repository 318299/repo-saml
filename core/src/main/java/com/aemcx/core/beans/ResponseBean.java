package com.aemcx.core.beans;

import java.util.ArrayList;

public class ResponseBean {
    //PROPERTIES
    private Long numFound;
    private ArrayList<SearchResultBean> responseSearchResultBean;
    
    //GETTER
    public Long getNumFound() {
        return numFound;
    }
    public ArrayList<SearchResultBean> getResponseSearchResultBean() {
        return responseSearchResultBean;
    }

    //SETTERS
    public void setNumFound(Long numFound) {
        this.numFound = numFound;
    }
    public void setResponseSearchResultBean(ArrayList<SearchResultBean> responseSearchResultBean) {
        this.responseSearchResultBean = responseSearchResultBean;
    }
}
