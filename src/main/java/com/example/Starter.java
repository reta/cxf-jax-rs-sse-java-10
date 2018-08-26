package com.example;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.example.rest.PeopleRestService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class Starter {	
	public static void main(final String[] args) throws Exception {
        final Server server = new Server(8686);

         // Register and map the dispatcher servlet
        final CXFNonSpringJaxrsServlet cxfServlet = new CXFNonSpringJaxrsServlet();
        final ServletHolder cxfServletHolder = new ServletHolder(cxfServlet);
        cxfServletHolder.setInitParameter("jaxrs.serviceClasses", PeopleRestService.class.getName());
        cxfServletHolder.setInitParameter("jaxrs.providers", JacksonJsonProvider.class.getName());

        final ServletContextHandler context = new ServletContextHandler();
        context.setContextPath("/");
        context.addServlet(cxfServletHolder, "/*");

        server.setHandler(context);
        server.start();
        server.join();
    }
}
