package org.anemoi.framework.dbmanager.brigdes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DatabaseConfiguration{
    @JsonProperty("resources")
    List<DbResource> resources;

    public DatabaseConfiguration(){

    }
}