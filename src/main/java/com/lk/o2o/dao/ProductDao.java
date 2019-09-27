package com.lk.o2o.dao;

import com.lk.o2o.entity.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductDao {
    /**
     * 插入店铺商品
     * @param product 商品信息
     * @return 影响的行数
     */
    int insertProduct(Product product);

    /**
     * 根据传入的商品信息，更改商品
     * @param product 商品信息
     * @return 影响的行数
     */
    int updataProduct(Product product);

    /**
     * 根据商品ID，查询商品信息
     * @param productId 商品id
     * @return  商品信息
     */
    Product queryProductById(Long productId);

    /**
     * 查询店铺商品
     * @param product 查询条件
     * @param rowIndex 从第几条数据开始
     * @param pageSize 一页多少数据
     * @return 查询出来的集合
     */
    List<Product> selectProduct(@Param("product") Product product, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 查询符合条件的商品数量
     * @param product 查询条件
     * @return 商品数量
     */
    int selectCount(@Param("product") Product product);

    /**
     * 把符合条件的商品的商品类别置为空
     * @param shopId 店铺id
     * @param productCategoryId 商品类别id
     * @return 影响的行数
     */
    int updataProductCategoryToNullById(@Param("shopId") Long shopId, @Param("productCategoryId") Long productCategoryId);
}
