package com.lk.o2o.service;

import com.lk.o2o.dto.ImageHolder;
import com.lk.o2o.dto.ShopExecution;
import com.lk.o2o.entity.Area;
import com.lk.o2o.entity.PersonInfo;
import com.lk.o2o.entity.Shop;
import com.lk.o2o.entity.ShopCategory;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopServiceTest {
    @Autowired
    private ShopService shopService;

    @Test
    public void testGetShopList(){
        PersonInfo user = new PersonInfo();
        user.setUserId(8L);
        Shop shopCondition = new Shop();
        shopCondition.setOwner(user);
        ShopExecution se = shopService.getShopList(shopCondition, 3, 5);
        System.out.println("该页面店铺数量："+se.getShopList().size());
        System.out.println("该用户总店铺数量"+se.getCount());
    }

    @Test
    public void testUpdataShop() throws FileNotFoundException{
        Shop shop = new Shop();
        shop.setShopId(28L);
        shop.setShopName("更改店铺");
        File shopImg = new File("F:\\new.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(is, shopImg.getName());
        ShopExecution shopExecution = shopService.updataShop(shop, imageHolder);
        System.out.println(shopExecution.getStateInfo());
    }

    @Test
    @Ignore
    public void testAddShop() throws FileNotFoundException {
        Shop shop = new Shop();
        Area area = new Area();
        PersonInfo owner = new PersonInfo();
        ShopCategory shopCategory = new ShopCategory();
        area.setAreaId(3L);
        owner.setUserId(11L);
        shopCategory.setShopCategoryId(12L);
        shop.setAdvice("无");
        shop.setArea(area);
        shop.setShopName("ttteeeest");
        shop.setShopCategory(shopCategory);
        shop.setOwner(owner);
        File shopImg = new File("F:\\test.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder(is, shopImg.getName());
        ShopExecution shopExecution = shopService.addShop(shop, imageHolder);
        System.out.println(shopExecution.getShop().getShopId());
    }
}
