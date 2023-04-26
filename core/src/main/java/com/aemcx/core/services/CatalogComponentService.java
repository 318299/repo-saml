package com.aemcx.core.services;

import org.apache.sling.api.resource.Resource;
import java.util.List;
import java.util.Map;

public interface CatalogComponentService {
    String CQ_DIALOG = "cq:dialog";
    String SERVICE_USER = "aemcx-catalogreadservice";

    String getDialogPath(Resource resource);

    List<Map> findProperties(String dialogPath);
}
