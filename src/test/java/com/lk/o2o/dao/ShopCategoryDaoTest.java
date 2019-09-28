package com.lk.o2o.dao;

import com.lk.o2o.entity.ShopCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopCategoryDaoTest{
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Test
    public void testQueryShopCategory(){
        ShopCategory shopCategory = new ShopCategory();
        List<ShopCategory> shopCategories = shopCategoryDao.queryShopCategory(null);
        for (ShopCategory category : shopCategories ) {
            System.out.println(category);
        }
    }
}
