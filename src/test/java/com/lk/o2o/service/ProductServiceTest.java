package com.lk.o2o.service;

import com.lk.o2o.dto.ImageHolder;
import com.lk.o2o.dto.ProductExecution;
import com.lk.o2o.entity.Product;
import com.lk.o2o.entity.ProductCategory;
import com.lk.o2o.entity.Shop;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest{
    @Autowired
    private ProductService productService;

    @Test
    public void testQueryProductList(){
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(20L);
        product.setShop(shop);
        ProductExecution productExecution = productService.queryProductList(product, 0, 999);
        List<Product> productList = productExecution.getProductList();
        for (Product p : productList) {
            System.out.println(p.getProductName());
        }
    }

    @Test
    public void testAddProduct() throws FileNotFoundException {
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
        File file1 = new File("F:\\1.jpg");
        File file2 = new File("F:\\2.jpg");
        File file3 = new File("F:\\3.jpg");
        File file4 = new File("F:\\4.jpg");
        FileInputStream is1 = new FileInputStream(file1);
        FileInputStream is2 = new FileInputStream(file2);
        FileInputStream is3 = new FileInputStream(file3);
        FileInputStream is4 = new FileInputStream(file4);
        ImageHolder imageHolder = new ImageHolder(is1,"1.jpg");
        List<ImageHolder> imageHolders = new ArrayList<ImageHolder>();
        ImageHolder imageHolder2 = new ImageHolder(is2,"2.jpg");
        ImageHolder imageHolder3 = new ImageHolder(is3,"3.jpg");
        ImageHolder imageHolder4 = new ImageHolder(is4,"4.jpg");
        imageHolders.add(imageHolder2);
        imageHolders.add(imageHolder3);
        imageHolders.add(imageHolder4);
        ProductExecution productExecution = productService.addProduct(product, imageHolder, imageHolders);
        System.out.println(productExecution.getStateInfo());
    }
}
