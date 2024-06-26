package org.anemoi.features.server;

import org.anemoi.features.server.defaultcontroller.DefaultServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public final class AnemoiJettyServerImpl {
    private Server server;
    private int port;
    
    
    private final  int maxThreads = 200;
    private final int minThreads = 10;
    private final int idleTimeout = 120;

    public AnemoiJettyServerImpl(int port) {
        this.port = port;
        server = new Server(port);
    }


    public void launch() throws Exception{
        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
        server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(this.port);
        server.setConnectors(new Connector[]{connector});

        ServletHandler servletHandler = new ServletHandler();
        server.setHandler(servletHandler);

        servletHandler.addServletWithMapping(DefaultServlet.class, "/status");


        server.start();
    }


    public void stop() throws Exception{
        server.stop();
    }


    
}
