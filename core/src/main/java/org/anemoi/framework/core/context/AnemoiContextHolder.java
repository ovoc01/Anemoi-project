package org.anemoi.framework.core.context;

public final class AnemoiContextHolder {
    private static AnemoiContextHolder anemoiContextHolder;

    public AnemoiContextHolder getInstance(){
        if(anemoiContextHolder==null){
            anemoiContextHolder = new AnemoiContextHolder();
            return anemoiContextHolder;
        }
        return anemoiContextHolder;
    }
}
