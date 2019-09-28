package com.lk.o2o.service;

import com.lk.o2o.dto.LocalAuthExecution;
import com.lk.o2o.entity.LocalAuth;
import com.lk.o2o.entity.PersonInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthServiceTest{
    @Autowired
    private LocalAuthService localAuthService;

    @Test
    public void testaddLocalAuth(){
        LocalAuth localAuth = new LocalAuth();
        localAuth.setUsername("qwer1232");
        localAuth.setPassword("123456");
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(11L);
        localAuth.setPersonInfo(personInfo);
        LocalAuthExecution localAuthExecution = localAuthService.addLocalAuth(localAuth);
        System.out.println(localAuthExecution.getStateInfo());
    }

    @Test
    public void testmodifyLocalAuth(){
        LocalAuthExecution execution = localAuthService.modifyLocalAuth(11L, "qwer1232", "1234456", "123456");
        System.out.println(execution.getStateInfo());
    }
}
