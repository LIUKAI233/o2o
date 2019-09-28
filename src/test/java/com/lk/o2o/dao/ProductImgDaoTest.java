package com.lk.o2o.dao;

import com.lk.o2o.entity.ProductImg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductImgDaoTest{
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testDeleteProductImgByProductId(){
        int effectNum = productImgDao.deleteProductImagByProductId(15L);
        System.out.println(effectNum);
    }

    @Test
    public void testSelectProductImgByProductId(){
        List<ProductImg> productImgs = productImgDao.selectProductImgListByProductId(15L);
        for (ProductImg pi : productImgs) {
            System.out.println(pi.getImgAddr());
        }
    }

    @Test
    public void testInsertProductImgs(){
        List<ProductImg> productImgs = new ArrayList<ProductImg>();
        ProductImg p1 = new ProductImg();
        ProductImg p2 = new ProductImg();
        p1.setImgAddr("adad");
        p2.setImgAddr("dfdf");
        p1.setCreateTime(new Date());
        p2.setCreateTime(new Date());
        p1.setProductId(15L);
        p2.setProductId(15L);
        productImgs.add(p1);
        productImgs.add(p2);
        int i = productImgDao.insertProductImgs(productImgs);
        System.out.println(i);
    }
}
