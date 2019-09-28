package com.lk.o2o.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/local")
public class LocalController {

    @RequestMapping("accountbind")
    private String accoutBind(){
        return "local/accountbind";
    }

    @RequestMapping("changepsw")
    private String changePsw(){
        return "local/changepsw";
    }

    @RequestMapping("login")
    private String login(){
        return "local/login";
    }
}
