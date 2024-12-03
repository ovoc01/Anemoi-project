package org.anemoi.framework.dbmanager;

import org.anemoi.framework.dbmanager.brigdes.Connection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

public class App {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Connection c = new Connection();
        c.initializeDbReSource();
    }
}
