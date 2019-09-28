package com.lk.o2o.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheServiceTest{

    @Autowired
    private CacheService cacheService;

    @Test
    public void testremoveKey(){
        cacheService.removeKey("areaList");
    }
}
