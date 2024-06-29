package org.anemoi.framework.core.modelview;


import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class ModelView {
    Map<String, Object> data;
    String view;

    public void addParameter(String key, Object value) {
        if (data == null) data = new HashMap<>();
        data.put(key, value);
    }

    public ModelView setView(String viewName) {
        this.view = viewName;
        return this;
    }
}
