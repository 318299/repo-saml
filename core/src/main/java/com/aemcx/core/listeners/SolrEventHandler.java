package com.aemcx.core.listeners;

import com.aemcx.core.services.PageService;
import com.day.cq.replication.ReplicationAction;
import com.day.cq.replication.ReplicationActionType;

import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service = EventHandler.class, immediate = true, property = {
        Constants.SERVICE_DESCRIPTION + "= Index data to solr on page activation",
        EventConstants.EVENT_TOPIC + "=" + ReplicationAction.EVENT_TOPIC
        })

public class SolrEventHandler implements EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolrEventHandler.class);

    @Reference
    PageService pageService;

    @Override
    public void handleEvent(Event event) {
        ReplicationAction replicationAction = ReplicationAction.fromEvent(event);


        //LOGGERS 
        LOGGER.info(replicationAction.toString());
        LOGGER.info("------- I Am Listening -----------");  


        //On Page activation/publish this will create document in Solr by calling PageService
        if(replicationAction.getType() == ReplicationActionType.ACTIVATE) {
            LOGGER.info("Event Topic = {}, Path = {}",event.getTopic(),
            replicationAction.getPath());

            String currentPage = replicationAction.getPath();
            LOGGER.info(currentPage);

            pageService.addSolrDoc(currentPage);
            

        }

        //On page de-activation/unpublish this will delete document in solr related to page
        if (replicationAction.getType() == ReplicationActionType.DEACTIVATE){
            LOGGER.info("Event Topic = {}, Path = {}",event.getTopic(),
            replicationAction.getPath());

            String docName = replicationAction.getPath().replaceAll("/", "-");
            LOGGER.info(docName);
            pageService.deleteSolrDoc(docName);

        }
        

    }
}