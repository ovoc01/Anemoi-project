package org.anemoi.features.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

public class AnemoiJettyServerImpl {
    private Server server;
    private int port;

    public AnemoiJettyServerImpl(int port) {
        this.port = port;
        server = new Server(port);
    }


    public void launch(){
        server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(this.port);
        
    }
}
