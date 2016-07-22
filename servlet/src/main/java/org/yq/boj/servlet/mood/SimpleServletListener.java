package org.yq.boj.servlet.mood;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created on 2016/7/17.
 */
@WebListener()
public class SimpleServletListener implements ServletContextListener, ServletContextAttributeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServletListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("Context initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("Context destroyed");

    }

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        LOGGER.info("Attribute {} has been added, with value: {}", event.getName(), event.getValue());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        LOGGER.info("Attribute {} has been removed", event.getName());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        LOGGER.info("Attribute {} has been replaced, with value: {}", event.getName(), event.getValue());
    }
}
