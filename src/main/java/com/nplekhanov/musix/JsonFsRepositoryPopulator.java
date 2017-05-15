package com.nplekhanov.musix;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by nplekhanov on 2/18/2017.
 */
public class JsonFsRepositoryPopulator implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        JsonRepository repository = new JsonRepository();
        RepositoryUsingMusix musix = new RepositoryUsingMusix(repository);
        musix.init();
        servletContextEvent.getServletContext().setAttribute("repository", repository);
        servletContextEvent.getServletContext().setAttribute("musix", musix);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        throw new UnsupportedOperationException();
    }
}
