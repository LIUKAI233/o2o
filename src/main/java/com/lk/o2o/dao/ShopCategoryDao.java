package com.lk.o2o.dao;

import com.lk.o2o.entity.ShopCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopCategoryDao {
    /**
     * 查询商铺列表
     * @param shopCategoryCondition 查询条件
     * @return 符合条件的店铺列表
     */
    List<ShopCategory> queryShopCategory(@Param("shopCategoryCondition") ShopCategory shopCategoryCondition);
}
