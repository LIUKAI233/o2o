package com.lk.o2o.service;

import com.lk.o2o.dto.ProductCategoryExecution;
import com.lk.o2o.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    /*查询商品类别*/
    List<ProductCategory> queryProductCategory(Long shopId);

    /*批量插入商品类别*/
    ProductCategoryExecution addProductCategorys(List<ProductCategory> productCategoryList);

    /*删除商品类别*/
    ProductCategoryExecution deleteProductCategory(Long productCategoryId, Long shopId);
}
