package com.lk.o2o.service;

import com.lk.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService {

    public static final String ShopCategoryListKEY = "shopCategoryList";

    List<ShopCategory> getShopCategory(ShopCategory shopCategory);
}
