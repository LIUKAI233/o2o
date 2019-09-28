package com.lk.o2o.service.impl;

import com.lk.o2o.dao.ProductCategoryDao;
import com.lk.o2o.dao.ProductDao;
import com.lk.o2o.dto.ProductCategoryExecution;
import com.lk.o2o.entity.ProductCategory;
import com.lk.o2o.enums.ProductCategoryStateEnum;
import com.lk.o2o.exceptions.ProductCategoryOperationException;
import com.lk.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;

    @Override
    /*查询该店铺的所有的商品分类*/
    public List<ProductCategory> queryProductCategory(Long shopId) {
        return productCategoryDao.queryProductCategoryList(shopId);
    }

    @Override
    /*添加店铺分类*/
    public ProductCategoryExecution addProductCategorys(List<ProductCategory> productCategoryList) {
        if(productCategoryList != null && productCategoryList.size() > 0){
            try {
                int effectNum = productCategoryDao.insertProductCategoryList(productCategoryList);
                if(effectNum <= 0){
                    throw new ProductCategoryOperationException("店铺类别创建失败");
                }else {
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ProductCategoryOperationException("addProductCategorys error"+e.getMessage());
            }
        }else{
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }

    @Override
    /*删除该店铺下的商品类别*/
    public ProductCategoryExecution deleteProductCategory(Long productCategoryId, Long shopId) {
        //将属于此类别的商品的类别id置为空
        try{
            productDao.updataProductCategoryToNullById(shopId,productCategoryId);
        }catch (Exception e){
            throw new ProductCategoryOperationException("商品类别置空错误"+e.getMessage());
        }
        //然后删除商品类别
        try{
            int effectNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if(effectNum <= 0){
                return new ProductCategoryExecution(ProductCategoryStateEnum.INNER_ERROR);
            }else{
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch(Exception e){
            throw new ProductCategoryOperationException("deleteProductCategory  error:"+e.getMessage());
        }

    }
}
