package org.anemoi.framework.dbmanager.mapping;

import java.sql.Connection;
import java.util.Map;

public class BddObject <T> {
    private final String  table;
    private Object id;



    public BddObject(String table) {
        this.table = table;
    }


    public T insert(Connection c){
        return null;
    }

    public T [] select(Connection c){
        return null;
    }

    public T update(Connection c){
        return null;
    }

    public void delete(Connection c){

    }

    public T [] find(Connection c, Map<String,Object> criteria){
        return null;
    }

    public T [] findPlainText(Connection c, String plainTextSearch){
        throw new UnsupportedOperationException("This features plainTextSearch is not implemented yet");
    }

}
