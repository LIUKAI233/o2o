package com.lk.o2o.dao;

import com.lk.o2o.entity.ProductCategory;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*这个注解开启test方法按名字顺序执行*/
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryTest{

    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void testAinsertProductCategoryList(){
        ProductCategory pc1 = new ProductCategory();
        pc1.setShopId(16L);
        pc1.setProductCategoryName("测试类别1");
        pc1.setCreateTime(new Date());
        pc1.setPriority(12);
        ProductCategory pc2 = new ProductCategory();
        pc2.setShopId(16L);
        pc2.setProductCategoryName("测试类别2");
        pc2.setCreateTime(new Date());
        pc2.setPriority(15);
        List<ProductCategory> list = new ArrayList<>();
        list.add(pc1);
        list.add(pc2);
        int i = productCategoryDao.insertProductCategoryList(list);
        System.out.println(i);
    }

    @Test
    public void testBqueryProductCategoryList(){
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(20L);
        for (ProductCategory pc: productCategoryList) {
            System.out.println(pc.getProductCategoryName());
        }
    }

    @Test
    public void testCDeleteProductCategory(){
        Long shopId = 16L;
        List<ProductCategory> list = productCategoryDao.queryProductCategoryList(shopId);
        for (ProductCategory pc : list) {
            if(pc.getProductCategoryName().equals("测试店铺1") || pc.getProductCategoryName().equals("测试店铺2")) {
                int i = productCategoryDao.deleteProductCategory(pc.getProductCategoryId(), shopId);
                System.out.println(i);
            }
        }
    }

}
