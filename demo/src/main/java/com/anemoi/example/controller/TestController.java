package com.anemoi.example.controller;


import org.anemoi.framework.core.mapping.Controller;
import org.anemoi.framework.core.mapping.binding.*;
import org.anemoi.framework.core.modelview.ModelView;

@Controller
public class TestController {

    @GetMapping
    public ModelView index(){
        ModelView modelView = new ModelView();
        modelView.addParameter("message","Bonjour");
        return modelView.setView("index.jsp");
    }

}
