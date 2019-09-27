package com.lk.o2o.dao;

import com.lk.o2o.entity.ProductCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductCategoryDao {
    /*查询店铺类别列表*/
    List<ProductCategory> queryProductCategoryList(Long shopId);

    /*删除店铺商品类别*/
    int deleteProductCategory(@Param("productCategoryId") Long productCategoryId, @Param("shopId") Long shopId);

    /*批量添加商品列表*/
    int insertProductCategoryList(List<ProductCategory> productCategoryList);
}
