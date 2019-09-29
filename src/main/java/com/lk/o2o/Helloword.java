package com.lk.o2o;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Helloword {
    @RequestMapping(value = "hello",method = RequestMethod.GET)
    private String hello(){
        return "helloword";
    }
}
