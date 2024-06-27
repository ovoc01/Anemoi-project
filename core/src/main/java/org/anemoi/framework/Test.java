package org.anemoi.framework;

import org.anemoi.framework.core.mapping.Controller;
import org.anemoi.framework.core.mapping.binding.GetMapping;

@Controller
public class Test {

    @GetMapping("status")
    public String index(){
        return null;
    }
}
