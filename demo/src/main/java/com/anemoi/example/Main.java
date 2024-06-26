package com.anemoi.example;

import org.anemoi.features.server.AnemoiJettyServerImpl;

public class Main {
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