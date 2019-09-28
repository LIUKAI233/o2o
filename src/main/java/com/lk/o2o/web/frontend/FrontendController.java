package com.lk.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/frontend",method = RequestMethod.GET)
public class FrontendController {

    @RequestMapping(value = "/index")
    private String index(){
        return "frontend/index";
    }

    @RequestMapping(value = "/shoplist")
    private String shopList(){
        return "frontend/shoplist";
    }

    @RequestMapping(value = "/shopdetail")
    private String shopDetail(){
        return "frontend/shopdetail";
    }

    @RequestMapping(value = "/productdetail")
    private String productDetail(){
        return "frontend/productdetail";
    }

}
