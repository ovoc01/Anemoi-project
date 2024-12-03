package com.anemoi.example.controller;


import com.anemoi.example.Person;
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

    @GetMapping("params.ctl")
    public ModelView testingRequestParam(@ReqParam("dateOfBirth") Date date, @ReqParam("name") String nom,@ToObject Person person){
        ModelView modelView = new ModelView();
        modelView.addParameter("date",date);
        modelView.addParameter("nom",nom);
        return modelView.setView("index.jsp");
    }

}
