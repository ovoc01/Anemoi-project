package org.anemoi.framework.dbmanager.brigdes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DbResource {
    @JsonProperty("name")
    String name;
    @JsonProperty("jdbcUrl")
    String jdbcUrl;
    @JsonProperty("username")
    String username;
    @JsonProperty("password")
    String password;
    @JsonProperty("pool")
    boolean isPool;

    @JsonProperty("dataSourceProperty")
    List<DataSourceProperty> datasourceProperty;

    public DbResource() {

    }


    static DbResource[] retrieveDbResourceInformation() throws IOException, URISyntaxException {
        URL url = DbResource.class.getClassLoader().getResource("db.json");
        if (url == null)
            throw new FileNotFoundException("Database file configuration not found: create db.json at src/main/resources ");
        File dbJson = new File(url.toURI());
        InputStream inputStream = new FileInputStream(dbJson);
        String inputStreamContent = new String(inputStream.readAllBytes());
        System.out.println(inputStreamContent);
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        ObjectMapper objectMapper = new ObjectMapper();
        DatabaseConfiguration config = objectMapper.readValue(url, DatabaseConfiguration.class);


        //DbResource[] dbResources = new Gson().fromJson(inputStreamContent, DbJsonPOJO.class);

        return config.getResources().toArray(new DbResource[0]);
    }

    @Data
    public static class DataSourceProperty {
        @JsonProperty("key")
        String key;
        @JsonProperty("value")
        String value;

        public DataSourceProperty() {

        }
    }
}