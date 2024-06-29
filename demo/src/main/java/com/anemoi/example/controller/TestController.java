package com.anemoi.example.controller;


import org.anemoi.framework.core.mapping.Controller;
import org.anemoi.framework.core.mapping.binding.*;
import org.anemoi.framework.core.modelview.ModelView;

import java.util.Date;

@Controller
public class TestController {

    @GetMapping
    public ModelView index(){
        ModelView modelView = new ModelView();
        modelView.addParameter("message","Bonjour");
        return modelView.setView("index.jsp");
    }

    @GetMapping("params")
    public ModelView testingRequestParam(@ReqParam("date") Date date, @ReqParam("nom") String nom){
        ModelView modelView = new ModelView();
        modelView.addParameter("date",date);
        modelView.addParameter("nom",nom);
        return modelView.setView("index.jsp");
    }

}
