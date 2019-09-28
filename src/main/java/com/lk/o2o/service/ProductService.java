package com.lk.o2o.service;

import com.lk.o2o.dto.ImageHolder;
import com.lk.o2o.dto.ProductExecution;
import com.lk.o2o.entity.Product;

import java.util.List;

public interface ProductService {

    /**
     * 添加商品信息，并处理图片
     * @param product 商品信息
     * @param image 图片信息名称
     * @param imageList 批量图片信息名称
     * @return 处理结果
     */
    ProductExecution addProduct(Product product, ImageHolder image, List<ImageHolder> imageList);

    /**
     * 更新商品信息
     * @param product 商品信息
     * @param image 图片信息名称
     * @param imageList 批量图片信息名称
     * @return 处理结果
     */
    ProductExecution modifyProduct(Product product, ImageHolder image, List<ImageHolder> imageList);

    /**
     * 查询商品信息
     * @param productId 商品ID
     * @return 处理结果
     */
    Product queryProductById(Long productId);

    /**
     * 根据输入条件，查询符合的商品列表 商品名(模糊),商品id，店铺id，商品状态，商品类别
     * @param product 查询条件
     * @param pageIndex 查询页数
     * @param pageSize 一页多少数据
     * @return 查询到的集合
     */
    ProductExecution queryProductList(Product product, Integer pageIndex, Integer pageSize);
}
