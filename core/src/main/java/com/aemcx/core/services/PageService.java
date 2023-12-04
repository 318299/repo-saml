package com.aemcx.core.services;


public interface PageService {
    public void addSolrDoc(String currentPagePath);
    public void deleteSolrDoc (String docName);
    public String fecthDocs(String parameterString);
}
