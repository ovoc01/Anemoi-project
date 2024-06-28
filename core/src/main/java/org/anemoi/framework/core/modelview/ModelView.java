package org.anemoi.framework.core.modelview;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ModelView {
    Map<String, Object> data;
    String view;

    public void add(String key, Object value) {
        if (data == null) data = new HashMap<>();
        data.put(key, value);
    }

    public ModelView setView(String viewName) {
        this.view = viewName;
        return this;
    }
}
