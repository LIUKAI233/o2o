package com.lk.o2o.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lk.o2o.cache.JedisUtil;
import com.lk.o2o.dao.HeadLineDao;
import com.lk.o2o.entity.HeadLine;
import com.lk.o2o.exceptions.HeadLineOperationException;
import com.lk.o2o.service.HeadLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Autowired
    private HeadLineDao headLineDao;
    @Autowired
    private JedisUtil.Keys jedisKeys;
    @Autowired
    private JedisUtil.Strings jedisStrings;


    @Override
    //查询符合条件的头条集合
    public List<HeadLine> queryHeadLineList(HeadLine headLineCondition) {
        //定义key的前缀
        String key = HeadLineListKEY;
        //定义接收对象
        List<HeadLine> headLineList = null;
        //拼接字符串
        if(headLineCondition != null && headLineCondition.getEnableStatus() != null){
            key = key + '_' + headLineCondition.getEnableStatus();
        }
        //定义jackson数据转换操作类
        ObjectMapper mapper = new ObjectMapper();
        //判断redis中key是否存在
        if(!jedisKeys.exists(key)){
            //不存在
            //取出头条集合
            headLineList = headLineDao.selectHeadLineList(headLineCondition);
            try {
                //把头条集合转换成json字符串
                String jsonString = mapper.writeValueAsString(headLineList);
                jedisStrings.set(key,jsonString);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new HeadLineOperationException(e.getMessage());
            }
        }else {
            //存在,从redis中取出key对应的值
            String jsonString = jedisStrings.get(key);
            //指定将String转换成的类型
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, HeadLine.class);
            try {
                //将相关key中对应的value的String转换成对应的实体类集合
                headLineList = mapper.readValue(jsonString, javaType);
            } catch (IOException e) {
                e.printStackTrace();
                throw new HeadLineOperationException(e.getMessage());
            }
        }
        return headLineList;
    }
}
