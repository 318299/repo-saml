package com.aemcx.core.beans;

public class SearchResultBean {
    //PROPERTIES
    private String path;
    private String title;

    //GETTERS
    public String getTitle() {
        return title;
    }
    
    public String getPath() {
        return path;
    }

    //SETTERS
    public void setPath(String path) {
        this.path = path;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
