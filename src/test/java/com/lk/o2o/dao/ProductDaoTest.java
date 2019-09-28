package com.lk.o2o.dao;

import com.lk.o2o.entity.Product;
import com.lk.o2o.entity.ProductCategory;
import com.lk.o2o.entity.Shop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDaoTest{

    @Autowired
    private ProductDao productDao;

    @Test
    public void testqueryProduct(){
        Product product = productDao.queryProductById(14L);
        System.out.println(product.getProductName());
    }

    @Test
    public void testUpdataProductCategoryToNullById(){
        int i = productDao.updataProductCategoryToNullById(15L, 9L);
        System.out.println(i);
    }

    @Test
    public void testUpdataProduct(){
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(20L);
        product.setProductId(17L);
        /*product.setProductName("更改后的测试商品");*/
        product.setShop(shop);
        product.setEnableStatus(1);
        /*ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(13L);
        product.setProductCategory(productCategory);*/
        int i = productDao.updataProduct(product);
        System.out.println(i);
    }

    @Test
    public void testSelectProduct(){
        Product product1 = new Product();
        Shop shop = new Shop();
        shop.setShopId(20L);
        product1.setShop(shop);
        List<Product> products = productDao.selectProduct(product1,1,3);
        int i = productDao.selectCount(product1);
        System.out.println(i);
        for (Product product : products) {
            System.out.println(product.getProductName());
        }
    }

    @Test
    public void testInsertProduct(){
        Product product = new Product();
        product.setProductName("测试商品");
        product.setProductDesc("这是测试商品的描述");
        product.setNormalPrice("15");
        product.setPromotionPrice("12");
        product.setPriority(15);
        product.setImgAddr("adaa");
        product.setCreateTime(new Date());
        product.setEnableStatus(0);
        ProductCategory pc = new ProductCategory();
        pc.setProductCategoryId(11L);
        product.setProductCategory(pc);
        Shop shop = new Shop();
        shop.setShopId(20L);
        product.setShop(shop);
        int i = productDao.insertProduct(product);
        System.out.println("i的值为"+i);
        System.out.println(product.getProductId());
    }
}
