package com.lk.o2o.dao;

import com.lk.o2o.entity.HeadLine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HeadLineDaoTest{
    @Autowired
    private HeadLineDao headLineDao;

    @Test
    public void testSelectHeadLineList(){
        HeadLine headLine = new HeadLine();
        headLine.setEnableStatus(0);
        List<HeadLine> headLines = headLineDao.selectHeadLineList(headLine);
        for (HeadLine head: headLines) {
            System.out.println(head.getLineName());
        }
    }
}
