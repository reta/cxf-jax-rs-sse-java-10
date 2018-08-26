module com.example.cxf {
    exports com.example.rest;
    
    requires org.apache.cxf.rs.sse;
    requires org.apache.cxf.frontend.jaxrs;
    requires org.apache.cxf.transport.http;
    requires com.fasterxml.jackson.jaxrs.json;
    requires io.reactivex.rxjava2;
    
    requires transitive java.ws.rs;
    requires transitive org.reactivestreams;
    
    requires javax.servlet.api;
    requires jetty.server;
    requires jetty.servlet;
    requires jetty.util;
    
    requires java.xml.bind;
}