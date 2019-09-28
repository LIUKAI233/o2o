package com.lk.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.o2o.cache.JedisUtil;
import com.lk.o2o.dao.ShopCategoryDao;
import com.lk.o2o.entity.ShopCategory;
import com.lk.o2o.exceptions.ShopCategoryOperationException;
import com.lk.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;


    @Override
    public List<ShopCategory> getShopCategory(ShopCategory shopCategoryCondition) {
        //定义key前缀
        String key = ShopCategoryListKEY;
        //定义接收对象
        List<ShopCategory> shopCategoryList = null;
        //定义json处理对象
        ObjectMapper mapper = new ObjectMapper();
        //拼接处redis的key
        if(shopCategoryCondition == null){
            //若查询条件为空，则列出所有的首页大类,即parent_id为空的值
            key = key +"_allfirstlevel";
        }else if(shopCategoryCondition != null && shopCategoryCondition.getParent() != null
                && shopCategoryCondition.getParent().getShopCategoryId() != null){
            //列出所有该parent_id下的子类别
            key = key +"_parent"+shopCategoryCondition.getParent().getShopCategoryId();
        }else if (shopCategoryCondition != null){
            //列出所有的子类别
            key = key +"_allsecondlevel";
        }
        //判断redis中是否存在该key
        if(!jedisKeys.exists(key)){
            //不存在
            shopCategoryList = shopCategoryDao.queryShopCategory(shopCategoryCondition);
            try {
                //把返回的集合转换成JSON字符串
                String jsonString = mapper.writeValueAsString(shopCategoryList);
                //存到redis中
                jedisStrings.set(key,jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new ShopCategoryOperationException(e.getMessage());
            }
        }else {
            //存在
            //从redis中取key对应的字符串
            String jsonString = jedisStrings.get(key);
            //jsonString对应的java类型
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, ShopCategory.class);
            try {
                shopCategoryList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ShopCategoryOperationException(e.getMessage());
            }
        }
        return shopCategoryList;
    }
}
