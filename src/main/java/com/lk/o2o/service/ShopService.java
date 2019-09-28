package com.lk.o2o.service;

import com.lk.o2o.dto.ImageHolder;
import com.lk.o2o.dto.ShopExecution;
import com.lk.o2o.entity.Shop;

public interface ShopService {

    /**
     * 根据shopCondition分页返回相应列表数据
     * @param shopCondition 查询条件
     * @param pageIndex 查询页数
     * @param pageSize 一页多少条数据
     * @return 处理结果
     */
    ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);

    /**
     * 根据传入的店铺id查找店铺信息
     * @param shopId 店铺ID
     * @return 店铺信息
     */
    Shop getShopById(Long shopId);

    /**
     * 更新店铺信息，包含对图片的处理
     * @param shop 店铺信息
     * @param thunbnail 图片相关信息
     * @return 处理结果
     */
    ShopExecution updataShop(Shop shop, ImageHolder thunbnail);

    /**
     * 添加店铺信息，包含对图片的处理
     * @param shop 店铺信息
     * @param thunbnail 图片相关信息
     * @return 处理结果
     */
    ShopExecution addShop(Shop shop, ImageHolder thunbnail);
}
