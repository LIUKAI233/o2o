package com.lk.o2o.dao;

import com.lk.o2o.entity.ProductImg;

import java.util.List;

public interface ProductImgDao {
    /**
     * 批量插入商品图片
     * @param productImgList 关于商品图片的集合
     * @return  处理结果
     */
    int insertProductImgs(List<ProductImg> productImgList);

    /**
     * 根据传入的商品ID，删除商品详情图
     * @param productId 商品ID
     * @return 处理结果
     */
    int deleteProductImagByProductId(Long productId);

    /**
     * 根据传入的商品ID，查询商品详情图
     * @param productId 商品ID
     * @return 图片信息集合
     */
    List<ProductImg> selectProductImgListByProductId(Long productId);
}
