package com.lk.o2o.dao;

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

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShopDaoTest{
    @Autowired
    private ShopDao shopDao;

    @Test
    public void testQueryShopList(){
        Shop shopCondition = new Shop();
        ShopCategory category = new ShopCategory();
        ShopCategory parent = new ShopCategory();
        parent.setShopCategoryId(10L);
        category.setParent(parent);
        shopCondition.setShopCategory(category);
        List<Shop> shopList = shopDao.queryShopList(shopCondition, 0, 5);
        int shopCount = shopDao.queryShopCount(shopCondition);
        System.out.println("shopList :"+shopList.size());
        System.out.println("shopCount :"+shopCount);
    }

    @Test
    public void testQueryByShopId(){
        Long shopId = 15L;
        Shop shop = shopDao.queryByShopId(shopId);
        System.out.println("区域名称"+shop.getArea().getAreaName()+shop.getShopCategory().getShopCategoryName());
    }

    @Test
    @Ignore
    public void testInsertShop(){
        Shop shop = new Shop();
        Area area = new Area();
        PersonInfo owner = new PersonInfo();
        ShopCategory shopCategory = new ShopCategory();
        area.setAreaId(3L);
        owner.setUserId(11L);
        shopCategory.setShopCategoryId(12L);
        shop.setEnableStatus(1);
        shop.setCreateTime(new Date());
        shop.setAdvice("无");
        shop.setArea(area);
        shop.setShopName("tttest");
        shop.setShopCategory(shopCategory);
        shop.setOwner(owner);
        int i = shopDao.insertShop(shop);
        System.out.println(i+"................"+shop.getShopId());
    }

    @Test
    public void testUpdateShop(){
        Shop shop = new Shop();
        shop.setShopId(26L);
        shop.setShopImg("\\upload\\images\\item\\shop\\26\\2017060609431259039.png");
        int i = shopDao.updateShop(shop);
        System.out.println(i);
    }
}
