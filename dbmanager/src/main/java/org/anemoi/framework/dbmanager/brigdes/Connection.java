package org.anemoi.framework.dbmanager.brigdes;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


import java.io.*;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Connection {
    private HikariConfig config = new HikariConfig();
    private HikariDataSource ds;
    private Map<String, HikariDataSource> hikariDataSourceMap;
    private Map<String, DbResource> dbResourceMap;

    static Connection c;

    public static void setConnection(Connection c) {
        Connection.c = c;
    }

    public Connection getInstance() {
        if (c == null) return null;
        return c;
    }

    public void initializeDatabaseConnection() {
        Connection temp = new Connection();
        Connection.setConnection(temp);
    }


    public void initializeDbReSource() throws IOException, URISyntaxException {
        DbResource[] dbResources = DbResource.retrieveDbResourceInformation();
        Map<String, DbResource> dbResourceStream = new ConcurrentHashMap<>();
        Arrays.stream(dbResources).forEach(dbResource -> {
            dbResourceStream.put(dbResource.getName(), dbResource);
        });
        this.dbResourceMap = dbResourceStream;
    }

    HikariDataSource initializeHikariDatasourceByName(String connectionName) {
        return null;
    }

    public HikariDataSource getHikariDataSourceByName(String name) {
        HikariDataSource hikariDataSource = this.hikariDataSourceMap.get(name);
        if (hikariDataSource == null) {
            hikariDataSource = initializeHikariDatasourceByName(name);
        }
        return hikariDataSource;
    }


    public java.sql.Connection getConnection(String name) throws SQLException {
        return getHikariDataSourceByName(name).getConnection();
    }

    public HikariDataSource getDefaultDataSource() {
        throw new UnsupportedOperationException("Function not implemented");
    }

    public java.sql.Connection getDefaultApplicationConnection() throws SQLException {
        return getDefaultDataSource().getConnection();
    }
}
