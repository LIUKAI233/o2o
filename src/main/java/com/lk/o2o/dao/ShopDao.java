package com.lk.o2o.dao;

import com.lk.o2o.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopDao {

    /**
     * 分页查询店铺，可输入的状态有店铺名（模糊），店铺状态，店铺类别，区域id，owner
     * @param shopCondition
     * @param rowIndex  从第几行开始取数据
     * @param pageSize  返回的条数
     * @return
     */
    List<Shop> queryShopList(@Param("shopCondition") Shop shopCondition, @Param("rowIndex") int rowIndex, @Param("pageSize") int pageSize);

    /**
     * 根据传入的条件，查询符合条件的数据的总数
     * @param shopCondition
     * @return
     */
    int queryShopCount(@Param("shopCondition") Shop shopCondition);

    /**
     * 插入商铺信息
     * @return 插入数据行数
     */
    int insertShop(Shop shop);

    /**
     * 更新商铺信息
     * @return 更改数据行数
     */
    int updateShop(Shop shop);

    /**
     * 根据店铺id查询店铺信息
     * @param shopId
     * @return
     */
    Shop queryByShopId(Long shopId);
}
