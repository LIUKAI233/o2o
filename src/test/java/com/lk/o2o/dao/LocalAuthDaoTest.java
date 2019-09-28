package com.lk.o2o.dao;

import com.lk.o2o.entity.LocalAuth;
import com.lk.o2o.entity.PersonInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalAuthDaoTest {
    @Autowired
    private LocalAuthDao localAuthDao;

    @Test
    public void testqueryLocalByUsernameAndPassword(){
        LocalAuth localAuth = localAuthDao.queryLocalByUsernameAndPassword("xiangze", "s05bse6q2qlb9qblls96s592y55y556s");
        System.out.println(localAuth.getPersonInfo().getName());
    }

    @Test
    public void testqueryLocalByUserId(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(9L);
        System.out.println(localAuth.getPersonInfo().getName());
    }

    @Test
    public void testinsertLocalAuth(){
        LocalAuth localAuth = new LocalAuth();
        localAuth.setCreateTime(new Date());
        localAuth.setLastEditTime(new Date());
        localAuth.setPassword("123456");
        localAuth.setUsername("abc");
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(9L);
        localAuth.setPersonInfo(personInfo);
        int i = localAuthDao.insertLocalAuth(localAuth);
        System.out.println(i);
    }

    @Test
    public void testupdataLocalAuth(){
        int i = localAuthDao.updataLocalAuth(9L, "abc", "123456", "123",null);
        System.out.println(i);
    }
}
