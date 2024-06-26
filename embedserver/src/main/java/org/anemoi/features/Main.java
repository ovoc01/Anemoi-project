package org.anemoi.features;

import org.anemoi.features.server.AnemoiJettyServerImpl;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Main {
    static
    {
        SLF4JBridgeHandler.install();
    }
    public static void main(String[] args) throws Exception{
        AnemoiJettyServerImpl server = new AnemoiJettyServerImpl(9090);
        try {
            server.launch();
        } catch (Exception e) {
            e.printStackTrace();
            server.stop();
        }
    }
}