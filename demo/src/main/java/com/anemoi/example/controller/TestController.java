package com.anemoi.example.controller;


import org.anemoi.framework.core.mapping.Controller;
import org.anemoi.framework.core.mapping.binding.*;

@Controller
public class TestController {

    @GetMapping("get")
    public String index(){
        return null;
    }

    @PostMapping("post")
    public String create(){
        return null;
    }

    @PutMapping("put")
    public String update(){
        return null;
    }

    @DeleteMapping("delete")
    public String delete(){
        return null;
    }



}
